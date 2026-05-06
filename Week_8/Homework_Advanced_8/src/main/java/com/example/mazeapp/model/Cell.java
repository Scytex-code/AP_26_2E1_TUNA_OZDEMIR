package com.example.mazeapp.model;

import java.io.Serializable;

public class Cell implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int row;
    private final int col;
    private boolean topWall = true;
    private boolean rightWall = true;
    private boolean bottomWall = true;
    private boolean leftWall = true;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean hasWall(Wall wall) {
        return switch (wall) {
            case TOP -> topWall;
            case RIGHT -> rightWall;
            case BOTTOM -> bottomWall;
            case LEFT -> leftWall;
        };
    }

    public void setWall(Wall wall, boolean present) {
        switch (wall) {
            case TOP -> topWall = present;
            case RIGHT -> rightWall = present;
            case BOTTOM -> bottomWall = present;
            case LEFT -> leftWall = present;
        }
    }

    public boolean hasTopWall() {
        return topWall;
    }

    public boolean hasRightWall() {
        return rightWall;
    }

    public boolean hasBottomWall() {
        return bottomWall;
    }

    public boolean hasLeftWall() {
        return leftWall;
    }
}
