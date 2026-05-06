package com.example.mazechase;

import com.example.mazechase.actor.ActorControl;
import java.util.Map;
import java.util.Scanner;

public class CommandThread extends Thread {
    private final GameState gameState;

    public CommandThread(GameState gameState) {
        super("Command");
        this.gameState = gameState;
        setDaemon(true);
    }

    @Override
    public void run() {
        printHelp();
        Scanner scanner = new Scanner(System.in);
        while (!gameState.isFinished()) {
            if (!scanner.hasNextLine()) {
                return;
            }
            handle(scanner.nextLine().trim());
        }
    }

    private void handle(String line) {
        if (line.isBlank()) {
            return;
        }
        String[] parts = line.split("\\s+");
        String command = parts[0].toLowerCase();
        if ("help".equals(command)) {
            printHelp();
            return;
        }
        if ("quit".equals(command)) {
            gameState.finish("Game stopped by command.");
            return;
        }
        if (parts.length < 2) {
            System.out.println("Missing target. Use all, robots, bunnies, or an actor name.");
            return;
        }

        Map<String, ActorControl> controls = gameState.controlsSnapshot();
        switch (command) {
            case "pause" -> selectedControls(parts[1], controls).forEach(ActorControl::pause);
            case "resume" -> selectedControls(parts[1], controls).forEach(ActorControl::resume);
            case "stop" -> selectedControls(parts[1], controls).forEach(ActorControl::stop);
            case "speed" -> changeSpeed(parts, controls);
            default -> System.out.println("Unknown command. Type help.");
        }
    }

    private void changeSpeed(String[] parts, Map<String, ActorControl> controls) {
        if (parts.length < 3) {
            System.out.println("Usage: speed <target> <milliseconds>");
            return;
        }
        try {
            int delay = Integer.parseInt(parts[2]);
            selectedControls(parts[1], controls).forEach(control -> control.setDelayMillis(delay));
        } catch (NumberFormatException exception) {
            System.out.println("Speed must be a number.");
        }
    }

    private java.util.List<ActorControl> selectedControls(String target, Map<String, ActorControl> controls) {
        String normalized = target.toLowerCase();
        if ("all".equals(normalized)) {
            return controls.values().stream().toList();
        }
        if ("robots".equals(normalized)) {
            return controls.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("Robot"))
                    .map(Map.Entry::getValue)
                    .toList();
        }
        if ("bunnies".equals(normalized)) {
            return controls.entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("Bunny"))
                    .map(Map.Entry::getValue)
                    .toList();
        }
        ActorControl control = controls.get(target);
        if (control == null) {
            System.out.println("No actor named " + target + ".");
            return java.util.List.of();
        }
        return java.util.List.of(control);
    }

    private void printHelp() {
        System.out.println("""
                Commands:
                  speed all 200
                  speed robots 120
                  speed Bunny-1 500
                  pause all | pause robots | pause Bunny-1
                  resume all | resume robots | resume Bunny-1
                  stop Robot-1
                  quit
                """);
    }
}
