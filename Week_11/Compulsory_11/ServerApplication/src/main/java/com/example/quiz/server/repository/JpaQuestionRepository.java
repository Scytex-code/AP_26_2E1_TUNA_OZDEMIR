package com.example.quiz.server.repository;

import com.example.quiz.server.entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaQuestionRepository extends JpaRepository<QuestionEntity, Long> {
}
