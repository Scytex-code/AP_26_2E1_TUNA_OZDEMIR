package com.example.mazechase.ui;

import com.example.mazechase.GameState;
import com.example.mazechase.SharedMemory;
import com.example.mazechase.model.Cell;
import com.example.mazechase.model.Direction;
import com.example.mazechase.model.Maze;
import com.example.mazechase.model.Position;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
    private static final int CELL_SIZE = 34;
    private static final int PADDING = 24;

    private final GameState gameState;
    private final SharedMemory sharedMemory;

    public GamePanel(GameState gameState, SharedMemory sharedMemory) {
        this.gameState = gameState;
        this.sharedMemory = sharedMemory;
        setBackground(Color.WHITE);
    }

    @Override
    public Dimension getPreferredSize() {
        Maze maze = gameState.maze();
        return new Dimension(maze.cols() * CELL_SIZE + PADDING * 2,
                maze.rows() * CELL_SIZE + PADDING * 2);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawMaze(g2);
        drawActors(g2);
        g2.dispose();
    }

    private void drawMaze(Graphics2D g2) {
        Maze maze = gameState.maze();
        for (int row = 0; row < maze.rows(); row++) {
            for (int col = 0; col < maze.cols(); col++) {
                Position position = new Position(row, col);
                int x = x(col);
                int y = y(row);
                g2.setColor(sharedMemory.hasBeenVisited(position) ? new Color(231, 241, 232) : new Color(225, 235, 244));
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                drawWalls(g2, maze.cell(position), x, y);
            }
        }
        Position exit = maze.exit();
        g2.setColor(new Color(255, 218, 98));
        g2.fillRect(x(exit.col()) + 7, y(exit.row()) + 7, CELL_SIZE - 14, CELL_SIZE - 14);
    }

    private void drawWalls(Graphics2D g2, Cell cell, int x, int y) {
        g2.setColor(new Color(36, 55, 68));
        g2.setStroke(new BasicStroke(2.5f));
        if (cell.hasWall(Direction.TOP)) {
            g2.drawLine(x, y, x + CELL_SIZE, y);
        }
        if (cell.hasWall(Direction.RIGHT)) {
            g2.drawLine(x + CELL_SIZE, y, x + CELL_SIZE, y + CELL_SIZE);
        }
        if (cell.hasWall(Direction.BOTTOM)) {
            g2.drawLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
        }
        if (cell.hasWall(Direction.LEFT)) {
            g2.drawLine(x, y, x, y + CELL_SIZE);
        }
    }

    private void drawActors(Graphics2D g2) {
        g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        for (Map.Entry<String, Position> entry : gameState.bunniesSnapshot().entrySet()) {
            drawToken(g2, entry.getValue(), new Color(66, 150, 86), "B");
        }
        for (Map.Entry<String, Position> entry : gameState.robotsSnapshot().entrySet()) {
            drawToken(g2, entry.getValue(), new Color(196, 69, 69), "R");
        }
    }

    private void drawToken(Graphics2D g2, Position position, Color color, String label) {
        int diameter = CELL_SIZE - 10;
        int x = x(position.col()) + 5;
        int y = y(position.row()) + 5;
        g2.setColor(color);
        g2.fillOval(x, y, diameter, diameter);
        g2.setColor(Color.WHITE);
        g2.drawString(label, x + diameter / 2 - 5, y + diameter / 2 + 5);
    }

    private int x(int col) {
        return PADDING + col * CELL_SIZE;
    }

    private int y(int row) {
        return PADDING + row * CELL_SIZE;
    }
}
