package com.example.quiz.server.repository;

import com.example.quiz.server.model.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class QuestionRepository {
    private QuestionRepository() {
    }

    public static List<Question> loadDefault() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                Objects.requireNonNull(QuestionRepository.class.getResourceAsStream("/questions.txt")),
                StandardCharsets.UTF_8))) {
            List<Question> questions = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("\\|");
                if (parts.length != 6) {
                    throw new IllegalArgumentException("Invalid question line: " + line);
                }
                questions.add(new Question(
                        parts[0],
                        List.of(parts[1], parts[2], parts[3], parts[4]),
                        Character.toUpperCase(parts[5].charAt(0))));
            }
            return questions;
        } catch (IOException exception) {
            throw new IllegalStateException("Could not load questions", exception);
        }
    }
}
