package com.lab6ha.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import com.lab6ha.dao.ActorDAO;
import com.lab6ha.dao.GenreDAO;
import com.lab6ha.dao.MovieActorDAO;
import com.lab6ha.dao.MovieDAO;
import com.lab6ha.dao.MovieGenreDAO;
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
            String line = br.readLine(); // header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) continue; // eksik satırı atla

                String title = parts[0].trim();
                int duration = Integer.parseInt(parts[1].trim());
                double score = Double.parseDouble(parts[2].trim());
                String genreName = parts[3].trim();
                String[] actorNames = parts[4].split(";");

                // Genre ekle veya bul
                Genre genre = genreDAO.findByName(genreName);
                if (genre == null) {
                    genreDAO.create(genreName);
                    genre = genreDAO.findByName(genreName);
                }

                // Movie ekle veya bul
                Movie movie = movieDAO.findAll().stream()
                        .filter(m -> m.getTitle().equals(title))
                        .findFirst()
                        .orElse(null);
                if (movie == null) {
                    movieDAO.create(title, duration, score);
                    movie = movieDAO.findAll().stream()
                            .filter(m -> m.getTitle().equals(title))
                            .findFirst()
                            .orElseThrow(() -> new SQLException("Failed to retrieve movie: " + title));
                }

                // Genre set et
                movieGenreDAO.setGenreForMovie(movie.getId(), genre.getId());

                // Actors ekle ve ilişkilendir (duplicate kontrol)
                for (String actorName : actorNames) {
                    actorName = actorName.trim();
                    Actor actor = actorDAO.findByName(actorName);
                    if (actor == null) {
                        actorDAO.create(actorName);
                        actor = actorDAO.findByName(actorName);
                    }

                    // Eklenmiş mi kontrol et
                    if (!movieActorDAO.isActorInMovie(movie.getId(), actor.getId())) {
                        movieActorDAO.addActorToMovie(movie.getId(), actor.getId());
                    }
                }
            }
        }
    }
}