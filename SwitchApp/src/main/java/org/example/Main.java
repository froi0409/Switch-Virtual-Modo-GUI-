package org.example;

import NetworkManagement.ServerPrueba;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ServerPrueba server = new ServerPrueba(6868);
            server.startServer();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}