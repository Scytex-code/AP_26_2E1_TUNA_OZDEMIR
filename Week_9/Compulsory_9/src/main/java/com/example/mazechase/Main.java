package com.example.mazechase;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Maze maze = new Maze(8, 12);
        GameState gameState = new GameState(maze);
        SharedMemory sharedMemory = new SharedMemory();

        List<Thread> threads = new ArrayList<>();
        threads.add(new Thread(new Bunny(gameState, sharedMemory), "Bunny"));
        for (int i = 1; i <= 3; i++) {
            threads.add(new Thread(new Robot("Robot-" + i, gameState, sharedMemory), "Robot-" + i));
        }
        threads.forEach(Thread::start);

        while (!gameState.isFinished()) {
            System.out.println(gameState.render());
            Thread.sleep(700);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println(gameState.render());
        System.out.println(gameState.finishMessage());
    }
}
