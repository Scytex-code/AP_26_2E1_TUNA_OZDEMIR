package com.example.mazechase.actor;

public class ActorControl {
    private int delayMillis;
    private boolean paused;
    private boolean stopped;

    public ActorControl(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public synchronized void waitIfPaused() throws InterruptedException {
        while (paused && !stopped) {
            wait();
        }
    }

    public synchronized void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notifyAll();
    }

    public synchronized void stop() {
        stopped = true;
        paused = false;
        notifyAll();
    }

    public synchronized void setDelayMillis(int delayMillis) {
        this.delayMillis = Math.max(40, delayMillis);
    }

    public synchronized int delayMillis() {
        return delayMillis;
    }

    public synchronized boolean isStopped() {
        return stopped;
    }
}
