package com.example.mazeapp.ui;

import java.awt.FlowLayout;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class ConfigPanel extends JPanel {
    private final JSpinner rowsSpinner;
    private final JSpinner colsSpinner;
    private final JSlider speedSlider;

    public ConfigPanel(BiConsumer<Integer, Integer> drawAction) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        add(new JLabel("Rows:"));
        rowsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 50, 1));
        add(rowsSpinner);

        add(new JLabel("Columns:"));
        colsSpinner = new JSpinner(new SpinnerNumberModel(10, 2, 50, 1));
        add(colsSpinner);

        JButton drawButton = new JButton("Draw Cells");
        drawButton.addActionListener(e -> drawAction.accept(getRowsValue(), getColsValue()));
        add(drawButton);

        add(new JLabel("Animation speed:"));
        speedSlider = new JSlider(0, 200, 35);
        speedSlider.setMajorTickSpacing(100);
        speedSlider.setPaintTicks(true);
        speedSlider.setToolTipText("Delay in milliseconds between generation steps");
        add(speedSlider);
    }

    public int getRowsValue() {
        return (Integer) rowsSpinner.getValue();
    }

    public int getColsValue() {
        return (Integer) colsSpinner.getValue();
    }

    public int getAnimationDelay() {
        return speedSlider.getValue();
    }
}
