package com.example.movieapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.movieapp.model.Movie;
import com.example.movieapp.model.MovieList;
import com.example.movieapp.util.DatabaseConnection;

public class MovieListDAO {

    private final MovieDAO movieDAO = new MovieDAO();

    public void create(MovieList movieList) throws SQLException {
        String sql = "INSERT INTO movie_lists (name) VALUES (?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movieList.getName());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movieList.setId(rs.getInt(1));
                }
            }
        }
        // Add movies
        if (movieList.getMovies() != null) {
            for (Movie movie : movieList.getMovies()) {
                addMovieToList(movieList.getId(), movie.getId());
            }
        }
    }

    public Optional<MovieList> findById(int id) throws SQLException {
        String sql = "SELECT * FROM movie_lists WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    MovieList movieList = new MovieList(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("creation_timestamp"));
                    movieList.setMovies(findMoviesByListId(id));
                    return Optional.of(movieList);
                }
            }
        }
        return Optional.empty();
    }

    public List<MovieList> findAll() throws SQLException {
        List<MovieList> movieLists = new ArrayList<>();
        String sql = "SELECT * FROM movie_lists";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MovieList movieList = new MovieList(rs.getInt("id"), rs.getString("name"), rs.getTimestamp("creation_timestamp"));
                movieList.setMovies(findMoviesByListId(movieList.getId()));
                movieLists.add(movieList);
            }
        }
        return movieLists;
    }

    private List<Movie> findMoviesByListId(int listId) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT m.* FROM movies m JOIN movie_list_movies mlm ON m.id = mlm.movie_id WHERE mlm.list_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, listId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getDate("release_date"), rs.getInt("duration"), rs.getFloat("score"));
                    movie.setGenres(movieDAO.findGenresByMovieId(movie.getId()));
                    movie.setActors(movieDAO.findActorsByMovieId(movie.getId()));
                    movies.add(movie);
                }
            }
        }
        return movies;
    }

    private void addMovieToList(int listId, int movieId) throws SQLException {
        String sql = "INSERT INTO movie_list_movies (list_id, movie_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, listId);
            stmt.setInt(2, movieId);
            stmt.executeUpdate();
        }
    }
}