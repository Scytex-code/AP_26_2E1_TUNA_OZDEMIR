package com.example.quiz.server.search;

public record ResultSearchCriteria(
        String playerNameStartsWith,
        Integer minimumScore,
        String gameProperty,
        Long maximumResponseTimeMillis) {
}
