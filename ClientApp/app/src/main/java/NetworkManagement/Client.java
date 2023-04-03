package NetworkManagement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client implements Runnable{
    String SERVER_IP;
    int SERVER_PORT;


    @Override
    public void run() {
        connect();
    }

    public Client(String SERVER_IP, int SERVER_PORT) {
        this.SERVER_IP = SERVER_IP;
        this.SERVER_PORT = SERVER_PORT;
    }

    private void connect() {
        System.out.println("Estableciendo Conexión");
        try {
            Socket socket = new Socket(SERVER_IP, SERVER_PORT);
            System.out.println("Conexión Establecida");

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            System.out.println(reader.readLine());
        } catch(IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
