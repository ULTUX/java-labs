package pl.edu.pwr.lab12;

import javax.script.ScriptException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.FileSystems;
import java.util.stream.Stream;

public class MainFrame extends JFrame {
    private JButton startButton;
    private JButton stopButton;
    private JComboBox<String> scriptSelector;
    private final DefaultComboBoxModel<String> scriptSelectorModel = new DefaultComboBoxModel<>();
    private JButton reloadFilesButton;
    private JPanel canvasContainer;
    private JPanel mainPanel;
    private JSpinner simulationSpeed;

    private final Canvas canvas;

    private final Dimension CANVAS_SIZE = new Dimension(800, 600);
    private final Dimension RESOLUTION = new Dimension(16, 16);

    private JavascriptLoader loader;

    private boolean isSimRunning = false;


    public MainFrame() {
        super("");
        canvasContainer.setPreferredSize(new Dimension(CANVAS_SIZE.width + 1, CANVAS_SIZE.height + 1));
        canvasContainer.setMaximumSize(new Dimension(CANVAS_SIZE.width + 1, CANVAS_SIZE.height + 1));
        canvas = new Canvas(RESOLUTION);
        canvas.setSize(CANVAS_SIZE);
        canvasContainer.add(canvas);
        simulationSpeed.setValue(100);

        scriptSelector.setModel(scriptSelectorModel);
        scriptSelector.addActionListener(e -> handleSelectionChanged());

        setContentPane(mainPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);

        startButton.addActionListener(e -> handleStart());
        stopButton.addActionListener(e -> handleStop());
        reloadFilesButton.addActionListener(e -> findJsFiles());

        findJsFiles();
        var size = canvas.getDataSize();
        var data = new int[size.width][size.height];
        for (int i = 0; i < size.width; i++) {
            for (int j = 0; j < size.height; j++) {
                data[i][j] = 1;
            }
        }
        canvas.setData(data);


    }

    private void handleSelectionChanged() {
        var selectedItem = scriptSelector.getSelectedItem();
        if (selectedItem == null || selectedItem.equals("")) return;
        startButton.setEnabled(true);
    }

    private void findJsFiles() {
        scriptSelectorModel.removeAllElements();
        var fileList = FileSystems.getDefault().getPath(".").toFile().listFiles();
        if (fileList == null) return;
        Stream.of(fileList)
                .filter(file -> !file.isDirectory() && file.getName().matches("^.*\\.js$"))
                .map(File::getName)
                .forEach(scriptSelectorModel::addElement);
    }

    private void handleStart() {
        if (isSimRunning) return;
        isSimRunning = true;
        startSimulation();
        startButton.setEnabled(false);
        stopButton.setEnabled(true);
        scriptSelector.setEnabled(false);
    }

    private void handleStop() {
       isSimRunning = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        scriptSelector.setEnabled(true);
    }

    private void startSimulation() {
        try {
            var selectedItem = scriptSelector.getSelectedItem();
            if (selectedItem == null || selectedItem.equals("")) return;
            loader = new JavascriptLoader((String) selectedItem);
            new Thread(() -> {
                while (isSimRunning) {
                    var currGeneration = canvas.getData();
                    int[][] nextGen;
                    try {
                        nextGen = loader.nextGeneration(currGeneration);
                        canvas.setData(nextGen);
                    } catch (ScriptException | NoSuchMethodException e) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Could not continue simulation, error unknown", "Error", JOptionPane.ERROR_MESSAGE));

                    }
                    try {
                        Thread.sleep(Integer.toUnsignedLong((Integer) simulationSpeed.getValue()));
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Simulation thead finished");
            }).start();
        } catch (FileNotFoundException | NoSuchMethodException | ScriptException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) {
        new MainFrame();
    }


}
