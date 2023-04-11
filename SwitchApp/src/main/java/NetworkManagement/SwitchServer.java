package NetworkManagement;

import UI.ServerAppUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.CRC32;

public class SwitchServer {
    private int clientServerPort;
    private int PORT;
    private ServerSocket serverSocket;
    private ARPTable arpTable;
    private ServerAppUI serverAppUI;

    public SwitchServer(int PORT, ServerAppUI serverAppUI) throws IOException {
        this.PORT = PORT;
        this.serverAppUI = serverAppUI;
        this.serverSocket = new ServerSocket(PORT);
        this.arpTable = new ARPTable(serverAppUI);
        this.clientServerPort = 9090;

        serverAppUI.setArpTable(this.arpTable);
    }

    public void startServer() throws IOException, ClassNotFoundException {
        System.out.println("Servidor iniciado en puerto " + PORT + "...");
        while(true) {
            Socket socket = serverSocket.accept();

            // get ip
            String ip = socket.getInetAddress().toString().replace("/", "");
            int port = socket.getPort();

            // get the input stream from the connected socket
            InputStream input = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(input);

            // Get the object
            NetworkFrame frame = (NetworkFrame) objectInputStream.readObject();
//            System.out.println("New Client Conencted: " + socket.toString());
//            System.out.println("Interface/ip: " + ip);
//            System.out.println("MAC Origen: " + frame.getMacOrigin());
//            System.out.println("MAC Destino: " + frame.getMacDestiny());
//            System.out.println("Mensaje recibido: " + frame.getMessage());




            if (frame.getType() == 0) {
                serverAppUI.appendOutputText("Se está intentando desconectar la siguiente MAC");
                serverAppUI.appendOutputText("MAC: " + frame.getMacOrigin());
                serverAppUI.appendOutputText("Esperando respuesta del switch");

                NetworkFrame feedbackFrame = new NetworkFrame(0, frame.getMacOrigin());
                if(arpTable.disconnectDevice(frame.getMacOrigin())) {
                    feedbackFrame.setMessage("Se desconectó con éxito el dispositivo");
                } else {
                    feedbackFrame.setMessage("No fue posible desconectar el dispositivo");
                }
                MessageManager messageManager = new MessageManager(frame.getMacOrigin(), feedbackFrame, ip, clientServerPort, serverAppUI);
                messageManager.run();
            } else if(frame.getType() == 1) {
                ARPNode node = new ARPNode(ip, port, frame.getMacOrigin(), false);
                serverAppUI.appendOutputText("El siguiente dispositivo está intentando establecer conexión");
                serverAppUI.appendOutputText("Interface (IP): " + ip);
                serverAppUI.appendOutputText("MAC Origen: " + frame.getMacOrigin());
                serverAppUI.appendOutputText("Esperando respuesta del Switch...");

                if(arpTable.addDevice(node)) {
                    serverAppUI.appendOutputText("El dispositivo fue añadido y conectado con éxito");
                    NetworkFrame secondaryFrame = new NetworkFrame(1, frame.getMacOrigin());
                    secondaryFrame.setMessage("La conexión se realizó con éxito");
                    MessageManager manager = new MessageManager(frame.getMacOrigin(), secondaryFrame, ip, clientServerPort, serverAppUI);
                    manager.run();
                } else { // Cannot add device
                    serverAppUI.appendOutputText("El dispositivo no pudo ser añadido con éxito, probablemente la dirección MAC ya esté en uso");
                    NetworkFrame secondaryFrame = new NetworkFrame(5, frame.getMacOrigin());
                    secondaryFrame.setMessage("No se pudo agregar el dispositivo a la tabla ARP.");
                    MessageManager manager = new MessageManager(frame.getMacOrigin(), secondaryFrame, ip, clientServerPort, serverAppUI);
                    manager.run();
                }

                serverAppUI.appendOutputText("");
            } else if(frame.getType() == 3) { // Client Broadcast Message
                serverAppUI.appendOutputText("Se está intentando enviar un mensaje entre clientes");
                serverAppUI.appendOutputText("MAC Origen: " + frame.getMacOrigin());
                serverAppUI.appendOutputText("MAC Destino: " + frame.getMacDestiny());
                serverAppUI.appendOutputText("Datos (Mensaje): " + frame.getMessage());
                serverAppUI.appendOutputText("CRC: " + getCrc(frame.getMessage()));
                serverAppUI.appendOutputText("Esperando respuesta...");

                // Send broadcast message
                for(ARPNode node : arpTable.getArpTable()) {
                    if(!node.getMac().equals(frame.getMacOrigin()) && node.isEnabled()) {
                        serverAppUI.appendOutputText("Enviando Broadcast: " + node.getMac());
                        MessageManager messageManager = new MessageManager(frame.getMacDestiny(), frame, node.getIp(), clientServerPort, serverAppUI);
                        messageManager.run();
                    }
                }

                serverAppUI.appendOutputText("");
            } else if(frame.getType() == 4) { // Unicast Message
                serverAppUI.appendOutputText("Se recibió respuesta de " + frame.getMacDestiny());
                serverAppUI.appendOutputText("Enviando unicast a " + frame.getMacOrigin());
                ARPNode node;
                if(( node = arpTable.getDevice(frame.getMacDestiny())) != null) {
                    MessageManager messageManager = new MessageManager(frame.getMacDestiny(), frame, node.getIp(), clientServerPort, serverAppUI);
                    messageManager.run();

                } else {
                    serverAppUI.appendOutputText("Ocurrió un error al enviar el unicast message");
                }
            }

            System.out.println();
        }
    }

    private String getCrc(String data) {
        CRC32 crc = new CRC32();
        crc.update(data.getBytes());
        return String.valueOf(crc.getValue());
    }
}
