package com.example.quiz.server.benchmark;

import com.example.quiz.server.QuizServerApplication;
import com.example.quiz.server.service.QuizPersistenceService;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JpaReadBenchmark {
    private ConfigurableApplicationContext context;
    private QuizPersistenceService persistenceService;

    @Setup(Level.Trial)
    public void setup() {
        context = SpringApplication.run(QuizServerApplication.class,
                "--spring.datasource.url=jdbc:h2:mem:jmh;DB_CLOSE_DELAY=-1",
                "--spring.jpa.hibernate.ddl-auto=create-drop");
        persistenceService = context.getBean(QuizPersistenceService.class);
        persistenceService.loadQuestions();
    }

    @Benchmark
    public int cachedQuestionRead() {
        return persistenceService.loadQuestions().size();
    }

    @TearDown(Level.Trial)
    public void tearDown() {
        context.close();
    }
}
