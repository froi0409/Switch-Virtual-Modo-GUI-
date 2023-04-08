package NetworkManagement;

import UI.ServerAppUI;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SwitchServer {
    private int PORT;
    private ServerSocket serverSocket;
    private ARPTable arpTable;
    private ServerAppUI serverAppUI;

    public SwitchServer(int PORT, ServerAppUI serverAppUI) throws IOException {
        this.PORT= PORT;
        this.serverAppUI = serverAppUI;
        this.serverSocket = new ServerSocket(PORT);
        this.arpTable = new ARPTable();
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
            System.out.println("New Client Conencted: " + socket.toString());
            System.out.println("Interface/ip: " + ip);
            System.out.println("MAC Origen: " + frame.getMacOrigin());
            System.out.println("MAC Destino: " + frame.getMacDestiny());
            System.out.println("Mensaje recibido: " + frame.getMessage());


            if(frame.getType() == 1) {
                ARPNode node = new ARPNode(ip, port, frame.getMacOrigin(), false);
                if(arpTable.addDevice(node)) {
                    System.out.println("Dispositivo agregado a tabla ARP con éxito");

                } else {
                    System.err.println("Error al aguregar dispositivo a la tabla ARP");
                }
            }

            System.out.println();
        }
    }
}
