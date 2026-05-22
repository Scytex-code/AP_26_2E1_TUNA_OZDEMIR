package com.example.quiz.server.search;

import com.example.quiz.server.entity.ResultEntity;
import org.springframework.data.jpa.domain.Specification;

public final class ResultSpecifications {
    private ResultSpecifications() {
    }

    public static Specification<ResultEntity> matching(ResultSearchCriteria criteria) {
        return Specification.where(playerNameStartsWith(criteria.playerNameStartsWith()))
                .and(minimumScore(criteria.minimumScore()))
                .and(gameProperty(criteria.gameProperty()))
                .and(maximumResponseTime(criteria.maximumResponseTimeMillis()));
    }

    private static Specification<ResultEntity> playerNameStartsWith(String prefix) {
        return (root, query, builder) -> prefix == null || prefix.isBlank()
                ? builder.conjunction()
                : builder.like(builder.lower(root.join("player").get("name")), prefix.toLowerCase() + "%");
    }

    private static Specification<ResultEntity> minimumScore(Integer score) {
        return (root, query, builder) -> score == null
                ? builder.conjunction()
                : builder.greaterThanOrEqualTo(root.get("score"), score);
    }

    private static Specification<ResultEntity> gameProperty(String property) {
        return (root, query, builder) -> property == null || property.isBlank()
                ? builder.conjunction()
                : builder.equal(root.join("game").get("property"), property);
    }

    private static Specification<ResultEntity> maximumResponseTime(Long responseTimeMillis) {
        return (root, query, builder) -> responseTimeMillis == null
                ? builder.conjunction()
                : builder.lessThanOrEqualTo(root.get("totalResponseTimeMillis"), responseTimeMillis);
    }
}
