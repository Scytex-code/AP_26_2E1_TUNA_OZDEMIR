package com.example.quiz.server;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class VirtualThreadScenario {
    private VirtualThreadScenario() {
    }

    public static void run() {
        int clientCount = 10_000;
        System.out.println("Scenario: one short-lived simulated client per thread, clients=" + clientCount);
        runPlatformThreadScenario(clientCount);
        runVirtualThreadScenario(clientCount);
    }

    private static void runPlatformThreadScenario(int clientCount) {
        Instant start = Instant.now();
        List<Thread> threads = new ArrayList<>();
        int created = 0;
        try {
            for (int i = 0; i < clientCount; i++) {
                Thread thread = new Thread(VirtualThreadScenario::simulatedClient);
                thread.start();
                threads.add(thread);
                created++;
            }
            joinAll(threads);
        } catch (OutOfMemoryError error) {
            System.out.println("Platform threads reached a system limit after " + created + " clients.");
        }
        System.out.println("Platform thread time: " + Duration.between(start, Instant.now()).toMillis() + " ms");
    }

    private static void runVirtualThreadScenario(int clientCount) {
        try {
            Method startVirtualThread = Thread.class.getMethod("startVirtualThread", Runnable.class);
            Instant start = Instant.now();
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < clientCount; i++) {
                threads.add((Thread) startVirtualThread.invoke(null, (Runnable) VirtualThreadScenario::simulatedClient));
            }
            joinAll(threads);
            System.out.println("Virtual thread time: " + Duration.between(start, Instant.now()).toMillis() + " ms");
        } catch (NoSuchMethodException exception) {
            System.out.println("Virtual threads require Java 21+. Run this scenario with JDK 21 to compare results.");
        } catch (ReflectiveOperationException exception) {
            System.out.println("Could not start virtual thread scenario: " + exception.getMessage());
        }
    }

    private static void simulatedClient() {
        try {
            Thread.sleep(20);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private static void joinAll(List<Thread> threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
