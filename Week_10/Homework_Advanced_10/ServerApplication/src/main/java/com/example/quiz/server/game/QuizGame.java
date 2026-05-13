package com.example.quiz.server.game;

import com.example.quiz.server.bot.BotPlayer;
import com.example.quiz.server.bot.KnowledgeBaseBot;
import com.example.quiz.server.bot.LlmBot;
import com.example.quiz.server.bot.RandomAiBot;
import com.example.quiz.server.model.Player;
import com.example.quiz.server.model.Question;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class QuizGame {
    private static final long ANSWER_TIME_LIMIT_MILLIS = 10_000;

    private final List<Question> questions;
    private final Consumer<String> broadcaster;
    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<String, BotPlayer> bots = new ConcurrentHashMap<>();
    private final Set<String> answeredCurrentQuestion = ConcurrentHashMap.newKeySet();
    private final ScheduledExecutorService botExecutor = Executors.newScheduledThreadPool(2);
    private int currentQuestionIndex = -1;
    private long questionStartedAtNanos;

    public QuizGame(List<Question> questions, Consumer<String> broadcaster) {
        this.questions = questions;
        this.broadcaster = broadcaster;
    }

    public synchronized String join(String name) {
        players.putIfAbsent(name, new Player(name, false));
        broadcaster.accept("SERVER " + name + " joined the game.");
        return "OK Joined as " + name;
    }

    public synchronized void leave(String name) {
        broadcaster.accept("SERVER " + name + " left the game.");
    }

    public synchronized String addBot(String type, String difficultyOrName, String maybeName) {
        BotPlayer bot = switch (type.toLowerCase()) {
            case "random" -> new RandomAiBot(difficultyOrName);
            case "custom" -> new KnowledgeBaseBot(difficultyOrName);
            case "llm" -> new LlmBot(difficultyOrName, maybeName);
            default -> null;
        };
        if (bot == null) {
            return "ERROR Unknown bot type. Use random, custom or llm.";
        }
        bots.put(bot.name(), bot);
        players.put(bot.name(), new Player(bot.name(), true));
        broadcaster.accept("SERVER Bot joined: " + bot.name() + " (" + type + ")");
        return "OK Bot added: " + bot.name();
    }

    public synchronized String nextQuestion() {
        if (currentQuestionIndex + 1 >= questions.size()) {
            return results();
        }
        currentQuestionIndex++;
        answeredCurrentQuestion.clear();
        questionStartedAtNanos = System.nanoTime();
        Question question = questions.get(currentQuestionIndex);
        broadcaster.accept(question.format() + " | timeLimitMs=" + ANSWER_TIME_LIMIT_MILLIS);
        scheduleBots(question);
        return "OK Question " + (currentQuestionIndex + 1) + "/" + questions.size();
    }

    public synchronized String answer(String playerName, String answer) {
        if (currentQuestionIndex < 0) {
            return "ERROR No active question. Use question.";
        }
        if (!players.containsKey(playerName)) {
            return "ERROR Unknown player: " + playerName;
        }
        if (!answeredCurrentQuestion.add(playerName)) {
            return "ERROR " + playerName + " already answered this question.";
        }
        Question question = questions.get(currentQuestionIndex);
        long responseTimeMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - questionStartedAtNanos);
        boolean inTime = responseTimeMillis <= ANSWER_TIME_LIMIT_MILLIS;
        boolean correct = inTime && question.isCorrect(answer);
        players.get(playerName).recordAnswer(correct, responseTimeMillis);
        BotPlayer bot = bots.get(playerName);
        if (bot != null) {
            bot.learn(question, answer, correct);
        }
        String status = correct ? "correct" : inTime ? "wrong" : "too late";
        broadcaster.accept("RESULT " + playerName + " answered " + status + " in " + responseTimeMillis + " ms");
        return "OK Answer recorded: " + status;
    }

    public String scoreboard() {
        return players.values().stream()
                .sorted(scoreComparator())
                .map(player -> player.getName() + "=" + player.getScore() + " (" + player.getTotalResponseTimeMillis() + " ms)")
                .collect(Collectors.joining(", ", "SCORES ", ""));
    }

    public String results() {
        return players.values().stream()
                .sorted(scoreComparator())
                .findFirst()
                .map(winner -> "WINNER " + winner.getName() + " score=" + winner.getScore()
                        + " totalTimeMs=" + winner.getTotalResponseTimeMillis() + " | " + scoreboard())
                .orElse("WINNER nobody");
    }

    public void shutdown() {
        botExecutor.shutdownNow();
    }

    private Comparator<Player> scoreComparator() {
        return Comparator.comparingInt(Player::getScore).reversed()
                .thenComparingLong(Player::getTotalResponseTimeMillis)
                .thenComparing(Player::getName);
    }

    private void scheduleBots(Question question) {
        bots.values().forEach(bot -> botExecutor.schedule(
                () -> answer(bot.name(), bot.answer(question)),
                300 + Math.abs(bot.name().hashCode() % 800),
                TimeUnit.MILLISECONDS));
    }
}
