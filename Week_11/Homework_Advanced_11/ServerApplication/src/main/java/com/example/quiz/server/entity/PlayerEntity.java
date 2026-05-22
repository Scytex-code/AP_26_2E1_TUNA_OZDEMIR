package com.example.quiz.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "players")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayerEntity extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private boolean bot;

    @ManyToMany(mappedBy = "players")
    private Set<QuizGameEntity> games = new LinkedHashSet<>();

    protected PlayerEntity() {
    }

    public PlayerEntity(String name, boolean bot) {
        this.name = name;
        this.bot = bot;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }
}
