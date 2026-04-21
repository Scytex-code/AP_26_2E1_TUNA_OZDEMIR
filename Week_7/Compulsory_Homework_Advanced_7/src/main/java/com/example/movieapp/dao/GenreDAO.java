package com.example.movieapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.movieapp.model.Genre;
import com.example.movieapp.util.DatabaseConnection;

public class GenreDAO {

    public void create(Genre genre) throws SQLException {
        // Check if exists
        Optional<Genre> existing = findByName(genre.getName());
        if (existing.isPresent()) {
            genre.setId(existing.get().getId());
            return;
        }
        String sql = "INSERT INTO genres (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, genre.getName());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    genre.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                // Already exists, find and set id
                Optional<Genre> existing2 = findByName(genre.getName());
                if (existing2.isPresent()) {
                    genre.setId(existing2.get().getId());
                }
            } else {
                throw e;
            }
        }
    }

    public Optional<Genre> findByName(String name) throws SQLException {
        String sql = "SELECT * FROM genres WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Genre(rs.getInt("id"), rs.getString("name")));
                }
            }
        }
        return Optional.empty();
    }

    public List<Genre> findAll() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM genres";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
            }
        }
        return genres;
    }
}