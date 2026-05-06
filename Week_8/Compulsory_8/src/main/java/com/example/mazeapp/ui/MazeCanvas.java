package com.example.mazeapp.ui;

import com.example.mazeapp.model.Cell;
import com.example.mazeapp.model.Maze;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class MazeCanvas extends JPanel {
    private static final int CELL_SIZE = 30;
    private static final int PADDING = 20;

    private Maze maze;

    public MazeCanvas() {
        setBackground(Color.WHITE);
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        if (maze == null) {
            return new Dimension(500, 500);
        }

        int width = maze.getCols() * CELL_SIZE + PADDING * 2;
        int height = maze.getRows() * CELL_SIZE + PADDING * 2;
        return new Dimension(width, height);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (maze == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2f));

        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                drawCell(g2, maze.getCell(row, col));
            }
        }
        g2.dispose();
    }

    private void drawCell(Graphics2D g2, Cell cell) {
        int x = PADDING + cell.getCol() * CELL_SIZE;
        int y = PADDING + cell.getRow() * CELL_SIZE;

        g2.setColor(new Color(196, 224, 255));
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        g2.setColor(new Color(24, 67, 122));
        if (cell.hasTopWall()) {
            g2.drawLine(x, y, x + CELL_SIZE, y);
        }
        if (cell.hasRightWall()) {
            g2.drawLine(x + CELL_SIZE, y, x + CELL_SIZE, y + CELL_SIZE);
        }
        if (cell.hasBottomWall()) {
            g2.drawLine(x, y + CELL_SIZE, x + CELL_SIZE, y + CELL_SIZE);
        }
        if (cell.hasLeftWall()) {
            g2.drawLine(x, y, x, y + CELL_SIZE);
        }
    }
}
