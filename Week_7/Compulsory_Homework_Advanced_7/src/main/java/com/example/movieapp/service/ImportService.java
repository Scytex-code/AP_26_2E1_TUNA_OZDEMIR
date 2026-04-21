package com.example.movieapp.service;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.movieapp.dao.ActorDAO;
import com.example.movieapp.dao.GenreDAO;
import com.example.movieapp.dao.MovieDAO;
import com.example.movieapp.model.Actor;
import com.example.movieapp.model.Genre;
import com.example.movieapp.model.Movie;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class ImportService {

    private final MovieDAO movieDAO = new MovieDAO();
    private final GenreDAO genreDAO = new GenreDAO();
    private final ActorDAO actorDAO = new ActorDAO();

    public void importMoviesFromCSV(String filePath) throws IOException, CsvException, SQLException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            // Skip header
            records.remove(0);
            for (String[] record : records) {
                // Assuming CSV format: title,release_date,duration,score,genres,actors
                String title = record[0];
                Date releaseDate = Date.valueOf(record[1]);
                int duration = Integer.parseInt(record[2]);
                float score = Float.parseFloat(record[3]);
                List<String> genreNames = Arrays.asList(record[4].split(";"));
                List<String> actorNames = Arrays.asList(record[5].split(";"));

                Movie movie = new Movie(title, releaseDate, duration, score);

                // Add genres
                for (String genreName : genreNames) {
                    Optional<Genre> genreOpt = genreDAO.findByName(genreName.trim());
                    Genre genre;
                    if (genreOpt.isPresent()) {
                        genre = genreOpt.get();
                    } else {
                        genre = new Genre(genreName.trim());
                        genreDAO.create(genre);
                    }
                    movie.getGenres().add(genre);
                }

                // Add actors
                for (String actorName : actorNames) {
                    Optional<Actor> actorOpt = actorDAO.findByName(actorName.trim());
                    Actor actor;
                    if (actorOpt.isPresent()) {
                        actor = actorOpt.get();
                    } else {
                        actor = new Actor(actorName.trim(), null);
                        actorDAO.create(actor);
                    }
                    movie.getActors().add(actor);
                }

                movieDAO.create(movie);
            }
        }
    }
}