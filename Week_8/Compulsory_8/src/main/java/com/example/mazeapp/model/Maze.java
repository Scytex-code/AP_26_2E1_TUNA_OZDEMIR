package com.example.mazeapp.model;

import java.util.Random;

public class Maze {
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
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Cell cell = cells[row][col];
                cell.setTopWall(true);
                cell.setRightWall(true);
                cell.setBottomWall(true);
                cell.setLeftWall(true);
            }
        }
    }

    public void randomizeWalls() {
        resetWalls();
        Random random = new Random();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col < cols - 1 && random.nextBoolean()) {
                    removeWallBetween(cells[row][col], cells[row][col + 1]);
                }
                if (row < rows - 1 && random.nextBoolean()) {
                    removeWallBetween(cells[row][col], cells[row + 1][col]);
                }
            }
        }
    }

    private void removeWallBetween(Cell first, Cell second) {
        if (first.getRow() == second.getRow()) {
            if (first.getCol() < second.getCol()) {
                first.setRightWall(false);
                second.setLeftWall(false);
            } else {
                first.setLeftWall(false);
                second.setRightWall(false);
            }
            return;
        }

        if (first.getRow() < second.getRow()) {
            first.setBottomWall(false);
            second.setTopWall(false);
        } else {
            first.setTopWall(false);
            second.setBottomWall(false);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Cell getCell(int row, int col) {
        return cells[row][col];
    }
}
