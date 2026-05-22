package com.example.quiz.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class GameClient {
    private final String host;
    private final int port;

    public GameClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        CountDownLatch done = new CountDownLatch(1);
        try (Socket socket = new Socket(host, port);
             BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader serverInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter serverOutput = new PrintWriter(socket.getOutputStream(), true)) {
            Thread listener = new Thread(() -> listen(serverInput, done), "server-listener");
            Thread sender = new Thread(() -> sendCommands(keyboard, serverOutput, done), "keyboard-sender");
            listener.start();
            sender.start();
            done.await();
            socket.close();
        } catch (IOException exception) {
            System.err.println("Client error: " + exception.getMessage());
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private void listen(BufferedReader serverInput, CountDownLatch done) {
        try {
            String message;
            while ((message = serverInput.readLine()) != null) {
                System.out.println(message);
            }
        } catch (IOException exception) {
            System.err.println("Listener stopped: " + exception.getMessage());
        } finally {
            done.countDown();
        }
    }

    private void sendCommands(BufferedReader keyboard, PrintWriter serverOutput, CountDownLatch done) {
        System.out.println("Commands: join <name>, question, answer <A-D>, scores, results, bot random/custom <name>, bot llm hard <name>, exit");
        try {
            String command;
            while ((command = keyboard.readLine()) != null) {
                if ("exit".equalsIgnoreCase(command.trim())) {
                    done.countDown();
                    break;
                }
                serverOutput.println(command);
                if ("stop".equalsIgnoreCase(command.trim())) {
                    done.countDown();
                    break;
                }
            }
        } catch (IOException exception) {
            System.err.println("Input stopped: " + exception.getMessage());
        }
    }

    public static void main(String[] args) {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;
        new GameClient(host, port).start();
    }
}
