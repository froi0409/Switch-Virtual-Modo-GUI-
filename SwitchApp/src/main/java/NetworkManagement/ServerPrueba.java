package NetworkManagement;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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

    public void startServer() throws IOException {
        System.out.println("Servidor iniciado en puerto " + PORT + "...");
        while(true) {
            Socket socket = serverSocket.accept();
            System.out.println("New Client Conencted");
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println(new Date().toString());
        }
    }
}
