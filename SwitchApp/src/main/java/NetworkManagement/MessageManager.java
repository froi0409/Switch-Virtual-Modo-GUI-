package NetworkManagement;

import UI.ServerAppUI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MessageManager implements Runnable {
    private String macAddress;
    private NetworkFrame frame;
    private String clientIp;
    private int clientPort;
    private ServerAppUI serverAppUI;

    @Override
    public void run() {
        connect();
        serverAppUI.appendOutputText("El mensaje fue enviado con éxito");
    }

    public MessageManager(String macAddress, NetworkFrame frame, String clientIp, int clientPort, ServerAppUI serverAppUI) {
        this.macAddress = macAddress;
        this.frame = frame;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.serverAppUI = serverAppUI;
    }

    private void connect() {
        System.out.println("IP: " + clientIp + " - port: " + clientPort);
        try (Socket socket = new Socket(clientIp, clientPort)) {

            System.out.println("Enviando respuesta a " + macAddress);

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);

            // Send frame to ClientApp
            objectOutputStream.writeObject(frame);
            objectOutputStream.close();

        } catch (IOException e) {
            System.err.println("Error al Enviar Mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
