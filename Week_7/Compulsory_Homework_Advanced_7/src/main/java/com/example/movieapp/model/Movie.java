package com.example.movieapp.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int id;
    private String title;
    private Date releaseDate;
    private int duration;
    private float score;
    private List<Genre> genres;
    private List<Actor> actors;

    public Movie() {}

    public Movie(int id, String title, Date releaseDate, int duration, float score) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
    }

    public Movie(String title, Date releaseDate, int duration, float score) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public List<Genre> getGenres() {
        if (genres == null) {
            genres = new ArrayList<>();
        }
        if (genres == null) {
            genres = new ArrayList<>();
        }
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        if (actors == null) {
            actors = new ArrayList<>();
        }
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
                ", score=" + score +
                ", genres=" + genres +
                ", actors=" + actors +
                '}';
    }
}

