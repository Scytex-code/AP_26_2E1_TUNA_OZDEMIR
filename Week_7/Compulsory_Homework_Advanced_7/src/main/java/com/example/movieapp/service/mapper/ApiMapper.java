package com.example.movieapp.service.mapper;

import java.util.Comparator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.movieapp.domain.ActorEntity;
import com.example.movieapp.domain.GenreEntity;
import com.example.movieapp.domain.MovieEntity;
import com.example.movieapp.dto.ActorResponse;
import com.example.movieapp.dto.GenreResponse;
import com.example.movieapp.dto.MovieResponse;

@Component
public class ApiMapper {

    public ActorResponse toActorResponse(ActorEntity actor) {
        ActorResponse response = new ActorResponse();
        response.setId(actor.getId());
        response.setName(actor.getName());
        response.setBirthDate(actor.getBirthDate());
        return response;
    }

    public GenreResponse toGenreResponse(GenreEntity genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        return response;
    }

    public MovieResponse toMovieResponse(MovieEntity movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setReleaseDate(movie.getReleaseDate());
        response.setDuration(movie.getDuration());
        response.setScore(movie.getScore());
        response.setGenres(movie.getGenres().stream()
                .sorted(Comparator.comparing(GenreEntity::getId))
                .map(this::toGenreResponse)
                .collect(Collectors.toList()));
        response.setActors(movie.getActors().stream()
                .sorted(Comparator.comparing(ActorEntity::getId))
                .map(this::toActorResponse)
                .collect(Collectors.toList()));
        return response;
    }
}
