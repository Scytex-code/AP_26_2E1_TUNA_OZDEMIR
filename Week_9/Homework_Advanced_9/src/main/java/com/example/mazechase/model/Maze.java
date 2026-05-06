package com.example.mazechase.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class Maze {
    private final int rows;
    private final int cols;
    private final Cell[][] cells;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
        generatePerfectMaze();
    }

    private void generatePerfectMaze() {
        boolean[][] visited = new boolean[rows][cols];
        carve(0, 0, visited, new Random());
    }

    private void carve(int row, int col, boolean[][] visited, Random random) {
        visited[row][col] = true;
        List<Direction> directions = new ArrayList<>(List.of(Direction.values()));
        Collections.shuffle(directions, random);
        for (Direction direction : directions) {
            int nextRow = row + direction.rowDelta();
            int nextCol = col + direction.colDelta();
            if (!contains(nextRow, nextCol) || visited[nextRow][nextCol]) {
                continue;
            }
            cells[row][col].setWall(direction, false);
            cells[nextRow][nextCol].setWall(direction.opposite(), false);
            carve(nextRow, nextCol, visited, random);
        }
    }

    public List<Position> neighbors(Position position) {
        List<Position> result = new ArrayList<>();
        Cell cell = cell(position);
        for (Direction direction : Direction.values()) {
            int nextRow = position.row() + direction.rowDelta();
            int nextCol = position.col() + direction.colDelta();
            if (contains(nextRow, nextCol) && !cell.hasWall(direction)) {
                result.add(new Position(nextRow, nextCol));
            }
        }
        return result;
    }

    public List<Position> shortestPath(Position start, Position end) {
        Queue<Position> queue = new ArrayDeque<>();
        Set<Position> visited = new HashSet<>();
        Map<Position, Position> parent = new HashMap<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            Position current = queue.remove();
            if (current.equals(end)) {
                break;
            }
            for (Position next : neighbors(current)) {
                if (visited.add(next)) {
                    parent.put(next, current);
                    queue.add(next);
                }
            }
        }

        if (!visited.contains(end)) {
            return List.of();
        }
        List<Position> path = new ArrayList<>();
        for (Position current = end; current != null; current = parent.get(current)) {
            path.add(current);
        }
        Collections.reverse(path);
        return path;
    }

    public boolean contains(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public Cell cell(Position position) {
        return cells[position.row()][position.col()];
    }

    public Position exit() {
        return new Position(rows - 1, cols - 1);
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }
}
