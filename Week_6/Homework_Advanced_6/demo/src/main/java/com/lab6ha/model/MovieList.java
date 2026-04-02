package com.lab6ha.model;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
    private int id;
    private String name;
    private List<Movie> movies = new ArrayList<>();

    public MovieList() { }

    public MovieList(String name) { this.name = name; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public List<Movie> getMovies() { return movies; }
    public void addMovie(Movie m) { movies.add(m); }
}