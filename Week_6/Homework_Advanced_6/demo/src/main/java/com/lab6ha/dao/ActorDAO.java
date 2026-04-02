package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lab6ha.database.DatabaseConnection;
import com.lab6ha.model.Actor;

public class ActorDAO {

    public void create(String name) throws SQLException {
        String sql = "INSERT INTO actors(name) VALUES(?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public Actor findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM actors WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getInt("id"));
                    actor.setName(rs.getString("name"));
                    return actor;
                }
            }
        }
        return null;
    }

    public Actor findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM actors WHERE name=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Actor actor = new Actor();
                    actor.setId(rs.getInt("id"));
                    actor.setName(rs.getString("name"));
                    return actor;
                }
            }
        }
        return null;
    }
}