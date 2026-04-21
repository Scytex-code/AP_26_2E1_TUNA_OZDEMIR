package com.example.movieapp.service.bootstrap;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieapp.domain.ActorEntity;
import com.example.movieapp.domain.GenreEntity;
import com.example.movieapp.domain.MovieEntity;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.repository.GenreRepository;
import com.example.movieapp.repository.MovieRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

@Component
@ConditionalOnProperty(value = "movie.bootstrap.enabled", havingValue = "true", matchIfMissing = true)
public class CsvMovieBootstrapService implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(CsvMovieBootstrapService.class);

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final String csvPath;

    public CsvMovieBootstrapService(MovieRepository movieRepository,
                                    GenreRepository genreRepository,
                                    ActorRepository actorRepository,
                                    @Value("${movie.bootstrap.csv-path}") String csvPath) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.csvPath = csvPath;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        Path resolvedCsvPath = resolveCsvPath(csvPath);
        if (!Files.exists(resolvedCsvPath)) {
            log.info("CSV bootstrap skipped because file was not found: {}", resolvedCsvPath);
            return;
        }

        try (Reader fileReader = Files.newBufferedReader(resolvedCsvPath);
             CSVReader csvReader = new CSVReader(fileReader)) {
            List<String[]> rows = csvReader.readAll();
            if (rows.size() <= 1) {
                return;
            }

            for (int i = 1; i < rows.size(); i++) {
                importRow(rows.get(i));
            }
            log.info("Bootstrap import completed from {}", resolvedCsvPath);
        } catch (IOException | CsvException exception) {
            log.warn("CSV bootstrap failed: {}", exception.getMessage());
        }
    }

    private void importRow(String[] row) {
        String title = row[0].trim();
        if (movieRepository.existsByTitleIgnoreCase(title)) {
            return;
        }

        MovieEntity movie = new MovieEntity();
        movie.setTitle(title);
        movie.setReleaseDate(LocalDate.parse(row[1].trim()));
        movie.setDuration(Integer.parseInt(row[2].trim()));
        movie.setScore(Float.parseFloat(row[3].trim()));
        movie.setGenres(resolveGenres(row[4]));
        movie.setActors(resolveActors(row[5]));
        movieRepository.save(movie);
    }

    private Set<GenreEntity> resolveGenres(String csvGenres) {
        Set<GenreEntity> genres = new LinkedHashSet<>();
        for (String genreName : csvGenres.split(";")) {
            String normalized = genreName.trim();
            GenreEntity genre = genreRepository.findByNameIgnoreCase(normalized)
                    .orElseGet(() -> {
                        GenreEntity created = new GenreEntity();
                        created.setName(normalized);
                        return genreRepository.save(created);
                    });
            genres.add(genre);
        }
        return genres;
    }

    private Set<ActorEntity> resolveActors(String csvActors) {
        Set<ActorEntity> actors = new LinkedHashSet<>();
        for (String actorName : csvActors.split(";")) {
            String normalized = actorName.trim();
            ActorEntity actor = actorRepository.findByNameIgnoreCase(normalized)
                    .orElseGet(() -> {
                        ActorEntity created = new ActorEntity();
                        created.setName(normalized);
                        return actorRepository.save(created);
                    });
            actors.add(actor);
        }
        return actors;
    }

    private Path resolveCsvPath(String configuredPath) {
        Path directPath = Paths.get(configuredPath).toAbsolutePath().normalize();
        if (Files.exists(directPath)) {
            return directPath;
        }

        Path current = Paths.get("").toAbsolutePath().normalize();
        while (current != null) {
            Path candidate = current.resolve(configuredPath);
            if (Files.exists(candidate)) {
                return candidate;
            }
            current = current.getParent();
        }

        return directPath;
    }
}
