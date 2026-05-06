package com.example.mazechase;

public class Robot extends Creature {
    public Robot(String name, GameState gameState, SharedMemory sharedMemory) {
        super(name, gameState, sharedMemory, 450);
    }

    @Override
    protected void act() {
        gameState.moveRandomly(name);
        gameState.positionOf("Bunny").ifPresent(sharedMemory::reportBunny);
    }
}
