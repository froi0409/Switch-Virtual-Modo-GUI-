package NetworkManagement;

import android.app.Activity;
import android.widget.EditText;

import com.example.clientapp.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MessageManager extends Activity {

    private EditText txtTextArea;

    private int port;


    public MessageManager(EditText txtTextArea) {
        this.txtTextArea = txtTextArea;
    }



    private void startServer() {
        System.out.println("XD");
    }

}
