package com.example.mazechase.model;

public enum Direction {
    TOP(-1, 0),
    RIGHT(0, 1),
    BOTTOM(1, 0),
    LEFT(0, -1);

    private final int rowDelta;
    private final int colDelta;

    Direction(int rowDelta, int colDelta) {
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    public int rowDelta() {
        return rowDelta;
    }

    public int colDelta() {
        return colDelta;
    }

    public Direction opposite() {
        return switch (this) {
            case TOP -> BOTTOM;
            case RIGHT -> LEFT;
            case BOTTOM -> TOP;
            case LEFT -> RIGHT;
        };
    }
}
