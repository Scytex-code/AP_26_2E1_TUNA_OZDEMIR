package com.example.mazeapp.ui;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    public ControlPanel(Runnable createAction, Runnable resetAction, Runnable exitAction) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 10));

        JButton createButton = new JButton("Create");
        createButton.addActionListener(e -> createAction.run());
        add(createButton);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(e -> resetAction.run());
        add(resetButton);

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> exitAction.run());
        add(exitButton);
    }
}
