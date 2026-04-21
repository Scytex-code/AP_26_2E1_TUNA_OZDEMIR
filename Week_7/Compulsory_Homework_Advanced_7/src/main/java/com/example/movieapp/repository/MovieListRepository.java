package com.example.movieapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieapp.domain.MovieListEntity;

public interface MovieListRepository extends JpaRepository<MovieListEntity, Integer> {
}
