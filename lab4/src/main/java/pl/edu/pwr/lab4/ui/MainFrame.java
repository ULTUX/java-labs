package pl.edu.pwr.lab4.ui;

import pl.edu.pwr.lab4.ProcessClassLoader;
import pl.edu.pwr.lab4.processing.CustomStatusListener;
import pl.edu.pwr.lab4.processing.Processor;
import pl.edu.pwr.lab4.processing.Status;
import pl.edu.pwr.lab4.processing.TaskIdDistributor;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
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

        statusListener.addEventHandler(this::stateUpdated);

        reloadProcessorClassesButton.addActionListener(e -> handleReloadClasses());
        addNewTaskButton.addActionListener(e -> handleAddNewTask());

    }

    private void stateUpdated(Status s) {
        System.out.println("UI RELOAD REQUESTED");
        if (s.getProgress() == 100) {
            try {
                Thread.sleep(100);
                JOptionPane.showMessageDialog(mainFrame, "Progress finished\nResult"+ TaskIdDistributor.getInstance().getProcessorWithId(s.getTaskId()).getResult(), "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //TODO: Status of one of the processors changed, reload the window with new data.
    }

    private void handleAddNewTask() {
        if (processors == null || processors.isEmpty()) {
            uiUtils.showErrorMessage("Please first add some processors (processor list is empty)");
            return;
        }
        AddNewTaskDialog dialog = new AddNewTaskDialog(processors);
        dialog.setVisible(true);
        if (dialog.isApproved()){
            try {
                Processor toRun = dialog.getSelectedProcessor().getClass().getConstructor().newInstance();
                toRun.submitTask(dialog.getProcessorInput(), statusListener);
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
