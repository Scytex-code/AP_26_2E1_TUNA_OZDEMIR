package com.lab6ha.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lab6ha.database.DatabaseConnection;

public class MovieActorDAOHelper {

    // Film için aktör ID’lerini döndür
    public static List<Integer> getActorIdsForMovie(int movieId) throws SQLException {
        List<Integer> actorIds = new ArrayList<>();
        String sql = "SELECT actor_id FROM movie_actors WHERE movie_id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actorIds.add(rs.getInt("actor_id"));
                }
            }
        }
        return actorIds;
    }
}