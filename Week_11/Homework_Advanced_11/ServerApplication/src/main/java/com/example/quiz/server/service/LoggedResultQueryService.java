package com.example.quiz.server.service;

import com.example.quiz.server.entity.ResultEntity;
import com.example.quiz.server.repository.ResultRepository;
import com.example.quiz.server.search.ResultSearchCriteria;
import com.example.quiz.server.search.ResultSpecifications;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.jpa.HibernateHints;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class LoggedResultQueryService {
    private static final Logger LOGGER = Logger.getLogger(LoggedResultQueryService.class.getName());

    static {
        try {
            Files.createDirectories(Path.of("logs"));
            LOGGER.setUseParentHandlers(false);
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(consoleHandler);
            FileHandler fileHandler = new FileHandler("logs/jpql-statements.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
        } catch (IOException exception) {
            LOGGER.log(Level.WARNING, "Could not configure JPQL file logger", exception);
        }
    }

    private final ResultRepository resultRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public LoggedResultQueryService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    @Transactional(readOnly = true)
    public List<ResultEntity> topResultsForPlayerPrefix(String prefix) {
        String jpql = """
                select r
                from ResultEntity r
                join fetch r.player p
                join fetch r.game g
                where lower(p.name) like lower(concat(:prefix, '%'))
                order by r.score desc, r.totalResponseTimeMillis asc
                """;
        long started = System.nanoTime();
        try {
            List<ResultEntity> results = entityManager.createQuery(jpql, ResultEntity.class)
                    .setParameter("prefix", prefix)
                    .setHint(HibernateHints.HINT_CACHEABLE, true)
                    .getResultList();
            logSuccess(jpql, started, results.size());
            return results;
        } catch (RuntimeException exception) {
            logFailure(jpql, started, exception);
            throw exception;
        }
    }

    @Transactional
    public int addBonusToWeakResults(int threshold, int bonus) {
        String jpql = "update ResultEntity r set r.score = r.score + :bonus where r.score < :threshold";
        long started = System.nanoTime();
        try {
            int updated = resultRepository.addBonusToScoresBelow(threshold, bonus);
            logSuccess(jpql, started, updated);
            return updated;
        } catch (RuntimeException exception) {
            logFailure(jpql, started, exception);
            throw exception;
        }
    }

    @Transactional(readOnly = true)
    public List<ResultEntity> search(ResultSearchCriteria criteria) {
        long started = System.nanoTime();
        try {
            List<ResultEntity> results = resultRepository.findAll(ResultSpecifications.matching(criteria));
            LOGGER.info(() -> "Criteria search took " + elapsedMillis(started) + " ms and returned " + results.size() + " rows");
            return results;
        } catch (RuntimeException exception) {
            LOGGER.log(Level.SEVERE, "Criteria search failed after " + elapsedMillis(started) + " ms", exception);
            throw exception;
        }
    }

    private void logSuccess(String jpql, long started, int rows) {
        LOGGER.info(() -> "JPQL succeeded in " + elapsedMillis(started) + " ms, rows=" + rows + ", query=[" + jpql + "]");
    }

    private void logFailure(String jpql, long started, RuntimeException exception) {
        LOGGER.log(Level.SEVERE, "JPQL failed after " + elapsedMillis(started) + " ms, query=[" + jpql + "]", exception);
    }

    private long elapsedMillis(long started) {
        return (System.nanoTime() - started) / 1_000_000;
    }
}
