package NetworkManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client implements Runnable{
    private String serverIp;
    private int serverPort;
    private NetworkFrame frame;


    @Override
    public void run() {
        connect();
    }

    public Client(String serverIp, int serverPort, NetworkFrame frame) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.frame = frame;
    }

    private void connect() {
        System.out.println("Estableciendo Conexión");
        try (Socket socket = new Socket(serverIp, serverPort)) {

            System.out.println("Conexión Establecida");

            OutputStream output = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);

            // Send Frame to SwitchApp
            objectOutputStream.writeObject(frame);
            System.out.println("Trama Enviada a Switch");

            output.close();
            objectOutputStream.close();
        } catch(IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
