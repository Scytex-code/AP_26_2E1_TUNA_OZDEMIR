package com.example.movieapp.service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.movieapp.domain.ActorEntity;
import com.example.movieapp.domain.GenreEntity;
import com.example.movieapp.domain.MovieEntity;
import com.example.movieapp.dto.MovieRequest;
import com.example.movieapp.dto.MovieResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.repository.ActorRepository;
import com.example.movieapp.repository.GenreRepository;
import com.example.movieapp.repository.MovieRepository;
import com.example.movieapp.service.mapper.ApiMapper;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final ApiMapper apiMapper;

    public MovieService(MovieRepository movieRepository,
                        GenreRepository genreRepository,
                        ActorRepository actorRepository,
                        ApiMapper apiMapper) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.apiMapper = apiMapper;
    }

    @Transactional(readOnly = true)
    public List<MovieResponse> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(apiMapper::toMovieResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MovieResponse getMovie(Integer id) {
        return apiMapper.toMovieResponse(findMovieEntity(id));
    }

    public MovieResponse createMovie(MovieRequest request) {
        MovieEntity movie = new MovieEntity();
        applyRequest(movie, request);
        return apiMapper.toMovieResponse(movieRepository.save(movie));
    }

    public MovieResponse updateMovie(Integer id, MovieRequest request) {
        MovieEntity movie = findMovieEntity(id);
        applyRequest(movie, request);
        return apiMapper.toMovieResponse(movieRepository.save(movie));
    }

    public MovieResponse updateScore(Integer id, Float score) {
        MovieEntity movie = findMovieEntity(id);
        movie.setScore(score);
        return apiMapper.toMovieResponse(movieRepository.save(movie));
    }

    public void deleteMovie(Integer id) {
        MovieEntity movie = findMovieEntity(id);
        movieRepository.delete(movie);
    }

    public MovieEntity findMovieEntity(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    }

    private void applyRequest(MovieEntity movie, MovieRequest request) {
        movie.setTitle(request.getTitle().trim());
        movie.setReleaseDate(request.getReleaseDate());
        movie.setDuration(request.getDuration());
        movie.setScore(request.getScore());
        movie.setGenres(resolveGenres(request.getGenreIds()));
        movie.setActors(resolveActors(request.getActorIds()));
    }

    private Set<GenreEntity> resolveGenres(Set<Integer> ids) {
        Set<GenreEntity> genres = new LinkedHashSet<>();
        if (ids == null) {
            return genres;
        }

        for (Integer id : ids) {
            GenreEntity genre = genreRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
            genres.add(genre);
        }
        return genres;
    }

    private Set<ActorEntity> resolveActors(Set<Integer> ids) {
        Set<ActorEntity> actors = new LinkedHashSet<>();
        if (ids == null) {
            return actors;
        }

        for (Integer id : ids) {
            ActorEntity actor = actorRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
            actors.add(actor);
        }
        return actors;
    }
}
