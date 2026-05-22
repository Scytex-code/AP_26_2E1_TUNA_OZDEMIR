package com.example.quiz.server.model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Player {
    private final String name;
    private final boolean bot;
    private final AtomicInteger score = new AtomicInteger();
    private final AtomicLong totalResponseTimeMillis = new AtomicLong();

    public Player(String name, boolean bot) {
        this.name = name;
        this.bot = bot;
    }

    public String getName() {
        return name;
    }

    public boolean isBot() {
        return bot;
    }

    public int getScore() {
        return score.get();
    }

    public long getTotalResponseTimeMillis() {
        return totalResponseTimeMillis.get();
    }

    public void recordAnswer(boolean correct, long responseTimeMillis) {
        if (correct) {
            score.incrementAndGet();
            totalResponseTimeMillis.addAndGet(responseTimeMillis);
        }
    }
}
