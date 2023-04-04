package com.example.clientapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import NetworkManagement.AddressManager;
import NetworkManagement.Client;
import NetworkManagement.NetworkFrame;
import UIManagement.MACAddressDialog;

public class MainActivity extends AppCompatActivity implements MACAddressDialog.MACAddressListener {

    private AddressManager addressManager = new AddressManager();
    private EditText txtMessage;
    private EditText txtMacDestiny;
    private FloatingActionButton btnSend;
    private Button btnSetMacAddress;
    private EditText txtTextArea;
    private Thread thread1;
    private String[] MAC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewsById
        btnSend = findViewById(R.id.btnSend);
        btnSetMacAddress = findViewById(R.id.btnSetMacAddress);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtTextArea = findViewById(R.id.txtTextArea);
        txtMacDestiny = findViewById(R.id.txtMacDestiny);

        // Setting the Text color of the EditText
        txtMessage.setTextColor(Color.parseColor("#B66d38"));
        txtTextArea.setTextColor(Color.BLACK);
        txtMacDestiny.setBackgroundColor(Color.parseColor("#EAE2CC"));
        txtMacDestiny.setTextColor(Color.parseColor("#336b6f"));
        txtMacDestiny.setHintTextColor(Color.parseColor("#336b6f"));
        txtTextArea.setText("Salida:\n");
        txtTextArea.append("Aquí se muestran los mensajes:\n");
        txtMessage.setText("");
        txtMacDestiny.setText("");

        // Variables
        String SERVER_IP = "192.168.137.1";
        int SERVER_PORT = 6868;

        // Listeners
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MAC != null) {
                    String message = txtMessage.getText().toString();
                    String[] macDestiny = txtMacDestiny.getText().toString().split(":");
                    if(addressManager.isMacAdress(macDestiny)) {
                        // Create NetworkFrame
                        NetworkFrame frame = new NetworkFrame(message, addressManager.getMacAddress(MAC), addressManager.getMacAddress(macDestiny));

                        Client client = new Client(SERVER_IP, SERVER_PORT, frame);
                        client.run();


                        txtMessage.setText("");
                        txtTextArea.append(message + "\n");
                    } else {
                        // Invalid MAC destiny message
                        toastNotification("Dirección MAC de destino inválida");
                    }
                } else {
                    // Invalid MAC message
                    toastNotification("Dirección MAC no establecida");
                }
            }
        });

        btnSetMacAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MACAddressDialog dialog = new MACAddressDialog();
                dialog.show(getSupportFragmentManager(), "MAC Address");
                System.out.println("Se mostró el cuadro de diálogo");
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

    public void showMACDialog() {
        MACAddressDialog dialog = new MACAddressDialog();
        dialog.show(getSupportFragmentManager(), "MAC Address:");
    }

    @Override
    public void onDialogPositiveClick(MACAddressDialog dialog) {
        String[] macAux = dialog.getMacAddress().split(":");
        if(addressManager.isMacAdress(macAux)) {
            // set MAC address
           setMAC(macAux);
        } else {
            // Notify the invalid MAC address format
            toastNotification("Formato de dirección MAC inválido");
        }
    }

    private void setMAC(String[] macAddress) {
        String macAux = addressManager.getMacAddress(macAddress);
        MAC = macAddress;
        btnSetMacAddress.setText("MAC: " + macAux);
    }

    private void toastNotification(String message) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}