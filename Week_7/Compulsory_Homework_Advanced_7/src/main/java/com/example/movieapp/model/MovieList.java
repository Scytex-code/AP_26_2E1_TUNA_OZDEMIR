package com.example.movieapp.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private int id;
    private String name;
    private Timestamp creationTimestamp;
    private List<Movie> movies;

    public MovieList() {}

    public MovieList(int id, String name, Timestamp creationTimestamp) {
        this.id = id;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }

    public MovieList(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Timestamp creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public List<Movie> getMovies() {
        if (movies == null) {
            movies = new ArrayList<>();
        }
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public String toString() {
        return "MovieList{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationTimestamp=" + creationTimestamp +
                ", movies=" + movies +
                '}';
    }
}