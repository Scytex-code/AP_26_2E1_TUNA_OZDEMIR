package com.example.movieapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Genre;
import com.example.movieapp.model.Movie;
import com.example.movieapp.util.DatabaseConnection;

public class MovieDAO {

    private final GenreDAO genreDAO = new GenreDAO();
    private final ActorDAO actorDAO = new ActorDAO();

    public void create(Movie movie) throws SQLException {
        String sql = "INSERT INTO movies (title, release_date, duration, score) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movie.getTitle());
            stmt.setDate(2, movie.getReleaseDate());
            stmt.setInt(3, movie.getDuration());
            stmt.setFloat(4, movie.getScore());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movie.setId(rs.getInt(1));
                }
            }
        }
        // Add genres and actors
        if (movie.getGenres() != null) {
            for (Genre genre : movie.getGenres()) {
                addGenreToMovie(movie.getId(), genre.getId());
            }
        }
        if (movie.getActors() != null) {
            for (Actor actor : movie.getActors()) {
                addActorToMovie(movie.getId(), actor.getId());
            }
        }
    }

    public Optional<Movie> findById(int id) throws SQLException {
        String sql = "SELECT * FROM movies WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getDate("release_date"), rs.getInt("duration"), rs.getFloat("score"));
                    movie.setGenres(findGenresByMovieId(id));
                    movie.setActors(findActorsByMovieId(id));
                    return Optional.of(movie);
                }
            }
        }
        return Optional.empty();
    }

    public List<Movie> findAll() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sql = "SELECT * FROM movies";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = new Movie(rs.getInt("id"), rs.getString("title"), rs.getDate("release_date"), rs.getInt("duration"), rs.getFloat("score"));
                movie.setGenres(findGenresByMovieId(movie.getId()));
                movie.setActors(findActorsByMovieId(movie.getId()));
                movies.add(movie);
            }
        }
        return movies;
    }

    public List<Genre> findGenresByMovieId(int movieId) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.* FROM genres g JOIN movie_genres mg ON g.id = mg.genre_id WHERE mg.movie_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
                }
            }
        }
        return genres;
    }

    public List<Actor> findActorsByMovieId(int movieId) throws SQLException {
        List<Actor> actors = new ArrayList<>();
        String sql = "SELECT a.* FROM actors a JOIN movie_actors ma ON a.id = ma.actor_id WHERE ma.movie_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Actor(rs.getInt("id"), rs.getString("name"), rs.getDate("birth_date")));
                }
            }
        }
        return actors;
    }

    private void addGenreToMovie(int movieId, int genreId) throws SQLException {
        String sql = "INSERT INTO movie_genres (movie_id, genre_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.setInt(2, genreId);
            stmt.executeUpdate();
        }
    }

    private void addActorToMovie(int movieId, int actorId) throws SQLException {
        String sql = "INSERT INTO movie_actors (movie_id, actor_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.setInt(2, actorId);
            stmt.executeUpdate();
        }
    }
}