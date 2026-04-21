package com.example.movieapp.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieapp.dto.GenreRequest;
import com.example.movieapp.dto.GenreResponse;
import com.example.movieapp.service.GenreService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    @Operation(summary = "Get all genres")
    public ResponseEntity<List<GenreResponse>> getGenres() {
        return ResponseEntity.ok(genreService.getAllGenres());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a genre by id")
    public ResponseEntity<GenreResponse> getGenre(@PathVariable Integer id) {
        return ResponseEntity.ok(genreService.getGenre(id));
    }

    @PostMapping
    @Operation(summary = "Create a new genre")
    public ResponseEntity<GenreResponse> createGenre(@Valid @RequestBody GenreRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(genreService.createGenre(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a genre")
    public ResponseEntity<GenreResponse> updateGenre(@PathVariable Integer id,
                                                     @Valid @RequestBody GenreRequest request) {
        return ResponseEntity.ok(genreService.updateGenre(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a genre")
    public ResponseEntity<Void> deleteGenre(@PathVariable Integer id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
