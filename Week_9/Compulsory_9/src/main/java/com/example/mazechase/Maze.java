package com.example.mazechase;

import java.util.ArrayList;
import java.util.List;

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
        openSimpleValidMaze();
    }

    private void openSimpleValidMaze() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (col < cols - 1) {
                    cells[row][col].setRightWall(false);
                    cells[row][col + 1].setLeftWall(false);
                }
                if (row < rows - 1) {
                    cells[row][col].setBottomWall(false);
                    cells[row + 1][col].setTopWall(false);
                }
            }
        }
    }

    public List<Position> neighbors(Position position) {
        List<Position> result = new ArrayList<>();
        Cell cell = cells[position.row()][position.col()];
        if (!cell.hasTopWall() && contains(position.row() - 1, position.col())) {
            result.add(new Position(position.row() - 1, position.col()));
        }
        if (!cell.hasRightWall() && contains(position.row(), position.col() + 1)) {
            result.add(new Position(position.row(), position.col() + 1));
        }
        if (!cell.hasBottomWall() && contains(position.row() + 1, position.col())) {
            result.add(new Position(position.row() + 1, position.col()));
        }
        if (!cell.hasLeftWall() && contains(position.row(), position.col() - 1)) {
            result.add(new Position(position.row(), position.col() - 1));
        }
        return result;
    }

    public boolean contains(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public int rows() {
        return rows;
    }

    public int cols() {
        return cols;
    }
}
