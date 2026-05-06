package com.example.mazechase.ui;

import com.example.mazechase.GameState;
import com.example.mazechase.SharedMemory;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class GameFrame extends JFrame {
    private final JLabel statusLabel = new JLabel("Game is running.");

    public GameFrame(GameState gameState, SharedMemory sharedMemory) {
        super("Lab 9 Maze Chase");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        GamePanel panel = new GamePanel(gameState, sharedMemory);
        add(new JScrollPane(panel), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        Timer timer = new Timer(120, event -> {
            statusLabel.setText(gameState.finishMessage() + " | visited: " + sharedMemory.visitedCount());
            panel.repaint();
        });
        timer.start();

        pack();
        setLocationRelativeTo(null);
    }
}
