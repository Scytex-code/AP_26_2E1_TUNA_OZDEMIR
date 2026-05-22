package com.example.quiz.server.service;

import org.springframework.stereotype.Service;

@Service
public class CachePerformanceService {
    private final QuizPersistenceService persistenceService;

    public CachePerformanceService(QuizPersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public String compareQuestionReads() {
        long coldStarted = System.nanoTime();
        int coldRows = persistenceService.loadQuestions().size();
        long coldMillis = elapsedMillis(coldStarted);

        long warmStarted = System.nanoTime();
        int warmRows = persistenceService.loadQuestions().size();
        long warmMillis = elapsedMillis(warmStarted);

        return "CACHE coldReadMs=" + coldMillis + " warmReadMs=" + warmMillis
                + " rows=" + coldRows + "/" + warmRows;
    }

    private long elapsedMillis(long started) {
        return (System.nanoTime() - started) / 1_000_000;
    }
}
