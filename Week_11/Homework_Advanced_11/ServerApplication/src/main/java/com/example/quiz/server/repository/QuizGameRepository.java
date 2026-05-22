package com.example.quiz.server.repository;

import com.example.quiz.server.entity.QuizGameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizGameRepository extends JpaRepository<QuizGameEntity, Long> {
}
