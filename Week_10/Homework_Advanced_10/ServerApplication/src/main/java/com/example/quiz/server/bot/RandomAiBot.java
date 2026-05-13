package com.example.quiz.server.bot;

import com.example.quiz.server.model.Question;

import java.util.Random;

public class RandomAiBot implements BotPlayer {
    private final String name;
    private final Random random = new Random();

    public RandomAiBot(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String answer(Question question) {
        return String.valueOf((char) ('A' + random.nextInt(4)));
    }
}
