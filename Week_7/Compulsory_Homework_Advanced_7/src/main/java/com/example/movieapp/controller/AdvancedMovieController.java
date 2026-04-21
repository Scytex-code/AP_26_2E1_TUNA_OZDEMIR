package com.example.movieapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.movieapp.dto.UnrelatedMovieListResponse;
import com.example.movieapp.service.AdvancedMovieService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/advanced/movies")
public class AdvancedMovieController {

    private final AdvancedMovieService advancedMovieService;

    public AdvancedMovieController(AdvancedMovieService advancedMovieService) {
        this.advancedMovieService = advancedMovieService;
    }

    @GetMapping("/unrelated")
    @Operation(summary = "Find a saved list of mutually unrelated movies with size greater than minSize")
    public ResponseEntity<UnrelatedMovieListResponse> getUnrelatedMovies(
            @RequestParam(defaultValue = "1") int minSize) {
        return ResponseEntity.ok(advancedMovieService.findUnrelatedMoviesGreaterThan(minSize));
    }
}
