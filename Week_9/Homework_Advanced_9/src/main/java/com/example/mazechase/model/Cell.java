package com.example.mazechase.model;

import java.util.EnumMap;
import java.util.Map;

public class Cell {
    private final int row;
    private final int col;
    private final Map<Direction, Boolean> walls = new EnumMap<>(Direction.class);

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        for (Direction direction : Direction.values()) {
            walls.put(direction, true);
        }
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public boolean hasWall(Direction direction) {
        return walls.get(direction);
    }

    public void setWall(Direction direction, boolean present) {
        walls.put(direction, present);
    }
}
