package pl.edu.pwr.lab4.ui;

import pl.edu.pwr.lab4.DirClassLoader;
import pl.edu.pwr.lab4.processing.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFrame extends JFrame {
    private JLabel taskIdLabel;
    private JLabel taskStateLabel;
    private JLabel taskResultLabel;
    private JButton addNewTaskButton;
    private JButton reloadProcessorClassesButton;
    public JPanel mainPanel;
    private JList<Processor> currentProcessorList;
    private JButton unloadClassesButton;
    private JLabel processorInfoLabel;
    private final DefaultListModel<Processor> processorListModel = new DefaultListModel<>();
    private static JFrame mainFrame;
    private List<Class<Processor>> processors;
    private CustomStatusListener statusListener = new CustomStatusListener();
    private HashMap<Integer, String> status = new HashMap<>();
    DirClassLoader loader;

    UIUtils uiUtils = new UIUtils(mainFrame);

    public static void main(String[] args) {
        new MainFrame();
    }

    public MainFrame() {
        super("Simple task manager");
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
        statusListener.addEventHandler(this::stateUpdated);

        reloadProcessorClassesButton.addActionListener(e -> handleReloadClasses());
        addNewTaskButton.addActionListener(e -> handleAddNewTask());

        currentProcessorList.setModel(processorListModel);
        currentProcessorList.addListSelectionListener(e -> handleListSelectionChanged());

        unloadClassesButton.addActionListener(e -> {
            processors.clear();
            processorListModel.clear();
            TaskIdDistributor.getInstance().clearRunningProcessors();
            statusListener.clearStatusMap();
            loader = null;
            System.gc();
            uiUtils.showSuccessMessage("Removed all references to all processors and performed gc run.");
        });

    }

    private void updateSelectedProcessor() {
        Processor selectedProcessor = currentProcessorList.getSelectedValue();
        if (selectedProcessor == null) return;
        int id = TaskIdDistributor.getInstance().getId(selectedProcessor);
        taskIdLabel.setText(String.valueOf(id));
        taskStateLabel.setText(status.get(id));
        processorInfoLabel.setText(selectedProcessor.getInfo());
        if (selectedProcessor.getResult() != null) taskResultLabel.setText(selectedProcessor.getResult());
        else taskResultLabel.setText("no data");
    }

    private void handleListSelectionChanged() {
        updateSelectedProcessor();
    }

    private void stateUpdated(Status s) {
        Processor processor = TaskIdDistributor.getInstance().getProcessorWithId(s.getTaskId());
        status.put(s.getTaskId(), String.valueOf(s.getProgress()));
        updateSelectedProcessor();
        if (s.getProgress() == 100) {
            new Thread(() -> {
                while (processor.getResult() == null) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                updateSelectedProcessor();
            }).start();
        }
    }

    private void handleAddNewTask() {
        if (processors == null || processors.isEmpty()) {
            uiUtils.showErrorMessage("Please first add some processors (processor list is empty)");
            return;
        }
        AddNewTaskDialog dialog = new AddNewTaskDialog(processors);
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.isApproved()) {
            try {
                Processor toRun = dialog.getSelectedProcessor().getClass().getConstructor().newInstance();
                toRun.submitTask(dialog.getProcessorInput(), statusListener);
                processorListModel.addElement(toRun);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                uiUtils.showErrorMessage("Something impossible just happened (this probably means app bug).");
            }
        }
    }

    private void handleReloadClasses() {
        JFileChooser fileChooser = new JFileChooser(FileSystems.getDefault().getPath("").toAbsolutePath().toString());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Pick class files containing dir");
        fileChooser.setToolTipText("Please select directory that contains processor classes.");
        int result = fileChooser.showOpenDialog(mainFrame);
        if (result != JFileChooser.APPROVE_OPTION) return;
        File dir = fileChooser.getSelectedFile();
        loader = new DirClassLoader("pl.edu.pwr.lab4.processors", dir.toPath());
        try {
            processors = new ArrayList<>(loader.loadProcessorsFromFile());
            uiUtils.showSuccessMessage("Successfully imported " + processors.size() + " classes");

        } catch (IOException ex) {
            uiUtils.showErrorMessage("Bad directory selected.");
        }
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
        mainPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 4, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Task list", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        currentProcessorList = new JList();
        scrollPane1.setViewportView(currentProcessorList);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(null, "Task info", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Task ID");
        panel1.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Status");
        panel1.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Result");
        panel1.add(label3, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        taskIdLabel = new JLabel();
        taskIdLabel.setText("no data");
        panel1.add(taskIdLabel, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        taskStateLabel = new JLabel();
        taskStateLabel.setText("no data");
        panel1.add(taskStateLabel, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        taskResultLabel = new JLabel();
        taskResultLabel.setText("no data");
        panel1.add(taskResultLabel, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        processorInfoLabel = new JLabel();
        processorInfoLabel.setText("no processor info");
        panel1.add(processorInfoLabel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reloadProcessorClassesButton = new JButton();
        reloadProcessorClassesButton.setText("Reload processor classes");
        mainPanel.add(reloadProcessorClassesButton, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addNewTaskButton = new JButton();
        addNewTaskButton.setText("Add new task");
        mainPanel.add(addNewTaskButton, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        mainPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_VERTICAL, 1, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        unloadClassesButton = new JButton();
        unloadClassesButton.setText("Unload all classes");
        mainPanel.add(unloadClassesButton, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}
