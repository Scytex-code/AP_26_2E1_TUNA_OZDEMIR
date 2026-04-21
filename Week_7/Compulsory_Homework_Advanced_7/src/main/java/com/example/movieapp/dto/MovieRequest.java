package com.example.movieapp.dto;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MovieRequest {

    @NotBlank
    private String title;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private Integer duration;

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("10.0")
    private Float score;

    private Set<Integer> genreIds = new LinkedHashSet<>();
    private Set<Integer> actorIds = new LinkedHashSet<>();

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

    public Set<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(Set<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Set<Integer> getActorIds() {
        return actorIds;
    }

    public void setActorIds(Set<Integer> actorIds) {
        this.actorIds = actorIds;
    }
}
