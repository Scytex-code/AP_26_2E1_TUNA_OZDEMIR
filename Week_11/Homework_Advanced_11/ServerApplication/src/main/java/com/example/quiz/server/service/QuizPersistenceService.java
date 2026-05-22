package com.example.quiz.server.service;

import com.example.quiz.server.entity.PlayerEntity;
import com.example.quiz.server.entity.QuestionEntity;
import com.example.quiz.server.entity.QuizGameEntity;
import com.example.quiz.server.entity.ResultEntity;
import com.example.quiz.server.model.Player;
import com.example.quiz.server.model.Question;
import com.example.quiz.server.repository.PlayerRepository;
import com.example.quiz.server.repository.QuestionJpaRepository;
import com.example.quiz.server.repository.QuestionRepository;
import com.example.quiz.server.repository.QuizGameRepository;
import com.example.quiz.server.repository.ResultRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class QuizPersistenceService implements CommandLineRunner {
    private final QuestionJpaRepository questionRepository;
    private final PlayerRepository playerRepository;
    private final QuizGameRepository gameRepository;
    private final ResultRepository resultRepository;
    private final AuditService auditService;

    public QuizPersistenceService(QuestionJpaRepository questionRepository,
                                  PlayerRepository playerRepository,
                                  QuizGameRepository gameRepository,
                                  ResultRepository resultRepository,
                                  AuditService auditService) {
        this.questionRepository = questionRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.resultRepository = resultRepository;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (questionRepository.count() == 0) {
            QuestionRepository.loadDefault().forEach(question -> questionRepository.save(new QuestionEntity(
                    question.text(),
                    question.options().get(0),
                    question.options().get(1),
                    question.options().get(2),
                    question.options().get(3),
                    question.correctAnswer())));
        }
    }

    @Cacheable("questions")
    @Transactional(readOnly = true)
    public List<Question> loadQuestions() {
        return questionRepository.findAll().stream()
                .map(entity -> new Question(entity.getText(), List.of(
                        entity.getOptionA(),
                        entity.getOptionB(),
                        entity.getOptionC(),
                        entity.getOptionD()), entity.getCorrectAnswer()))
                .toList();
    }

    @Transactional
    public Long createGame() {
        QuizGameEntity game = gameRepository.save(new QuizGameEntity());
        auditService.record("CREATE", "QuizGameEntity", game.getId(), "Game started");
        return game.getId();
    }

    @Transactional
    public void registerPlayer(Long gameId, String name, boolean bot) {
        PlayerEntity player = playerRepository.findByName(name)
                .map(existing -> {
                    if (bot && !existing.isBot()) {
                        existing.setBot(true);
                    }
                    return existing;
                })
                .orElseGet(() -> playerRepository.save(new PlayerEntity(name, bot)));
        QuizGameEntity game = gameRepository.findById(gameId).orElseThrow();
        game.addPlayer(player);
        auditService.record("UPSERT", "PlayerEntity", player.getId(), name + " joined game " + gameId);
    }

    @Transactional
    public void saveResults(Long gameId, Collection<Player> players) {
        QuizGameEntity game = gameRepository.findById(gameId).orElseThrow();
        for (Player player : players) {
            PlayerEntity persistedPlayer = playerRepository.findByName(player.getName()).orElseThrow();
            ResultEntity result = resultRepository.save(new ResultEntity(
                    persistedPlayer,
                    game,
                    player.getScore(),
                    player.getTotalResponseTimeMillis()));
            auditService.record("CREATE", "ResultEntity", result.getId(), player.getName() + " score=" + player.getScore());
        }
        game.finish();
        auditService.record("UPDATE", "QuizGameEntity", game.getId(), "Game finished");
    }
}
