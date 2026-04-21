package com.example.movieapp.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovieResponse {

    private Integer id;
    private String title;
    private LocalDate releaseDate;
    private Integer duration;
    private Float score;
    private List<GenreResponse> genres = new ArrayList<>();
    private List<ActorResponse> actors = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public List<GenreResponse> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreResponse> genres) {
        this.genres = genres;
    }

    public List<ActorResponse> getActors() {
        return actors;
    }

    public void setActors(List<ActorResponse> actors) {
        this.actors = actors;
    }
}
