package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.lab6ha.database.DatabaseConnection;
import com.lab6ha.model.Movie;

public class MovieListDAO {

    // movie_list tablosuna yeni liste ekle ve ID döndür
    public int create(String name) throws SQLException {
        String sql = "INSERT INTO movie_list(name, created_at) VALUES (?, NOW())";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, name);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // eklenen listenin ID'si
                }
            }
        }
        throw new SQLException("Failed to create movie list: " + name);
    }

    // movie_list_movies tablosuna film ekle
    public void addMovieToList(int listId, int movieId) throws SQLException {
        String sql = "INSERT INTO movie_list_movies(list_id, movie_id) VALUES (?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, listId);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        }
    }

    // Listeyi ve filmleri DB'ye kaydeden yardımcı metod
    public void saveListWithMovies(String listName, List<Movie> movies) throws SQLException {
        int listId = create(listName);
        for (Movie m : movies) {
            addMovieToList(listId, m.getId());
        }
    }
}