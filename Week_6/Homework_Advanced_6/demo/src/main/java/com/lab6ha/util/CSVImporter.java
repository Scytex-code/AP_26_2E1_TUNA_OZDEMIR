package com.lab6ha.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.lab6ha.dao.ActorDAO;
import com.lab6ha.dao.GenreDAO;
import com.lab6ha.dao.MovieActorDAO;
import com.lab6ha.dao.MovieDAO;
import com.lab6ha.dao.MovieGenreDAO;
import com.lab6ha.database.DatabaseConnection;
import com.lab6ha.model.Actor;
import com.lab6ha.model.Genre;
import com.lab6ha.model.Movie;

public class CSVImporter {

    private GenreDAO genreDAO = new GenreDAO();
    private ActorDAO actorDAO = new ActorDAO();
    private MovieDAO movieDAO = new MovieDAO();
    private MovieActorDAO movieActorDAO = new MovieActorDAO();
    private MovieGenreDAO movieGenreDAO = new MovieGenreDAO();

    public void importMovies(String filePath) throws IOException, SQLException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                String title = parts[0].trim();
                int duration = Integer.parseInt(parts[1].trim());
                double score = Double.parseDouble(parts[2].trim());
                String genreName = parts[3].trim();
                String[] actorNames = parts[4].split(";");

                // --- Genre ekle veya bul ---
                Genre genre = genreDAO.findByName(genreName);
                int genreId;
                if (genre == null) {
                    genreId = genreDAO.create(genreName);
                    genre = genreDAO.findById(genreId);
                    System.out.println("Genre added: " + genreName);
                } else {
                    genreId = genre.getId();
                }

                // --- Movie ekle veya bul ---
                Movie movie = movieDAO.findByTitle(title);
                int movieId;
                if (movie == null) {
                    movieId = movieDAO.create(title, duration, score);
                    movie = movieDAO.findById(movieId);
                    System.out.println("Movie added: " + title);

                    // --- Set genre_id directly in movies table ---
                    try (Connection con = DatabaseConnection.getConnection();
                         PreparedStatement stmt = con.prepareStatement("UPDATE movies SET genre_id=? WHERE id=?")) {
                        stmt.setInt(1, genreId);
                        stmt.setInt(2, movieId);
                        stmt.executeUpdate();
                        System.out.println("genre_id set for movie: " + title + " -> " + genreId);
                    }

                } else {
                    movieId = movie.getId();
                    // optional: update genre_id if missing or different
                    try (Connection con = DatabaseConnection.getConnection();
                         PreparedStatement stmt = con.prepareStatement("UPDATE movies SET genre_id=? WHERE id=?")) {
                        stmt.setInt(1, genreId);
                        stmt.setInt(2, movieId);
                        stmt.executeUpdate();
                        System.out.println("genre_id updated for movie: " + title + " -> " + genreId);
                    }
                }

                // --- Movie-Genre relationship (optional if still using your link table) ---
                if (movieGenreDAO.getGenreIdForMovie(movieId) == null) {
                    movieGenreDAO.setGenreForMovie(movieId, genreId);
                    System.out.println("Genre linked via movie_genres table: " + title + " -> " + genreName);
                }

                // --- Actors ekle ve Movie-Actor ilişkisi ---
                for (String actorName : actorNames) {
                    actorName = actorName.trim();
                    Actor actor = actorDAO.findByName(actorName);
                    int actorId;
                    if (actor == null) {
                        actorId = actorDAO.create(actorName);
                        actor = actorDAO.findById(actorId);
                        System.out.println("Actor added: " + actorName);
                    } else {
                        actorId = actor.getId();
                    }

                    if (!movieActorDAO.isActorInMovie(movieId, actorId)) {
                        movieActorDAO.addActorToMovie(movieId, actorId);
                        System.out.println("Actor linked to movie: " + actorName + " -> " + title);
                    }
                }
            }
        }
    }
}