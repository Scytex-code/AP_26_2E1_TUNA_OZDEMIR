package com.example.mazechase.actor;

import com.example.mazechase.GameState;
import com.example.mazechase.SharedMemory;
import com.example.mazechase.model.Position;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Robot extends Creature {
    private static final int SENSOR_DISTANCE = 2;

    public Robot(String name, GameState gameState, SharedMemory sharedMemory, ActorControl control) {
        super(name, gameState, sharedMemory, control);
    }

    @Override
    protected void place() {
        gameState.placeRobot(name, control);
    }

    @Override
    protected void act() {
        gameState.positionOfRobot(name).ifPresent(current -> {
            senseBunnies(current);
            Position next = chooseNextPosition(current);
            if (next != null) {
                gameState.moveRobot(name, next);
            }
        });
    }

    private void senseBunnies(Position current) {
        for (Map.Entry<String, Position> entry : gameState.bunniesSnapshot().entrySet()) {
            if (current.manhattanDistance(entry.getValue()) <= SENSOR_DISTANCE) {
                sharedMemory.reportBunny(entry.getKey(), entry.getValue());
            }
        }
    }

    private Position chooseNextPosition(Position current) {
        Optional<Position> knownBunny = sharedMemory.nearestKnownBunny(current);
        if (knownBunny.isPresent()) {
            List<Position> path = gameState.maze().shortestPath(current, knownBunny.get());
            if (path.size() > 1) {
                return path.get(1);
            }
        }

        List<Position> neighbors = gameState.maze().neighbors(current);
        return neighbors.stream()
                .filter(position -> !sharedMemory.hasBeenVisited(position))
                .findFirst()
                .orElseGet(() -> neighbors.stream()
                        .min(Comparator.comparingInt(position -> position.row() + position.col()))
                        .orElse(null));
    }
}
