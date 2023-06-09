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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import NetworkManagement.AddressManager;
import NetworkManagement.Client;
import NetworkManagement.MessageManager;
import NetworkManagement.NetworkFrame;
import UIManagement.MACAddressDialog;

public class MainActivity extends AppCompatActivity implements MACAddressDialog.MACAddressListener {

    AddressManager addressManager = new AddressManager();
    EditText txtMessage;
    EditText txtMacDestiny;
    FloatingActionButton btnSend;
    Button btnSetMacAddress;
    ToggleButton btnConnect;
    EditText txtTextArea;
    private String[] MAC;

    // Variables
    String SERVER_IP = "192.168.137.1";
    int SERVER_PORT = 6868;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewsById
        btnSend = (FloatingActionButton) findViewById(R.id.btnSend);
        btnSetMacAddress = (Button) findViewById(R.id.btnSetMacAddress);
        btnConnect = (ToggleButton) findViewById(R.id.btnConnect);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        txtTextArea = (EditText) findViewById(R.id.txtTextArea);
        txtMacDestiny = (EditText) findViewById(R.id.txtMacDestiny);

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

        btnConnect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    if(MAC != null && addressManager.isMacAdress(MAC)) {
                        btnSetMacAddress.setEnabled(false);
                        NetworkFrame connectDeviceFrame = new NetworkFrame(1, addressManager.getMacAddress(MAC));
                        Client connectMessage = new Client(SERVER_IP, SERVER_PORT, connectDeviceFrame);
                        connectMessage.run();

                    } else {
                        toastNotification("MAC Origen Inválida");
                        btnConnect.setChecked(false);
                    }
                } else {
                    btnSetMacAddress.setEnabled(true);
                    NetworkFrame disconnectDeviceFrame = new NetworkFrame(0, addressManager.getMacAddress(MAC));
                    Client disconnectDevice = new Client(SERVER_IP, SERVER_PORT, disconnectDeviceFrame);
                    disconnectDevice.run();
                }
            }
        });


        // Start MessageManager
        MessageManager messageManager = new MessageManager(txtTextArea);
        new Thread(conn).start();

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

    private ServerSocket serverSocket;

    Runnable conn = new Runnable() {
        @Override
        public void run() {
            System.out.println("Antes del XD");
            try {
                serverSocket = new ServerSocket(9090);
                System.out.println("Escuchando en 9090...");
                while(true) {
                    Socket socket = serverSocket.accept();


                    // get the input stream from the connected socket
                    InputStream input = socket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(input);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // get the object (Network Frame)
                            NetworkFrame frame = null;
                            try {
                                frame = (NetworkFrame) objectInputStream.readObject();
                                EditText txtArea = (EditText) findViewById(R.id.txtTextArea);

                                if (frame.getType() == 0) {     // Disconnect device response
                                    txtArea.append(frame.getMessage() + "\n\n");
                                } else if(frame.getType() == 1) {                   // Add device response
                                    txtArea.append(frame.getMessage() + "\n\n");
                                } else if(frame.getType() == 2) {
                                    txtArea.append(frame.getMessage() + "\n\n");
                                } else if (frame.getType() == 3) { // Messages response
                                    // Accept broadcast message
                                    if(frame.getMacDestiny().equals(addressManager.getMacAddress(MAC))) {
                                        txtTextArea.append(frame.renderMessage());

                                        // send response message
                                        NetworkFrame responseFrame = new NetworkFrame(4, frame.getMessage(),frame.getMacDestiny(), frame.getMacOrigin());
                                        Client clientResponse = new Client(SERVER_IP, SERVER_PORT, responseFrame);
                                        clientResponse.run();
                                    }
                                } else if(frame.getType() == 4) {
                                    txtArea.append("Yo:\n");
                                    txtArea.append(frame.getMessage() + "\n\n");
                                } else if(frame.getType() == 5) {                   // Cannot add device message
                                    ToggleButton btnConnect = (ToggleButton) findViewById(R.id.btnConnect);
                                    Button btnSetMacAddress = (Button) findViewById(R.id.btnSetMacAddress);

                                    // set initial properties
                                    btnConnect.setChecked(false);
                                    btnSetMacAddress.setEnabled(true);
                                    txtArea.append(frame.getMessage() + "\n");
                                } else if(frame.getType() == 6) {
                                    txtArea.append(frame.getMessage() + "\n");
                                }

                                socket.close();
                            } catch (ClassNotFoundException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    };

}