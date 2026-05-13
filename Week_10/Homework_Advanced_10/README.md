# Lab 10 - Multiplayer Quiz Game

This folder contains two projects:

- `ServerApplication`: TCP quiz server with a thread pool, graceful shutdown, timed questions, scoring and bot players.
- `ClientApplication`: TCP quiz client with two threads, one for keyboard input and one for real-time server messages.

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
