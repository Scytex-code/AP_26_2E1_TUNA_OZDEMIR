package com.example.mazechase;

import com.example.mazechase.actor.ActorControl;
import com.example.mazechase.model.Maze;
import com.example.mazechase.model.Position;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GameState {
    private final Maze maze;
    private final SharedMemory sharedMemory;
    private final Map<String, Position> robots = new LinkedHashMap<>();
    private final Map<String, Position> bunnies = new LinkedHashMap<>();
    private final Map<String, ActorControl> controls = new HashMap<>();
    private final Random random = new Random();
    private boolean finished;
    private String finishMessage = "Game is running.";

    public GameState(Maze maze, SharedMemory sharedMemory) {
        this.maze = maze;
        this.sharedMemory = sharedMemory;
    }

    public synchronized Position placeRobot(String name, ActorControl control) {
        Position position = randomFreePosition();
        robots.put(name, position);
        controls.put(name, control);
        sharedMemory.markVisited(position);
        return position;
    }

    public synchronized Position placeBunny(String name, ActorControl control) {
        Position position = randomFreePosition();
        bunnies.put(name, position);
        controls.put(name, control);
        return position;
    }

    private Position randomFreePosition() {
        Position position;
        do {
            position = new Position(random.nextInt(maze.rows()), random.nextInt(maze.cols()));
        } while (isOccupied(position) || position.equals(maze.exit()));
        return position;
    }

    public synchronized boolean moveRobot(String name, Position target) {
        if (finished || !robots.containsKey(name) || target == null || robotBlocks(name, target)) {
            return false;
        }
        robots.put(name, target);
        sharedMemory.markVisited(target);
        Optional<String> caught = bunnyAt(target);
        caught.ifPresent(bunny -> {
            bunnies.remove(bunny);
            sharedMemory.forgetBunny(bunny);
            if (bunnies.isEmpty()) {
                finish(name + " caught the last bunny at " + target + ".");
            }
        });
        return true;
    }

    public synchronized boolean moveBunny(String name, Position target) {
        if (finished || !bunnies.containsKey(name) || target == null || isOccupiedByRobot(target)) {
            if (!finished && isOccupiedByRobot(target)) {
                String robotName = robotAt(target).orElse("A robot");
                bunnies.remove(name);
                sharedMemory.forgetBunny(name);
                if (bunnies.isEmpty()) {
                    finish(robotName + " caught the last bunny.");
                }
            }
            return false;
        }
        bunnies.put(name, target);
        if (target.equals(maze.exit())) {
            bunnies.remove(name);
            sharedMemory.forgetBunny(name);
            if (bunnies.isEmpty()) {
                finish(name + " escaped through the exit and no bunnies remain.");
            }
        }
        return true;
    }

    public synchronized void finish(String message) {
        if (!finished) {
            finished = true;
            finishMessage = message;
            controls.values().forEach(ActorControl::stop);
            notifyAll();
        }
    }

    private boolean robotBlocks(String mover, Position target) {
        return robots.entrySet().stream()
                .anyMatch(entry -> !entry.getKey().equals(mover) && entry.getValue().equals(target));
    }

    private boolean isOccupied(Position position) {
        return isOccupiedByRobot(position) || bunnies.containsValue(position);
    }

    private boolean isOccupiedByRobot(Position position) {
        return robots.containsValue(position);
    }

    private Optional<String> robotAt(Position position) {
        return robots.entrySet().stream()
                .filter(entry -> entry.getValue().equals(position))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    private Optional<String> bunnyAt(Position position) {
        return bunnies.entrySet().stream()
                .filter(entry -> entry.getValue().equals(position))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    public synchronized Optional<Position> positionOfRobot(String name) {
        return Optional.ofNullable(robots.get(name));
    }

    public synchronized Optional<Position> positionOfBunny(String name) {
        return Optional.ofNullable(bunnies.get(name));
    }

    public synchronized Map<String, Position> robotsSnapshot() {
        return new LinkedHashMap<>(robots);
    }

    public synchronized Map<String, Position> bunniesSnapshot() {
        return new LinkedHashMap<>(bunnies);
    }

    public synchronized Map<String, ActorControl> controlsSnapshot() {
        return new HashMap<>(controls);
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized String finishMessage() {
        return finishMessage;
    }

    public Maze maze() {
        return maze;
    }

    public synchronized String render() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < maze.rows(); row++) {
            for (int col = 0; col < maze.cols(); col++) {
                Position position = new Position(row, col);
                String symbol = ".";
                if (position.equals(maze.exit())) {
                    symbol = "E";
                }
                if (bunnies.containsValue(position)) {
                    symbol = "B";
                }
                if (robots.containsValue(position)) {
                    symbol = "R";
                }
                builder.append(symbol).append(' ');
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
