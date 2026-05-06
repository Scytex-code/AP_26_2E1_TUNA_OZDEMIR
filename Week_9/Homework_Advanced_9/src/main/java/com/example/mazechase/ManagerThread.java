package com.example.mazechase;

import java.time.Duration;
import java.time.Instant;

public class ManagerThread extends Thread {
    private final GameState gameState;
    private final SharedMemory sharedMemory;
    private final Instant startedAt = Instant.now();
    private final long timeLimitSeconds;

    public ManagerThread(GameState gameState, SharedMemory sharedMemory, long timeLimitSeconds) {
        super("Manager");
        this.gameState = gameState;
        this.sharedMemory = sharedMemory;
        this.timeLimitSeconds = timeLimitSeconds;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (!gameState.isFinished()) {
            long elapsed = Duration.between(startedAt, Instant.now()).toSeconds();
            System.out.println("Time: " + elapsed + "s | Shared visited cells: "
                    + sharedMemory.visitedCount() + " | Known bunnies: "
                    + sharedMemory.knownBunniesSnapshot());
            System.out.println(gameState.render());
            if (elapsed >= timeLimitSeconds) {
                gameState.finish("Time limit exceeded after " + timeLimitSeconds + " seconds.");
                break;
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println(gameState.finishMessage());
    }
}
