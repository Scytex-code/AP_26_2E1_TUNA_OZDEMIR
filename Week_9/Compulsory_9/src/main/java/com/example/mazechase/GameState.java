package com.example.mazechase;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class GameState {
    private final Maze maze;
    private final Position exit;
    private final Map<String, Position> positions = new HashMap<>();
    private final Random random = new Random();
    private boolean finished;
    private String finishMessage = "Game is still running.";

    public GameState(Maze maze) {
        this.maze = maze;
        this.exit = new Position(maze.rows() - 1, maze.cols() - 1);
    }

    public synchronized Position place(String name) {
        Position position;
        do {
            position = new Position(random.nextInt(maze.rows()), random.nextInt(maze.cols()));
        } while (positions.containsValue(position) || position.equals(exit));
        positions.put(name, position);
        return position;
    }

    public synchronized Position moveRandomly(String name) {
        if (finished) {
            return positions.get(name);
        }
        Position current = positions.get(name);
        var options = maze.neighbors(current).stream()
                .filter(position -> canEnter(name, position))
                .toList();
        if (options.isEmpty()) {
            return current;
        }
        Position next = options.get(random.nextInt(options.size()));
        positions.put(name, next);
        checkFinish(name, next);
        return next;
    }

    private boolean canEnter(String name, Position position) {
        return positions.entrySet().stream()
                .noneMatch(entry -> blocks(name, entry.getKey(), entry.getValue(), position));
    }

    private boolean blocks(String mover, String occupant, Position occupied, Position target) {
        if (!occupied.equals(target) || occupant.equals(mover)) {
            return false;
        }
        return occupant.startsWith("Robot") || mover.equals("Bunny");
    }

    private void checkFinish(String name, Position position) {
        if (name.equals("Bunny") && position.equals(exit)) {
            finished = true;
            finishMessage = "Bunny escaped through the exit.";
            return;
        }
        Optional<Position> bunny = positionOf("Bunny");
        if (name.startsWith("Robot") && bunny.isPresent() && bunny.get().equals(position)) {
            finished = true;
            finishMessage = name + " caught the bunny.";
        }
    }

    public synchronized Optional<Position> positionOf(String name) {
        return Optional.ofNullable(positions.get(name));
    }

    public synchronized boolean isFinished() {
        return finished;
    }

    public synchronized String finishMessage() {
        return finishMessage;
    }

    public synchronized String render() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < maze.rows(); row++) {
            for (int col = 0; col < maze.cols(); col++) {
                Position p = new Position(row, col);
                if (p.equals(exit)) {
                    builder.append('E');
                } else if (p.equals(positions.get("Bunny"))) {
                    builder.append('B');
                } else {
                    String robot = positions.entrySet().stream()
                            .filter(entry -> entry.getKey().startsWith("Robot") && entry.getValue().equals(p))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);
                    builder.append(robot == null ? '.' : 'R');
                }
                builder.append(' ');
            }
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
