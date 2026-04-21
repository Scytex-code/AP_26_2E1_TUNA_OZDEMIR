package com.example.movieapp.exception;

public class NoValidMovieSelectionException extends RuntimeException {

    public NoValidMovieSelectionException(String message) {
        super(message);
    }
}
