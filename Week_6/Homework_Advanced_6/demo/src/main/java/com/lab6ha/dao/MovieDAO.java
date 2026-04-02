package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lab6ha.database.DatabaseConnection;
import com.lab6ha.model.Movie;

public class MovieDAO {

    public void create(String title, int duration, double score) throws SQLException {
        String sql = "INSERT INTO movies(title, duration, score) VALUES(?,?,?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, title);
            stmt.setInt(2, duration);
            stmt.setDouble(3, score);
            stmt.executeUpdate();
        }
    }

    public Movie findById(int id) throws SQLException {
        String sql = "SELECT id, title, duration, score FROM movies WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie();
                    movie.setId(rs.getInt("id"));
                    movie.setTitle(rs.getString("title"));
                    movie.setDuration(rs.getInt("duration"));
                    movie.setScore(rs.getDouble("score"));
                    return movie;
                }
            }
        }
        return null;
    }

    public List<Movie> findAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT id, title, duration, score FROM movies";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Movie movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setDuration(rs.getInt("duration"));
                movie.setScore(rs.getDouble("score"));
                movies.add(movie);
            }
        }
        return movies;
    }
}