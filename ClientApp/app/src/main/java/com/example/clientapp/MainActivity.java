package com.example.clientapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private EditText txtMessage;
    private FloatingActionButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting the Text color of the EditText
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtMessage.setTextColor(Color.parseColor("#B66d38"));

        // ViewsById
        btnSend = findViewById(R.id.btnSend);



        // Listeners
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Mensaje: " + txtMessage.getText());
            }
        });

    }
}