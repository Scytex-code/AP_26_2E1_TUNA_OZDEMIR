# Lab 8 - Homework Advanced Maze Builder

Swing + Java2D desktop application for creating, editing, validating, saving, loading and exporting mazes.

## Implemented requirements

- Main frame with `BorderLayout`.
- Configuration panel with labels, row/column spinners, a draw button and animation speed control.
- Center canvas that draws every cell as a square and every wall as a line.
- Control panel with Create, Generate Perfect, Validate, Export PNG, Save, Load, Reset and Exit.
- Manual wall editing by clicking near a cell wall. Adjacent cell walls are updated consistently.
- Traversability validation from the top-left cell to the bottom-right cell, with the found path highlighted.
- PNG export using `ImageIO`.
- Save and restore using Java object serialization.
- Automatic perfect-maze generation using recursive backtracking with animated GUI updates.

## Perfect maze validation

The recursive backtracking generator starts with all walls present, visits each cell once and removes one wall when moving to an unvisited neighbor. The resulting graph is a spanning tree over all cells.

The application also proves this programmatically with `Maze.isPerfectMaze()`:

- all cells must be reachable from `(0, 0)`;
- the number of open edges must be exactly `rows * cols - 1`.

A connected graph with `n` vertices and `n - 1` edges is a tree, so there is exactly one path between any two cells and there are no isolated areas.

## Run

```bash
mvn clean package
java -jar target/homework-advanced-8-1.0-SNAPSHOT.jar
```
