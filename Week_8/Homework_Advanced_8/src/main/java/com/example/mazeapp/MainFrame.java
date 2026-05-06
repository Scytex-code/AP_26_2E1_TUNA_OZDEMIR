package com.example.mazeapp;

import com.example.mazeapp.model.Maze;
import com.example.mazeapp.ui.ConfigPanel;
import com.example.mazeapp.ui.ControlPanel;
import com.example.mazeapp.ui.MazeCanvas;
import java.awt.BorderLayout;
import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainFrame extends JFrame {
    private final MazeCanvas mazeCanvas;
    private final ConfigPanel configPanel;
    private Maze maze;
    private SwingWorker<Void, Void> generationWorker;

    public MainFrame() {
        super("Maze Builder - Homework Advanced 8");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mazeCanvas = new MazeCanvas();
        configPanel = new ConfigPanel(this::drawMaze);
        add(configPanel, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(mazeCanvas);
        add(scrollPane, BorderLayout.CENTER);

        ControlPanel controlPanel = new ControlPanel(
                this::createRandomMaze,
                this::generatePerfectMaze,
                this::validateMaze,
                this::exportPng,
                this::saveMaze,
                this::loadMaze,
                this::resetMaze,
                this::dispose
        );
        add(controlPanel, BorderLayout.SOUTH);

        drawMaze(10, 10);
        pack();
        setLocationRelativeTo(null);
    }

    private void drawMaze(int rows, int cols) {
        cancelGeneration();
        maze = new Maze(rows, cols);
        mazeCanvas.setMaze(maze);
        pack();
    }

    private void createRandomMaze() {
        if (!hasMaze()) {
            return;
        }
        cancelGeneration();
        maze.randomizeWalls();
        mazeCanvas.setHighlightedPath(null);
        mazeCanvas.repaint();
    }

    private void generatePerfectMaze() {
        if (!hasMaze()) {
            return;
        }
        cancelGeneration();
        mazeCanvas.setHighlightedPath(null);

        int delay = configPanel.getAnimationDelay();
        generationWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                maze.generatePerfectMaze(changedMaze -> {
                    if (!isCancelled()) {
                        SwingUtilities.invokeLater(mazeCanvas::repaint);
                    }
                }, delay);
                return null;
            }

            @Override
            protected void done() {
                mazeCanvas.repaint();
                if (!isCancelled()) {
                    String result = maze.isPerfectMaze()
                            ? "Generated maze is valid: one connected component and exactly one path between cells."
                            : "Generation finished, but the maze failed the perfect-maze validation.";
                    JOptionPane.showMessageDialog(MainFrame.this, result, "Generation Finished",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };
        generationWorker.execute();
    }

    private void validateMaze() {
        if (!hasMaze()) {
            return;
        }
        Point start = new Point(0, 0);
        Point end = new Point(maze.getRows() - 1, maze.getCols() - 1);
        boolean traversable = maze.isTraversable(start, end);
        mazeCanvas.setHighlightedPath(traversable ? maze.shortestPath(start, end) : null);

        String message = traversable
                ? "The maze is traversable from the top-left cell to the bottom-right cell."
                : "No path exists from the top-left cell to the bottom-right cell.";
        JOptionPane.showMessageDialog(this, message, "Maze Validation",
                traversable ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
    }

    private void exportPng() {
        if (!hasMaze()) {
            return;
        }
        JFileChooser chooser = pngChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = ensureExtension(chooser.getSelectedFile(), ".png");
        try {
            ImageIO.write(mazeCanvas.createImage(), "png", file);
            JOptionPane.showMessageDialog(this, "Maze image exported to:\n" + file.getAbsolutePath(),
                    "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("Could not export the maze image.", exception);
        }
    }

    private void saveMaze() {
        if (!hasMaze()) {
            return;
        }
        JFileChooser chooser = mazeChooser();
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = ensureExtension(chooser.getSelectedFile(), ".maze");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(maze);
            JOptionPane.showMessageDialog(this, "Maze saved to:\n" + file.getAbsolutePath(),
                    "Save Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException exception) {
            showError("Could not save the maze.", exception);
        }
    }

    private void loadMaze() {
        cancelGeneration();
        JFileChooser chooser = mazeChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(chooser.getSelectedFile()))) {
            Object object = in.readObject();
            if (!(object instanceof Maze loadedMaze)) {
                throw new IOException("The selected file does not contain a maze.");
            }
            maze = loadedMaze;
            mazeCanvas.setMaze(maze);
            pack();
            JOptionPane.showMessageDialog(this, "Maze restored successfully.", "Load Complete",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException | ClassNotFoundException exception) {
            showError("Could not load the maze.", exception);
        }
    }

    private void resetMaze() {
        if (!hasMaze()) {
            return;
        }
        cancelGeneration();
        maze.resetWalls();
        mazeCanvas.setHighlightedPath(null);
        mazeCanvas.repaint();
    }

    private void cancelGeneration() {
        if (generationWorker != null && !generationWorker.isDone()) {
            generationWorker.cancel(true);
        }
    }

    private boolean hasMaze() {
        if (maze != null) {
            return true;
        }
        JOptionPane.showMessageDialog(this, "Draw a maze first from the configuration panel.", "No Maze",
                JOptionPane.INFORMATION_MESSAGE);
        return false;
    }

    private JFileChooser pngChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("PNG images (*.png)", "png"));
        chooser.setSelectedFile(new File("maze.png"));
        return chooser;
    }

    private JFileChooser mazeChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Serialized mazes (*.maze)", "maze"));
        chooser.setSelectedFile(new File("maze.maze"));
        return chooser;
    }

    private File ensureExtension(File file, String extension) {
        if (file.getName().toLowerCase().endsWith(extension)) {
            return file;
        }
        return new File(file.getParentFile(), file.getName() + extension);
    }

    private void showError(String message, Exception exception) {
        JOptionPane.showMessageDialog(this, message + "\n" + exception.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
