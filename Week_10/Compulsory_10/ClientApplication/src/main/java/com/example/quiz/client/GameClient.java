package com.example.quiz.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class GameClient {
    private final String host;
    private final int port;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (Socket socket = new Socket(host, port);
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)) {
            System.out.println("Connected to quiz server. Type commands, or exit to close the client.");
            String command;
            while ((command = keyboard.readLine()) != null) {
                if ("exit".equalsIgnoreCase(command.trim())) {
                    break;
                }
                serverOutput.println(command);
                System.out.println(serverInput.readLine());
            }
        } catch (IOException exception) {
            System.err.println("Client error: " + exception.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;
        new GameClient(host, port).start();
    }
}
