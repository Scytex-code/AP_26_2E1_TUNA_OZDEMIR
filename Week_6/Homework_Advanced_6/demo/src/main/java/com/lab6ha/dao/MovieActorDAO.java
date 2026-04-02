package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lab6ha.database.DatabaseConnection;

public class MovieActorDAO {

    public void addActorToMovie(int movieId, int actorId) throws SQLException {
        String sql = "INSERT INTO movie_actors(movie_id, actor_id) VALUES(?,?)"; // düzeltildi

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            stmt.setInt(2, actorId);
            stmt.executeUpdate();
        }
    }
    public boolean isActorInMovie(int movieId, int actorId) throws SQLException {
        String sql = "SELECT 1 FROM movie_actors WHERE movie_id=? AND actor_id=?";
        try (Connection con = DatabaseConnection.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.setInt(2, actorId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}