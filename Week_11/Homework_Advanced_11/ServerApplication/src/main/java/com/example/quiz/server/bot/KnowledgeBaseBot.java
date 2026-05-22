package com.example.quiz.server.bot;

import com.example.quiz.server.model.Question;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KnowledgeBaseBot implements BotPlayer {
    private final String name;
    private final Map<String, String> knowledgeBase = new ConcurrentHashMap<>();

    public KnowledgeBaseBot(String name) {
        this.name = name;
        knowledgeBase.put("Which Java class accepts TCP connections?", "B");
        knowledgeBase.put("Which protocol guarantees ordered byte streams?", "B");
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String answer(Question question) {
        return knowledgeBase.getOrDefault(question.text(), "A");
    }

    @Override
    public void learn(Question question, String submittedAnswer, boolean correct) {
        if (!correct) {
            knowledgeBase.put(question.text(), String.valueOf(question.correctAnswer()));
        }
    }
}
