package com.example.mazeapp.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;

public class Maze implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int rows;
    private final int cols;
    private final Cell[][] cells;

    public Maze(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Maze dimensions must be positive.");
        }
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];
        initializeCells();
    }

    private void initializeCells() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }

    public void resetWalls() {
        forEachCell(cell -> {
            cell.setWall(Wall.TOP, true);
            cell.setWall(Wall.RIGHT, true);
            cell.setWall(Wall.BOTTOM, true);
            cell.setWall(Wall.LEFT, true);
        });
    }

    public void randomizeWalls() {
        resetWalls();
        Random random = new Random();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col < cols - 1 && random.nextBoolean()) {
                    setWallBetween(cells[row][col], cells[row][col + 1], false);
                }
                if (row < rows - 1 && random.nextBoolean()) {
                    setWallBetween(cells[row][col], cells[row + 1][col], false);
                }
            }
        }
    }

    public void toggleWall(int row, int col, Wall wall) {
        Cell cell = getCell(row, col);
        setWall(row, col, wall, !cell.hasWall(wall));
    }

    public void setWall(int row, int col, Wall wall, boolean present) {
        Cell cell = getCell(row, col);
        cell.setWall(wall, present);

        int neighborRow = row + rowDelta(wall);
        int neighborCol = col + colDelta(wall);
        if (!contains(neighborRow, neighborCol)) {
            return;
        }
        getCell(neighborRow, neighborCol).setWall(opposite(wall), present);
    }

    public boolean isTraversable(Point start, Point end) {
        return shortestPath(start, end).contains(end);
    }

    public Set<Point> shortestPath(Point start, Point end) {
        validatePoint(start);
        validatePoint(end);

        Point[][] parent = new Point[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            Point current = queue.remove();
            if (current.equals(end)) {
                break;
            }
            for (Point next : openNeighbors(current.x, current.y)) {
                if (!visited[next.x][next.y]) {
                    visited[next.x][next.y] = true;
                    parent[next.x][next.y] = current;
                    queue.add(next);
                }
            }
        }

        Set<Point> path = new HashSet<>();
        if (!visited[end.x][end.y]) {
            return path;
        }
        for (Point current = end; current != null; current = parent[current.x][current.y]) {
            path.add(current);
        }
        return path;
    }

    public void generatePerfectMaze(Consumer<Maze> stepListener, int delayMillis) throws InterruptedException {
        resetWalls();
        boolean[][] visited = new boolean[rows][cols];
        carveFrom(0, 0, visited, new Random(), stepListener, Math.max(0, delayMillis));
    }

    private void carveFrom(int row, int col, boolean[][] visited, Random random,
                           Consumer<Maze> stepListener, int delayMillis) throws InterruptedException {
        visited[row][col] = true;
        List<Wall> directions = new ArrayList<>(List.of(Wall.TOP, Wall.RIGHT, Wall.BOTTOM, Wall.LEFT));
        Collections.shuffle(directions, random);

        for (Wall wall : directions) {
            int nextRow = row + rowDelta(wall);
            int nextCol = col + colDelta(wall);
            if (!contains(nextRow, nextCol) || visited[nextRow][nextCol]) {
                continue;
            }
            setWall(row, col, wall, false);
            stepListener.accept(this);
            if (delayMillis > 0) {
                Thread.sleep(delayMillis);
            }
            carveFrom(nextRow, nextCol, visited, random, stepListener, delayMillis);
        }
    }

    public boolean isPerfectMaze() {
        int cellCount = rows * cols;
        return countReachableCells(new Point(0, 0)) == cellCount && countOpenEdges() == cellCount - 1;
    }

    private int countReachableCells(Point start) {
        boolean[][] visited = new boolean[rows][cols];
        Queue<Point> queue = new ArrayDeque<>();
        queue.add(start);
        visited[start.x][start.y] = true;
        int count = 0;

        while (!queue.isEmpty()) {
            Point current = queue.remove();
            count++;
            for (Point next : openNeighbors(current.x, current.y)) {
                if (!visited[next.x][next.y]) {
                    visited[next.x][next.y] = true;
                    queue.add(next);
                }
            }
        }
        return count;
    }

    private int countOpenEdges() {
        int edges = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[row][col];
                if (col < cols - 1 && !cell.hasRightWall()) {
                    edges++;
                }
                if (row < rows - 1 && !cell.hasBottomWall()) {
                    edges++;
                }
            }
        }
        return edges;
    }

    private List<Point> openNeighbors(int row, int col) {
        List<Point> neighbors = new ArrayList<>(4);
        for (Wall wall : Wall.values()) {
            int nextRow = row + rowDelta(wall);
            int nextCol = col + colDelta(wall);
            if (contains(nextRow, nextCol) && !getCell(row, col).hasWall(wall)) {
                neighbors.add(new Point(nextRow, nextCol));
            }
        }
        return neighbors;
    }

    private void setWallBetween(Cell first, Cell second, boolean present) {
        if (first.getRow() == second.getRow()) {
            setWall(first.getRow(), first.getCol(), first.getCol() < second.getCol() ? Wall.RIGHT : Wall.LEFT, present);
        } else {
            setWall(first.getRow(), first.getCol(), first.getRow() < second.getRow() ? Wall.BOTTOM : Wall.TOP, present);
        }
    }

    private void validatePoint(Point point) {
        if (point == null || !contains(point.x, point.y)) {
            throw new IllegalArgumentException("Cell is outside the maze.");
        }
    }

    private boolean contains(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private static int rowDelta(Wall wall) {
        return switch (wall) {
            case TOP -> -1;
            case BOTTOM -> 1;
            case LEFT, RIGHT -> 0;
        };
    }

    private static int colDelta(Wall wall) {
        return switch (wall) {
            case LEFT -> -1;
            case RIGHT -> 1;
            case TOP, BOTTOM -> 0;
        };
    }

    private static Wall opposite(Wall wall) {
        return switch (wall) {
            case TOP -> Wall.BOTTOM;
            case RIGHT -> Wall.LEFT;
            case BOTTOM -> Wall.TOP;
            case LEFT -> Wall.RIGHT;
        };
    }

    private void forEachCell(Consumer<Cell> consumer) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                consumer.accept(cells[row][col]);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        if (!contains(row, col)) {
            throw new IllegalArgumentException("Cell is outside the maze.");
        }
        return cells[row][col];
    }
}
