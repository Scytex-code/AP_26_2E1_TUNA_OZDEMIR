# Lab 9 - Homework Advanced

Concurrent maze chase simulation based on the Lab 8 maze idea.

## Implemented requirements

- Object-oriented model: maze, cells, positions, actors, shared memory and synchronized game state.
- Bunny and robots run concurrently, one thread per actor.
- Text mode: the daemon manager periodically prints the board, running time and shared memory status.
- Synchronized movement: `GameState` protects positions and prevents two robots from occupying the same cell.
- Synchronized shared memory: robots share visited cells and sensed bunny positions.
- Systematic robot exploration: robots prefer cells that no robot has visited before.
- Proximity sensors: robots sense bunnies within Manhattan distance `2`.
- Once a bunny is sensed, robots use BFS shortest path to move toward the last known location.
- Commands from keyboard:
  - `speed all 200`
  - `speed robots 120`
  - `speed Bunny-1 500`
  - `pause all`, `pause robots`, `pause Bunny-1`
  - `resume all`, `resume robots`, `resume Bunny-1`
  - `stop Robot-1`
  - `quit`
- Daemon manager thread stops the game after a time limit.
- Swing GUI shows the maze, robots, bunnies, exit and shared visited cells.
- More than one bunny is supported; this configuration starts with two bunnies.

Run:

```bash
mvn clean package
java -jar target/homework-advanced-9-1.0-SNAPSHOT.jar
```
