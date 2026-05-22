package com.example.quiz.server.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "games")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class QuizGameEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Instant startedAt = Instant.now();
    private Instant finishedAt;
    private String property = "standard";

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_players",
            joinColumns = @JoinColumn(name = "game_id"),
            inverseJoinColumns = @JoinColumn(name = "player_id"))
    private Set<PlayerEntity> players = new LinkedHashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ResultEntity> results = new LinkedHashSet<>();

    public Long getId() {
        return id;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public String getProperty() {
        return property;
    }

    public Set<PlayerEntity> getPlayers() {
        return players;
    }

    public void addPlayer(PlayerEntity player) {
        players.add(player);
    }

    public void finish() {
        finishedAt = Instant.now();
    }
}
