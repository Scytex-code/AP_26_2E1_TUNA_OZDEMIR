package com.example.movieapp.dto;

import java.util.ArrayList;
import java.util.List;

public class UnrelatedMovieListResponse {

    private int minimumExclusiveSize;
    private int selectedMovieCount;
    private Integer savedListId;
    private String savedListName;
    private List<MovieResponse> movies = new ArrayList<>();

    public int getMinimumExclusiveSize() {
        return minimumExclusiveSize;
    }

    public void setMinimumExclusiveSize(int minimumExclusiveSize) {
        this.minimumExclusiveSize = minimumExclusiveSize;
    }

    public int getSelectedMovieCount() {
        return selectedMovieCount;
    }

    public void setSelectedMovieCount(int selectedMovieCount) {
        this.selectedMovieCount = selectedMovieCount;
    }

    public Integer getSavedListId() {
        return savedListId;
    }

    public void setSavedListId(Integer savedListId) {
        this.savedListId = savedListId;
    }

    public String getSavedListName() {
        return savedListName;
    }

    public void setSavedListName(String savedListName) {
        this.savedListName = savedListName;
    }

    public List<MovieResponse> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieResponse> movies) {
        this.movies = movies;
    }
}
