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
    private String SERVER_IP;
    private int SERVER_PORT;
    private NetworkFrame frame;


    @Override
    public void run() {
        connect();
    }

    public Client(String SERVER_IP, int SERVER_PORT, NetworkFrame frame) {
        this.SERVER_IP = SERVER_IP;
        this.SERVER_PORT = SERVER_PORT;
        this.frame = frame;
    }

    private void connect() {
        System.out.println("Estableciendo Conexión");
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {

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
