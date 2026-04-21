package com.example.movieapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.example.movieapp.model.Genre;

class GenreDAOTest {

    @Test
    void shouldStoreGenreName() {
        Genre genre = new Genre("Drama");
        assertEquals("Drama", genre.getName());
    }
}
