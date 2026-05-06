package com.example.mazechase;

public class Bunny extends Creature {
    public Bunny(GameState gameState, SharedMemory sharedMemory) {
        super("Bunny", gameState, sharedMemory, 350);
    }

    @Override
    protected void act() {
        Position position = gameState.moveRandomly(name);
        sharedMemory.reportBunny(position);
    }
}
