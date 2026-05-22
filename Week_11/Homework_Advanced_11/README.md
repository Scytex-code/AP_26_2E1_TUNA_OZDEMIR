# Lab 11 - Homework + Advanced JPA Quiz Game

This folder continues the Lab 10 multiplayer quiz game and adds the full Lab 11 persistence requirements.

- `ServerApplication`: TCP quiz server with Spring Data JPA, H2, auditing, JPQL logging, Criteria/Specification search, second-level/query cache, and JMH benchmark code.
- `ClientApplication`: unchanged Lab 10 TCP client.

## Implemented Lab 11 Items

- Entity package: `com.example.quiz.server.entity`.
- Entity classes for players, questions, games, results, and audit logs.
- Repositories for all persistent entities.
- One-to-many relationship: `QuizGameEntity` to `ResultEntity`.
- Many-to-many relationship: `QuizGameEntity` to `PlayerEntity`.
- JPQL read query: `LoggedResultQueryService.topResultsForPlayerPrefix`.
- Transactional modifying query: `ResultRepository.addBonusToScoresBelow`.
- JPQL timing and exception logging to console and `logs/jpql-statements.log`.
- Auditing through Spring Data auditing fields plus `audit_logs` records.
- Dynamic result search with `ResultSearchCriteria` and `ResultSpecifications`.
- Hibernate second-level cache and query cache using Ehcache/JCache.
- Cache timing demo: run server main with `cache-demo`.
- JMH benchmark: `com.example.quiz.server.benchmark.JpaReadBenchmark`.

## Question Text Format

Questions are stored in `ServerApplication/src/main/resources/questions.txt`.

```text
question|answerA|answerB|answerC|answerD|correctLetter
```

## Commands

```text
join <name>
question
answer <A-D>
scores
results
bot random <name>
bot custom <name>
bot llm <easy|medium|hard> <name>
stop
cache-demo
```

The winner is decided by score. Ties are broken by the lower total response time.

## Bots

- Random AI chooses a random answer.
- Custom AI uses a small knowledge base and learns correct answers after mistakes.
- LLM AI can call an external text endpoint if `LLM_API_URL` is set. It sends the question as plain text and expects a response beginning with `A`, `B`, `C` or `D`. If no API is configured, it falls back to difficulty-based local behavior.

## Virtual Thread Scenario

Run the server main class with `virtual-demo` on JDK 21+ to compare many short-lived platform threads with virtual threads:

```text
java com.example.quiz.server.GameServer virtual-demo
```

Run this command to compare cold and warm cached reads:

```text
java com.example.quiz.server.GameServer cache-demo
```

The default H2 database runs in memory to avoid file-locking problems under synced folders such as OneDrive.
