package com.example.quiz.server.repository;

import com.example.quiz.server.entity.QuestionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JpaQuestionRepositoryTest {
    @Autowired
    private JpaQuestionRepository repository;

    @Test
    void savesAndReadsQuestion() {
        repository.save(new QuestionEntity("2 + 2?", "3", "4", "5", "6", 'B'));

        assertThat(repository.findAll())
                .extracting(QuestionEntity::getCorrectAnswer)
                .contains('B');
    }
}
