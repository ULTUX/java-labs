package pl.edu.pwr.lab12;

import com.formdev.flatlaf.FlatDarkLaf;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
    private JButton clearCanvasButton;
    private JButton unloadScriptButton;
    private JButton importFromFileButton;
    private JButton exportToFileButton;

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
        setResizable(false);

        startButton.addActionListener(e -> handleStart());
        stopButton.addActionListener(e -> handleStop());
        reloadFilesButton.addActionListener(e -> findJsFiles());
        clearCanvasButton.addActionListener(e -> canvas.clearCanvas());
        unloadScriptButton.addActionListener(e -> handleUnloadScript());
        exportToFileButton.addActionListener(e -> handleExport());
        importFromFileButton.addActionListener(e -> handleImport());

        findJsFiles();
        var size = canvas.getDataSize();
        var data = new int[size.width][size.height];
        canvas.setData(data);


    }

    private void handleImport() {
        var fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("Cellular automata snapshot", "cas"));
        var res = fileChooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            var file = fileChooser.getSelectedFile();
            try {
                var fileIn = new ObjectInputStream(new FileInputStream(file));
                var data = (int[][]) fileIn.readObject();
                canvas.setData(data);
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, "Could not load file, it may be corrupted", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }

        }
    }

    private void handleExport() {
        var data = canvas.getData();
        try {
            var fileName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replace(":","_").replace("+", "_");
            var fileOut = new ObjectOutputStream(new FileOutputStream(fileName + ".cas"));
            fileOut.writeObject(data);
            JOptionPane.showMessageDialog(this, "Succesfully exported snapshot to file: " + fileName, "Export", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Could not save the file.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void handleUnloadScript() {
        if (isSimRunning) return;
        this.loader = null;
        unloadScriptButton.setEnabled(false);
        System.gc();
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
        unloadScriptButton.setEnabled(false);
    }

    private void handleStop() {
        isSimRunning = false;
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        scriptSelector.setEnabled(true);
        unloadScriptButton.setEnabled(true);
    }

    private void startSimulation() {
        try {
            var selectedItem = scriptSelector.getSelectedItem();
            if (selectedItem == null || selectedItem.equals("")) return;
            loader = new JavascriptLoader((String) selectedItem);
            new Thread(() -> {
                while (isSimRunning) {
                    try {
                        var currGeneration = canvas.getData();
                        int[][] nextGen;
                        nextGen = loader.nextGeneration(currGeneration);
                        canvas.setData(nextGen);
                        Thread.sleep(Integer.toUnsignedLong((Integer) simulationSpeed.getValue()));
                    } catch (InterruptedException ignored) {
                        Thread.currentThread().interrupt();
                        return;
                    } catch (ScriptException | NoSuchMethodException e) {
                        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Could not continue simulation, error unknown", "Error", JOptionPane.ERROR_MESSAGE));
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
        FlatDarkLaf.setup();
        new MainFrame();
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(4, 6, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        canvasContainer = new JPanel();
        canvasContainer.setLayout(new CardLayout(0, 0));
        canvasContainer.setEnabled(true);
        mainPanel.add(canvasContainer, new GridConstraints(0, 0, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        scriptSelector = new JComboBox();
        mainPanel.add(scriptSelector, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Load JS file");
        mainPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Simulation speed (ms)");
        mainPanel.add(label2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        simulationSpeed = new JSpinner();
        mainPanel.add(simulationSpeed, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        unloadScriptButton = new JButton();
        unloadScriptButton.setEnabled(false);
        unloadScriptButton.setText("Unload Script");
        mainPanel.add(unloadScriptButton, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadFilesButton = new JButton();
        reloadFilesButton.setText("Reload files");
        mainPanel.add(reloadFilesButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setEnabled(false);
        stopButton.setText("Stop");
        mainPanel.add(stopButton, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearCanvasButton = new JButton();
        clearCanvasButton.setText("Clear canvas");
        mainPanel.add(clearCanvasButton, new GridConstraints(3, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        exportToFileButton = new JButton();
        exportToFileButton.setText("Export to file");
        mainPanel.add(exportToFileButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        importFromFileButton = new JButton();
        importFromFileButton.setText("Import from file");
        mainPanel.add(importFromFileButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        startButton = new JButton();
        startButton.setEnabled(false);
        startButton.setText("Start");
        mainPanel.add(startButton, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        label2.setLabelFor(simulationSpeed);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
