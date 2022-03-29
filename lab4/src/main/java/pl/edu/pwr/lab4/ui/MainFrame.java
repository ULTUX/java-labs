package pl.edu.pwr.lab4.ui;

import pl.edu.pwr.lab4.ProcessClassLoader;
import pl.edu.pwr.lab4.processing.CustomStatusListener;
import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processors.MyProcessor;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.List;

public class MainFrame {
    private JTextPane taskNotifications;
    private JLabel taskIdLabel;
    private JLabel taskStateLabel;
    private JLabel taskResultLabel;
    private JButton addNewTaskButton;
    private JButton reloadProcessorClassesButton;
    private JPanel mainPanel;
    private JFrame mainFrame;
    private String selectedTask;
    private List<Processor> processors;
    private List<Processor> runningProcessors;

    private CustomStatusListener statusListener = new CustomStatusListener();

    UIUtils uiUtils = new UIUtils(mainFrame);

    public static void main(String[] args) {
        new MainFrame();
    }

    public MainFrame() {
        mainFrame = new JFrame("Simple task manager");
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.pack();

        statusListener.addEventHandler(() -> reloadWindow());

        reloadProcessorClassesButton.addActionListener(e -> handleReloadClasses());
        addNewTaskButton.addActionListener(e -> handleAddNewTask());

    }

    private void reloadWindow() {
        //TODO: Status of one of the processors changed, reload the window with new data.
    }

    private void handleAddNewTask() {
        //TODO: DELETE IT (DEBUG ONLY)
        processors = new ArrayList<>();
        processors.add(new MyProcessor());
        if (processors == null || processors.isEmpty()) {
            uiUtils.showErrorMessage("Please first add some processors (processor list is empty)");
            return;
        }
        AddNewTaskDialog dialog = new AddNewTaskDialog(processors);
        dialog.setVisible(true);
        if (dialog.isApproved()){
            //TODO: HANDLE TASK ADDED EVENT
            try {
                Processor toRun = dialog.getSelectedProcessor().getClass().getConstructor().newInstance();
                toRun.submitTask(dialog.getProcessorInput(), statusListener);
                runningProcessors.add(toRun);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
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
        ProcessClassLoader loader = new ProcessClassLoader(dir.toPath(), "pl.edu.pwr.lab4.processors");
        try {
            processors = loader.getClasses();
            uiUtils.showSuccessMessage("Successfully imported "+processors.size()+" classes");

        } catch (IOException ex) {
            uiUtils.showErrorMessage("Bad directory selected.");
        }
    }
}
