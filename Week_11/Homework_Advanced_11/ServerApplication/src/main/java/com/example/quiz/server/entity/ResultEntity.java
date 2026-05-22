package com.example.quiz.server.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.Instant;

@Entity
@Table(name = "results")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResultEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player_id")
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    private QuizGameEntity game;

    private int score;
    private long totalResponseTimeMillis;
    private Instant playedAt = Instant.now();

    protected ResultEntity() {
    }

    public ResultEntity(PlayerEntity player, QuizGameEntity game, int score, long totalResponseTimeMillis) {
        this.player = player;
        this.game = game;
        this.score = score;
        this.totalResponseTimeMillis = totalResponseTimeMillis;
    }

    public Long getId() {
        return id;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public QuizGameEntity getGame() {
        return game;
    }

    public int getScore() {
        return score;
    }

    public long getTotalResponseTimeMillis() {
        return totalResponseTimeMillis;
    }

    public Instant getPlayedAt() {
        return playedAt;
    }
}
