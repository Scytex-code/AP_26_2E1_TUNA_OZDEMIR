package com.example.quiz.server.repository;

import com.example.quiz.server.entity.QuestionEntity;
import com.example.quiz.server.model.Question;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JpaQuestionCatalog implements CommandLineRunner {
    private final JpaQuestionRepository repository;

    public JpaQuestionCatalog(JpaQuestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            QuestionRepository.loadDefault().forEach(question -> repository.save(new QuestionEntity(
                    question.text(),
                    question.options().get(0),
                    question.options().get(1),
                    question.options().get(2),
                    question.options().get(3),
                    question.correctAnswer())));
        }
    }

    public List<Question> loadAll() {
        return repository.findAll().stream()
                .map(entity -> new Question(entity.getText(), List.of(
                        entity.getOptionA(),
                        entity.getOptionB(),
                        entity.getOptionC(),
                        entity.getOptionD()), entity.getCorrectAnswer()))
                .toList();
    }
}
