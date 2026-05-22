package com.example.quiz.server.model;

import java.io.Serializable;
import java.util.List;

public record Question(String text, List<String> options, char correctAnswer) implements Serializable {
    public boolean isCorrect(String answer) {
        return !answer.isBlank() && Character.toUpperCase(answer.charAt(0)) == correctAnswer;
    }

    public String format() {
        return "QUESTION " + text
                + " A) " + options.get(0)
                + " B) " + options.get(1)
                + " C) " + options.get(2)
                + " D) " + options.get(3);
    }
}
