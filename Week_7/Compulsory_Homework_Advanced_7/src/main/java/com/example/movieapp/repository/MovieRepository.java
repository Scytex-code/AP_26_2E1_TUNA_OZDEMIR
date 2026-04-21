package com.example.movieapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieapp.domain.MovieEntity;

public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {

    @Override
    @EntityGraph(attributePaths = {"genres", "actors"})
    List<MovieEntity> findAll();

    @Override
    @EntityGraph(attributePaths = {"genres", "actors"})
    Optional<MovieEntity> findById(Integer id);

    boolean existsByTitleIgnoreCase(String title);
}
