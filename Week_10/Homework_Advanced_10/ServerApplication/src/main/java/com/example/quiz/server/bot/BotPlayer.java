package com.example.quiz.server.bot;

import com.example.quiz.server.model.Question;

public interface BotPlayer {
    String name();

    String answer(Question question);

    default void learn(Question question, String submittedAnswer, boolean correct) {
    }
}
