package com.example.mazechase.actor;

import com.example.mazechase.GameState;
import com.example.mazechase.SharedMemory;

public abstract class Creature implements Runnable {
    protected final String name;
    protected final GameState gameState;
    protected final SharedMemory sharedMemory;
    protected final ActorControl control;

    protected Creature(String name, GameState gameState, SharedMemory sharedMemory, ActorControl control) {
        this.name = name;
        this.gameState = gameState;
        this.sharedMemory = sharedMemory;
        this.control = control;
    }

    @Override
    public final void run() {
        place();
        while (!gameState.isFinished() && !control.isStopped()) {
            try {
                control.waitIfPaused();
                if (gameState.isFinished() || control.isStopped()) {
                    break;
                }
                act();
                Thread.sleep(control.delayMillis());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    protected abstract void place();

    protected abstract void act();
}
