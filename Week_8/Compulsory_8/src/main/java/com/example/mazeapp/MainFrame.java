package com.example.mazeapp;

import com.example.mazeapp.model.Maze;
import com.example.mazeapp.ui.ConfigPanel;
import com.example.mazeapp.ui.ControlPanel;
import com.example.mazeapp.ui.MazeCanvas;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class MainFrame extends JFrame {
    private final MazeCanvas mazeCanvas;
    private Maze maze;

    public MainFrame() {
        super("Maze Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mazeCanvas = new MazeCanvas();

        ConfigPanel configPanel = new ConfigPanel(this::drawMaze);
        add(configPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(mazeCanvas);
        add(scrollPane, BorderLayout.CENTER);

        ControlPanel controlPanel = new ControlPanel(this::createRandomMaze, this::resetMaze, this::dispose);
        add(controlPanel, BorderLayout.SOUTH);

        drawMaze(10, 10);

        pack();
        setLocationRelativeTo(null);
    }

    private void drawMaze(int rows, int cols) {
        maze = new Maze(rows, cols);
        mazeCanvas.setMaze(maze);
        pack();
    }

    private void createRandomMaze() {
        if (maze == null) {
            showMissingMazeMessage();
            return;
        }
        maze.randomizeWalls();
        mazeCanvas.repaint();
    }

    private void resetMaze() {
        if (maze == null) {
            showMissingMazeMessage();
            return;
        }
        maze.resetWalls();
        mazeCanvas.repaint();
    }

    private void showMissingMazeMessage() {
        JOptionPane.showMessageDialog(this, "Draw a maze first from the configuration panel.", "No Maze",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
