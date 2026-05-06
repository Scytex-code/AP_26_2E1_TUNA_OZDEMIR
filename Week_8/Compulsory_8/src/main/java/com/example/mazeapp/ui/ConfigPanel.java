package com.example.mazeapp.ui;

import java.awt.FlowLayout;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ConfigPanel extends JPanel {
    private final JSpinner rowsSpinner;
    private final JSpinner colsSpinner;
    private final JButton drawButton;

    public ConfigPanel(BiConsumer<Integer, Integer> drawAction) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        add(new JLabel("Rows:"));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 50, 1));
        add(rowsSpinner);

        add(new JLabel("Columns:"));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 50, 1));
        add(colsSpinner);

        drawButton = new JButton("Draw Maze");
        drawButton.addActionListener(e -> drawAction.accept(getRowsValue(), getColsValue()));
        add(drawButton);
    }

    public int getRowsValue() {
        return (Integer) rowsSpinner.getValue();
    }

    public int getColsValue() {
        return (Integer) colsSpinner.getValue();
    }
}
