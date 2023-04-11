package org.example;

import NetworkManagement.SwitchServer;
import UI.ServerAppUI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ServerAppUI v1 = new ServerAppUI();
            SwitchServer server = new SwitchServer(6868, v1);
            v1.setVisible(true);
            server.startServer();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNF Error: " + e.getMessage());
        }
    }
}