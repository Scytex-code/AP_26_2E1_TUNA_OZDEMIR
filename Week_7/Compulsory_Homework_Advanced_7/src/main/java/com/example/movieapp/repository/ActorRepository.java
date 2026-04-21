package com.example.movieapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.movieapp.domain.ActorEntity;

public interface ActorRepository extends JpaRepository<ActorEntity, Integer> {
    Optional<ActorEntity> findByNameIgnoreCase(String name);
}
