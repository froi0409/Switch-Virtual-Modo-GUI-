package com.example.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLOutput;

import NetworkManagement.Client;

public class MainActivity extends AppCompatActivity {

    private EditText txtMessage;
    private FloatingActionButton btnSend;
    private Thread thread1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting the Text color of the EditText
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtMessage.setTextColor(Color.parseColor("#B66d38"));

        // ViewsById
        btnSend = findViewById(R.id.btnSend);

        // Variables
        String SERVER_IP = "192.168.137.1";
        int SERVER_PORT = 6868;

        // Listeners
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Mensaje: " + txtMessage.getText());
                Client client = new Client(SERVER_IP, SERVER_PORT);
                client.run();

            }
        });


        // Solve Connection Error
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
}