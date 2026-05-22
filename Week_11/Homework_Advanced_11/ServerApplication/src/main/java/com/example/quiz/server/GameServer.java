package com.example.quiz.server;

import com.example.quiz.server.game.QuizGame;
import com.example.quiz.server.model.Question;
import com.example.quiz.server.service.QuizPersistenceService;
import com.example.quiz.server.service.CachePerformanceService;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Path;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameServer {
    private final int port;
    private final ExecutorService clientPool;
    private final Set<ClientThread> clients = ConcurrentHashMap.newKeySet();
    private final QuizGame game;
    private volatile boolean running = true;
    private ServerSocket serverSocket;

    public GameServer(int port) {
        this(port, com.example.quiz.server.repository.QuestionRepository.loadDefault(), null);
    }

    public GameServer(int port, java.util.List<Question> questions, QuizPersistenceService persistenceService) {
        this.port = port;
        this.clientPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4);
        this.game = new QuizGame(questions, this::broadcast, persistenceService);
    }

    public void start() {
        try (ServerSocket socket = new ServerSocket(port)) {
            serverSocket = socket;
            System.out.println("Quiz server started on port " + port);
            System.out.println("Commands: join <name>, question, answer <A-D>, scores, bot random/custom/llm <name>, results, stop");
            while (running) {
                try {
                    Socket clientSocket = socket.accept();
                    ClientThread client = new ClientThread(clientSocket, this, game);
                    clients.add(client);
                    clientPool.submit(client);
                } catch (SocketException exception) {
                    if (running) {
                        throw exception;
                    }
                }
            }
        } catch (IOException exception) {
            System.err.println("Server error: " + exception.getMessage());
        } finally {
            shutdown();
        }
    }

    public void broadcast(String message) {
        clients.forEach(client -> client.send(message));
    }

    public void remove(ClientThread client) {
        clients.remove(client);
    }

    public void stop() {
        running = false;
        closeServerSocket();
        broadcast("SERVER Server stopped");
    }

    private void shutdown() {
        running = false;
        closeServerSocket();
        clients.forEach(ClientThread::close);
        game.shutdown();
        clientPool.shutdown();
        try {
            if (!clientPool.awaitTermination(3, TimeUnit.SECONDS)) {
                clientPool.shutdownNow();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            clientPool.shutdownNow();
        }
        System.out.println("Quiz server stopped gracefully.");
    }

    private void closeServerSocket() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException exception) {
                System.err.println("Could not close server socket: " + exception.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        if (args.length > 0 && "virtual-demo".equalsIgnoreCase(args[0])) {
            VirtualThreadScenario.run();
            return;
        }
        if (args.length > 0 && "cache-demo".equalsIgnoreCase(args[0])) {
            try (ConfigurableApplicationContext context = SpringApplication.run(QuizServerApplication.class, args)) {
                System.out.println(context.getBean(CachePerformanceService.class).compareQuestionReads());
            }
            return;
        }
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 5000;
        try (ConfigurableApplicationContext context = SpringApplication.run(QuizServerApplication.class, args)) {
            QuizPersistenceService persistenceService = context.getBean(QuizPersistenceService.class);
            new GameServer(port, persistenceService.loadQuestions(), persistenceService).start();
        }
    }
}
