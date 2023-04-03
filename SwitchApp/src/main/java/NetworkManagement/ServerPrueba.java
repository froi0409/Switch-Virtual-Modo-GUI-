package NetworkManagement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerPrueba {
    int PORT;
    ServerSocket serverSocket;

    public ServerPrueba(int PORT) throws IOException {
        this.PORT= PORT;
        this.serverSocket = new ServerSocket(PORT);
    }

    public void startServer() throws IOException, ClassNotFoundException {
        System.out.println("Servidor iniciado en puerto " + PORT + "...");
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("New Client Conencted: " + socket.toString());

            // get the input stream from the connected socket
            InputStream input = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(input);

            // Get the object
            NetworkFrame frame = (NetworkFrame) objectInputStream.readObject();
            System.out.println("Mensaje recibido: " + frame.getText());

        }
    }
}
