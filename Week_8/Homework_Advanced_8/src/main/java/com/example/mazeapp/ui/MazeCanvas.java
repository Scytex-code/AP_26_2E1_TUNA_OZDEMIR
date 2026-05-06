package com.example.mazeapp.ui;

import com.example.mazeapp.model.Cell;
import com.example.mazeapp.model.Maze;
import com.example.mazeapp.model.Wall;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;

public class MazeCanvas extends JPanel {
    private static final int CELL_SIZE = 32;
    private static final int PADDING = 22;
    private static final int WALL_HIT_TOLERANCE = 8;

    private final Color cellColor = new Color(219, 238, 242);
    private final Color pathColor = new Color(255, 215, 122);
    private final Color wallColor = new Color(35, 58, 66);
    private final Color startColor = new Color(50, 151, 106);
    private final Color endColor = new Color(203, 75, 75);

    private Maze maze;
    private Set<Point> highlightedPath = new HashSet<>();

    public MazeCanvas() {
        setBackground(Color.WHITE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                toggleWallAt(event.getPoint());
            }
        });
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        highlightedPath = new HashSet<>();
        revalidate();
        repaint();
    }

    public void setHighlightedPath(Set<Point> highlightedPath) {
        this.highlightedPath = highlightedPath == null ? new HashSet<>() : highlightedPath;
        repaint();
    }

    public BufferedImage createImage() {
        Dimension size = getPreferredSize();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        setSize(size);
        paint(graphics);
        graphics.dispose();
        return image;
    }

    @Override
    public Dimension getPreferredSize() {
        if (maze == null) {
            return new Dimension(560, 520);
        }
        return new Dimension(maze.getCols() * CELL_SIZE + PADDING * 2,
                maze.getRows() * CELL_SIZE + PADDING * 2);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (maze == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                drawCell(g2, maze.getCell(row, col));
            }
        }
        drawEndpoint(g2, 0, 0, startColor);
        drawEndpoint(g2, maze.getRows() - 1, maze.getCols() - 1, endColor);
        g2.dispose();
    }

    private void drawCell(Graphics2D g2, Cell cell) {
        int x = xForColumn(cell.getCol());
        int y = yForRow(cell.getRow());
        Color fill = highlightedPath.contains(new Point(cell.getRow(), cell.getCol())) ? pathColor : cellColor;

        g2.setColor(fill);
        g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);

        g2.setColor(wallColor);
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

    private void drawEndpoint(Graphics2D g2, int row, int col, Color color) {
        int diameter = Math.max(10, CELL_SIZE / 3);
        int x = xForColumn(col) + (CELL_SIZE - diameter) / 2;
        int y = yForRow(row) + (CELL_SIZE - diameter) / 2;
        g2.setColor(color);
        g2.fillOval(x, y, diameter, diameter);
    }

    private void toggleWallAt(Point point) {
        if (maze == null) {
            return;
        }

        int col = (point.x - PADDING) / CELL_SIZE;
        int row = (point.y - PADDING) / CELL_SIZE;
        if (row < 0 || row >= maze.getRows() || col < 0 || col >= maze.getCols()) {
            return;
        }

        int localX = point.x - xForColumn(col);
        int localY = point.y - yForRow(row);
        Wall wall = closestWall(localX, localY);
        if (wall == null) {
            return;
        }
        maze.toggleWall(row, col, wall);
        highlightedPath = new HashSet<>();
        repaint();
    }

    private Wall closestWall(int localX, int localY) {
        int top = localY;
        int left = localX;
        int right = CELL_SIZE - localX;
        int bottom = CELL_SIZE - localY;
        int min = Math.min(Math.min(top, bottom), Math.min(left, right));

        if (min > WALL_HIT_TOLERANCE) {
            return null;
        }
        if (min == top) {
            return Wall.TOP;
        }
        if (min == right) {
            return Wall.RIGHT;
        }
        if (min == bottom) {
            return Wall.BOTTOM;
        }
        return Wall.LEFT;
    }

    private int xForColumn(int col) {
        return PADDING + col * CELL_SIZE;
    }

    private int yForRow(int row) {
        return PADDING + row * CELL_SIZE;
    }
}
