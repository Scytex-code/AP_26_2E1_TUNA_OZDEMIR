package com.example.mazeapp.ui;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    public ControlPanel(Runnable createAction, Runnable generateAction, Runnable validateAction,
                        Runnable exportAction, Runnable saveAction, Runnable loadAction,
                        Runnable resetAction, Runnable exitAction) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 8, 10));

        addButton("Create", createAction);
        addButton("Generate Perfect", generateAction);
        addButton("Validate", validateAction);
        addButton("Export PNG", exportAction);
        addButton("Save", saveAction);
        addButton("Load", loadAction);
        addButton("Reset", resetAction);
        addButton("Exit", exitAction);
    }

    private void addButton(String text, Runnable action) {
        JButton button = new JButton(text);
        button.addActionListener(e -> action.run());
        add(button);
    }
}
