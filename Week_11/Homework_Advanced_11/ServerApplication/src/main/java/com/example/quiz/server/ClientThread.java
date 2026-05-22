package com.example.quiz.server;

import com.example.quiz.server.game.QuizGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread implements Runnable {
    private final Socket socket;
    private final GameServer server;
    private final QuizGame game;
    private PrintWriter output;
    private String playerName;

    public ClientThread(Socket socket, GameServer server, QuizGame game) {
        this.socket = socket;
        this.server = server;
        this.game = game;
    }

    @Override
    public void run() {
        try (Socket client = socket;
             BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {
            output = writer;
            send("SERVER Welcome. Type help for commands.");
            String command;
            while ((command = input.readLine()) != null) {
                String response = handle(command.trim());
                if (response != null && !response.isBlank()) {
                    send(response);
                }
                if ("stop".equalsIgnoreCase(command.trim())) {
                    break;
                }
            }
        } catch (IOException exception) {
            send("SERVER Client disconnected.");
        } finally {
            if (playerName != null) {
                game.leave(playerName);
            }
            server.remove(this);
        }
    }

    private String handle(String command) {
        if (command.isBlank()) {
            return "ERROR Empty command";
        }
        String[] parts = command.split("\\s+", 3);
        return switch (parts[0].toLowerCase()) {
            case "help" -> "SERVER Commands: join <name>, question, answer <A-D>, scores, results, bot random/custom/llm <name>, stop";
            case "join" -> join(parts);
            case "question" -> game.nextQuestion();
            case "answer" -> answer(parts);
            case "scores" -> game.scoreboard();
            case "results" -> game.results();
            case "bot" -> addBot(parts);
            case "stop" -> {
                server.stop();
                yield "Server stopped";
            }
            default -> "ERROR Unknown command: " + command;
        };
    }

    private String join(String[] parts) {
        if (parts.length < 2) {
            return "ERROR Usage: join <name>";
        }
        playerName = parts[1];
        return game.join(playerName);
    }

    private String answer(String[] parts) {
        if (playerName == null) {
            return "ERROR Join first with: join <name>";
        }
        if (parts.length < 2) {
            return "ERROR Usage: answer <A-D>";
        }
        return game.answer(playerName, parts[1]);
    }

    private String addBot(String[] parts) {
        String[] tokens = String.join(" ", parts).split("\\s+");
        if (tokens.length < 3) {
            return "ERROR Usage: bot random <name> | bot custom <name> | bot llm <easy|medium|hard> <name>";
        }
        if ("llm".equalsIgnoreCase(tokens[1])) {
            if (tokens.length < 4) {
                return "ERROR Usage: bot llm <easy|medium|hard> <name>";
            }
            return game.addBot(tokens[1], tokens[2], tokens[3]);
        }
        return game.addBot(tokens[1], tokens[2], "");
    }

    public void send(String message) {
        PrintWriter writer = output;
        if (writer != null) {
            writer.println(message);
        }
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }
}
