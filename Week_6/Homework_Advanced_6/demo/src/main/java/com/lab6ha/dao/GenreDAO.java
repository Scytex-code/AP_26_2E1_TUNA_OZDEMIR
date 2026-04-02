package com.lab6ha.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lab6ha.database.DatabaseConnection;
import com.lab6ha.model.Genre;

public class GenreDAO {

    public void create(String name) throws SQLException {
        String sql = "INSERT INTO genres(name) VALUES(?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            stmt.executeUpdate();
        }
    }

    public Genre findById(int id) throws SQLException {
        String sql = "SELECT id, name FROM genres WHERE id=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Genre(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }

    public Genre findByName(String name) throws SQLException {
        String sql = "SELECT id, name FROM genres WHERE name=?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Genre(rs.getInt("id"), rs.getString("name"));
                }
            }
        }
        return null;
    }
}