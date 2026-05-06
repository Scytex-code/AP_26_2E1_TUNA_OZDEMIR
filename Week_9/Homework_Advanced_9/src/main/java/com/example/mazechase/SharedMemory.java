package com.example.mazechase;

import com.example.mazechase.model.Position;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class SharedMemory {
    private final Set<Position> visitedByAnyRobot = new HashSet<>();
    private final Map<String, Position> knownBunnies = new HashMap<>();

    public synchronized void markVisited(Position position) {
        visitedByAnyRobot.add(position);
        notifyAll();
    }

    public synchronized boolean hasBeenVisited(Position position) {
        return visitedByAnyRobot.contains(position);
    }

    public synchronized int visitedCount() {
        return visitedByAnyRobot.size();
    }

    public synchronized void reportBunny(String bunnyName, Position position) {
        knownBunnies.put(bunnyName, position);
        notifyAll();
    }

    public synchronized void forgetBunny(String bunnyName) {
        knownBunnies.remove(bunnyName);
    }

    public synchronized Optional<Position> nearestKnownBunny(Position from) {
        return knownBunnies.values().stream()
                .min((first, second) -> Integer.compare(from.manhattanDistance(first), from.manhattanDistance(second)));
    }

    public synchronized Map<String, Position> knownBunniesSnapshot() {
        return new HashMap<>(knownBunnies);
    }
}
