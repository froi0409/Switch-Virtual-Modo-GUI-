package NetworkManagement;

import com.example.clientapp.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageManager implements Runnable {

    private MainActivity mainActivity;

    private int port;
    private ServerSocket serverSocket;

    public MessageManager(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        startServer();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(6063);

            while(true) {
                Socket socket = serverSocket.accept();

                // get the input stream from the connected socket
                InputStream input = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(input);

                // get the object (Network Frame)
                NetworkFrame frame = (NetworkFrame) objectInputStream.readObject();

                if (frame.getType() == 3 || frame.getType() == 4) {
                    mainActivity.appendText(frame.renderMessage());
                }

            }
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
