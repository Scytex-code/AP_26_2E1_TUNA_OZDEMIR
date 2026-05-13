package com.example.quiz.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private final Socket socket;
    private final GameServer server;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (Socket client = socket;
             BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter output = new PrintWriter(client.getOutputStream(), true)) {
            String request;
            while ((request = input.readLine()) != null) {
                if ("stop".equalsIgnoreCase(request.trim())) {
                    output.println("Server stopped");
                    server.stop();
                    break;
                }
                output.println("Server received the request " + request);
            }
        } catch (IOException exception) {
            System.err.println("Client communication error: " + exception.getMessage());
        }
    }
}
