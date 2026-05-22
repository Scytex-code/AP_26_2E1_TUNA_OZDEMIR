package com.example.quiz.server.repository;

import com.example.quiz.server.entity.ResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ResultRepository extends JpaRepository<ResultEntity, Long>, JpaSpecificationExecutor<ResultEntity> {
    @Query("""
            select r
            from ResultEntity r
            join fetch r.player p
            join fetch r.game g
            where lower(p.name) like lower(concat(:prefix, '%'))
            order by r.score desc, r.totalResponseTimeMillis asc
            """)
    List<ResultEntity> findByPlayerNamePrefix(@Param("prefix") String prefix);

    @Transactional
    @Modifying
    @Query("update ResultEntity r set r.score = r.score + :bonus where r.score < :threshold")
    int addBonusToScoresBelow(@Param("threshold") int threshold, @Param("bonus") int bonus);
}
