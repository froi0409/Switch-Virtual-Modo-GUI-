package NetworkManagement;

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

    @Override
    public void run() {
        connect();
    }

    public MessageManager(String macAddress, NetworkFrame frame, String clientIp, int clientPort) {
        this.macAddress = macAddress;
        this.frame = frame;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
    }

    private void connect() {
        try (Socket socket = new Socket(clientIp, clientPort)) {

            System.out.println("Enviando respuesta a " + macAddress);

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);

            // Send frame to ClientApp
            objectOutputStream.writeObject(frame);
            objectOutputStream.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
