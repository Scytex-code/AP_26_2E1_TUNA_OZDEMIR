package com.lab6.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.lab6.database.DatabaseConnection;

public class GenreDAO {

    public void create(String name) throws SQLException {

        Connection con = DatabaseConnection.getInstance().getConnection();

        String sql = "INSERT INTO genres(name) VALUES(?)";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, name);

        stmt.executeUpdate();
    }

    public String findById(int id) throws SQLException {

        Connection con = DatabaseConnection.getInstance().getConnection();

        String sql = "SELECT name FROM genres WHERE id=?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, id);

        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            return rs.getString(1);

        return null;
    }

    public Integer findByName(String name) throws SQLException {

        Connection con = DatabaseConnection.getInstance().getConnection();

        String sql = "SELECT id FROM genres WHERE name=?";

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, name);

        ResultSet rs = stmt.executeQuery();

        if(rs.next())
            return rs.getInt(1);

        return null;
    }
}