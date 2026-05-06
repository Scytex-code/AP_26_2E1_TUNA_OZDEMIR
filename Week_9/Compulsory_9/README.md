# Lab 9 - Compulsory

Text-based concurrency simulation.

- Object-oriented model: `Maze`, `Cell`, `Position`, `GameState`, `Bunny`, `Robot`, `SharedMemory`.
- One thread is created for the bunny and one thread for each robot.
- The bunny and robots move randomly.
- `GameState` uses synchronized methods to protect movement and cell occupancy.
- `SharedMemory` uses synchronized methods for shared bunny information.
- The console periodically prints the maze state.

Run:

```bash
mvn clean package
java -jar target/compulsory-9-1.0-SNAPSHOT.jar
```
