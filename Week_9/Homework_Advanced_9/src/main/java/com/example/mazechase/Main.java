package com.example.mazechase;

import com.example.mazechase.actor.ActorControl;
import com.example.mazechase.actor.Bunny;
import com.example.mazechase.actor.Robot;
import com.example.mazechase.model.Maze;
import com.example.mazechase.ui.GameFrame;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        setSystemLookAndFeel();

        Maze maze = new Maze(12, 18);
        SharedMemory sharedMemory = new SharedMemory();
        GameState gameState = new GameState(maze, sharedMemory);
        List<Thread> actors = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            ActorControl control = new ActorControl(380);
            actors.add(new Thread(new Bunny("Bunny-" + i, gameState, sharedMemory, control), "Bunny-" + i));
        }
        for (int i = 1; i <= 5; i++) {
            ActorControl control = new ActorControl(260);
            actors.add(new Thread(new Robot("Robot-" + i, gameState, sharedMemory, control), "Robot-" + i));
        }

        actors.forEach(Thread::start);
        new ManagerThread(gameState, sharedMemory, 90).start();
        new CommandThread(gameState).start();

        SwingUtilities.invokeLater(() -> new GameFrame(gameState, sharedMemory).setVisible(true));
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Use default look and feel when the system one is not available.
        }
    }
}
