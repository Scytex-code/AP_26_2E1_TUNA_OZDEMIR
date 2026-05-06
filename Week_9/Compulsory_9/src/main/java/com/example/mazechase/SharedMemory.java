package com.example.mazechase;

public class SharedMemory {
    private Position lastKnownBunnyPosition;

    public synchronized void reportBunny(Position position) {
        lastKnownBunnyPosition = position;
        notifyAll();
    }

    public synchronized Position getLastKnownBunnyPosition() {
        return lastKnownBunnyPosition;
    }
}
