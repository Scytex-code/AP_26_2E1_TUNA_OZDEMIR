package com.example.quiz.server.bot;

import com.example.quiz.server.model.Question;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class LlmBot implements BotPlayer {
    private final String difficulty;
    private final String name;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public LlmBot(String difficulty, String name) {
        this.difficulty = difficulty;
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String answer(Question question) {
        String apiAnswer = askExternalApi(question);
        if (apiAnswer != null) {
            return apiAnswer;
        }
        if ("hard".equalsIgnoreCase(difficulty)) {
            return String.valueOf(question.correctAnswer());
        }
        if ("medium".equalsIgnoreCase(difficulty) && question.text().toLowerCase().contains("java")) {
            return String.valueOf(question.correctAnswer());
        }
        return "A";
    }

    private String askExternalApi(Question question) {
        String endpoint = System.getenv("LLM_API_URL");
        if (endpoint == null || endpoint.isBlank()) {
            return null;
        }
        String token = System.getenv("LLM_API_KEY");
        String prompt = "Return only A, B, C or D. Difficulty=" + difficulty + ". " + question.format();
        HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(endpoint))
                .timeout(Duration.ofSeconds(4))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(prompt));
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }
        try {
            HttpResponse<String> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300 && !response.body().isBlank()) {
                return response.body().trim().substring(0, 1).toUpperCase();
            }
        } catch (IOException | InterruptedException exception) {
            if (exception instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }
        return null;
    }
}
