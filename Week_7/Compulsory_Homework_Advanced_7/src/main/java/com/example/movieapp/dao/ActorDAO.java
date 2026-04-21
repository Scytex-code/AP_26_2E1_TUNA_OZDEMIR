package com.example.movieapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.movieapp.model.Actor;
import com.example.movieapp.util.DatabaseConnection;

public class ActorDAO {

    public void create(Actor actor) throws SQLException {
        // Check if exists
        Optional<Actor> existing = findByName(actor.getName());
        if (existing.isPresent()) {
            actor.setId(existing.get().getId());
            return;
        }
        String sql = "INSERT INTO actors (name, birth_date) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, actor.getName());
            stmt.setDate(2, actor.getBirthDate());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    actor.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                // Already exists, find and set id
                Optional<Actor> existing2 = findByName(actor.getName());
                if (existing2.isPresent()) {
                    actor.setId(existing2.get().getId());
                }
            } else {
                throw e;
            }
        }
    }

    public Optional<Actor> findById(int id) throws SQLException {
        String sql = "SELECT * FROM actors WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Actor(rs.getInt("id"), rs.getString("name"), rs.getDate("birth_date")));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Actor> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM actors WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Actor(rs.getInt("id"), rs.getString("name"), rs.getDate("birth_date")));
                }
            }
        }
        return Optional.empty();
    }

    public List<Actor> findAll() throws SQLException {
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT * FROM actors";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                actors.add(new Actor(rs.getInt("id"), rs.getString("name"), rs.getDate("birth_date")));
            }
        }
        return actors;
    }
}