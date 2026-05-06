package com.example.mazechase.actor;

import com.example.mazechase.GameState;
import com.example.mazechase.SharedMemory;
import com.example.mazechase.model.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bunny extends Creature {
    private final Random random = new Random();

    public Bunny(String name, GameState gameState, SharedMemory sharedMemory, ActorControl control) {
        super(name, gameState, sharedMemory, control);
    }

    @Override
    protected void place() {
        gameState.placeBunny(name, control);
    }

    @Override
    protected void act() {
        gameState.positionOfBunny(name).ifPresent(current -> {
            List<Position> candidates = new ArrayList<>(gameState.maze().neighbors(current));
            if (candidates.isEmpty()) {
                return;
            }
            Position next = candidates.get(random.nextInt(candidates.size()));
            gameState.moveBunny(name, next);
        });
    }
}
