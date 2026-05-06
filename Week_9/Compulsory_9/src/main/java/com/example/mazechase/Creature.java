package com.example.mazechase;

public abstract class Creature implements Runnable {
    protected final String name;
    protected final GameState gameState;
    protected final SharedMemory sharedMemory;
    private final long delayMillis;

    protected Creature(String name, GameState gameState, SharedMemory sharedMemory, long delayMillis) {
        this.name = name;
        this.gameState = gameState;
        this.sharedMemory = sharedMemory;
        this.delayMillis = delayMillis;
    }

    @Override
    public void run() {
        gameState.place(name);
        while (!gameState.isFinished()) {
            act();
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    protected abstract void act();
}
