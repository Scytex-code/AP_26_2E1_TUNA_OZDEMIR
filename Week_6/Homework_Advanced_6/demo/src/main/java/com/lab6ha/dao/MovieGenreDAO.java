package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lab6ha.database.DatabaseConnection;

public class MovieGenreDAO {

    // Film için genre set et (UPDATE)
    public void setGenreForMovie(int movieId, int genreId) throws SQLException {
        String sql = "UPDATE movies SET genre_id=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, genreId);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        }
    }

    // Filmin genre ID'sini getir
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

    // Film için genre set edilmiş mi kontrol et
    public boolean isGenreSet(int movieId) throws SQLException {
        Integer genreId = getGenreIdForMovie(movieId);
        return genreId != null && genreId > 0; // null veya 0 değilse genre set edilmiş demektir
    }
}