package com.example.movieapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieapp.domain.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Integer> {
    Optional<GenreEntity> findByNameIgnoreCase(String name);
}
