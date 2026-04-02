package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lab6ha.database.DatabaseConnection;

public class MovieGenreDAO {

    public void setGenreForMovie(int movieId, int genreId) throws SQLException {
        String sql = "UPDATE movies SET genre_id=? WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, genreId);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        }
    }
    public Integer getGenreIdForMovie(int movieId) throws SQLException {
        String sql = "SELECT genre_id FROM movies WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("genre_id");
            }
        }
        return null;
    }
}