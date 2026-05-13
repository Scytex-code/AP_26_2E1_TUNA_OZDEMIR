package com.example.quiz.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class GameServer {
    private final int port;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public GameServer(int port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket socket = new ServerSocket(port)) {
            serverSocket = socket;
            System.out.println("Quiz server started on port " + port);
            while (running) {
                try {
                    Socket clientSocket = socket.accept();
                    new ClientThread(clientSocket, this).start();
                } catch (SocketException exception) {
                    if (running) {
                        throw exception;
                    }
                }
            }
        } catch (IOException exception) {
            System.err.println("Server error: " + exception.getMessage());
        } finally {
            running = false;
            System.out.println("Quiz server stopped.");
        }
    }

    public void stop() {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException exception) {
                System.err.println("Could not close server socket: " + exception.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        new GameServer(port).start();
    }
}
