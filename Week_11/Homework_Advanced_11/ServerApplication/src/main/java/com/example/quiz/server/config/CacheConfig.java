package com.example.quiz.server.config;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;

@Configuration
public class CacheConfig {
    @Bean
    JCacheManagerCustomizer quizCacheCustomizer() {
        return cacheManager -> {
            if (cacheManager.getCache("questions") == null) {
                cacheManager.createCache("questions", new MutableConfiguration<>()
                        .setStoreByValue(false)
                        .setStatisticsEnabled(true));
            }
        };
    }
}
