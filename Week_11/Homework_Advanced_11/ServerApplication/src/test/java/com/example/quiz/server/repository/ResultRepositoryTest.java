package com.example.quiz.server.repository;

import com.example.quiz.server.entity.PlayerEntity;
import com.example.quiz.server.entity.QuizGameEntity;
import com.example.quiz.server.entity.ResultEntity;
import com.example.quiz.server.search.ResultSearchCriteria;
import com.example.quiz.server.search.ResultSpecifications;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ResultRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuizGameRepository gameRepository;
    @Autowired
    private ResultRepository resultRepository;

    @Test
    void jpqlAndSpecificationQueriesFindResults() {
        PlayerEntity ana = playerRepository.save(new PlayerEntity("Ana", false));
        PlayerEntity bob = playerRepository.save(new PlayerEntity("Bob", false));
        QuizGameEntity game = gameRepository.save(new QuizGameEntity());
        resultRepository.save(new ResultEntity(ana, game, 7, 1200));
        resultRepository.save(new ResultEntity(bob, game, 2, 900));

        assertThat(resultRepository.findByPlayerNamePrefix("A"))
                .extracting(ResultEntity::getScore)
                .containsExactly(7);

        assertThat(resultRepository.findAll(ResultSpecifications.matching(
                new ResultSearchCriteria("A", 5, "standard", 1500L))))
                .hasSize(1);

        assertThat(resultRepository.addBonusToScoresBelow(5, 1)).isEqualTo(1);
    }
}
