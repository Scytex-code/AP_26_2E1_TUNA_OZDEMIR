package com.example.movieapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieapp.dto.MovieRequest;
import com.example.movieapp.dto.MovieResponse;
import com.example.movieapp.dto.MovieScorePatchRequest;
import com.example.movieapp.service.MovieService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    @Operation(summary = "Get all movies")
    public ResponseEntity<List<MovieResponse>> getMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a movie by id")
    public ResponseEntity<MovieResponse> getMovie(@PathVariable Integer id) {
        return ResponseEntity.ok(movieService.getMovie(id));
    }

    @PostMapping
    @Operation(summary = "Create a new movie")
    public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing movie")
    public ResponseEntity<MovieResponse> updateMovie(@PathVariable Integer id,
                                                     @Valid @RequestBody MovieRequest request) {
        return ResponseEntity.ok(movieService.updateMovie(id, request));
    }

    @PatchMapping("/{id}/score")
    @Operation(summary = "Update only the score of a movie")
    public ResponseEntity<MovieResponse> patchMovieScore(@PathVariable Integer id,
                                                         @Valid @RequestBody MovieScorePatchRequest request) {
        return ResponseEntity.ok(movieService.updateScore(id, request.getScore()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a movie")
    public ResponseEntity<Void> deleteMovie(@PathVariable Integer id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
