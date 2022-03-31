package pl.edu.pwr.lab4.ui;

import pl.edu.pwr.lab4.DirClassLoader;
import pl.edu.pwr.lab4.processing.*;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFrame {
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
    private JFrame mainFrame;
    private String selectedTask;
    private List<Class<Processor>> processors;
    private CustomStatusListener statusListener = new CustomStatusListener();
    private HashMap<Integer, String> status = new HashMap<>();
    DirClassLoader loader;

    UIUtils uiUtils = new UIUtils(mainFrame);

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Simple task manager");
        mainFrame.setContentPane(new MainFrame().mainPanel);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.pack();

    }

    public MainFrame() {

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
                while (processor.getResult() == null){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                updateSelectedProcessor();
            }).start();
        }
        //TODO: Status of one of the processors changed, reload the window with new data.
    }

    private void handleAddNewTask() {
        if (processors == null || processors.isEmpty()) {
            uiUtils.showErrorMessage("Please first add some processors (processor list is empty)");
            return;
        }
        AddNewTaskDialog dialog = new AddNewTaskDialog(processors);
        dialog.pack();
        dialog.setVisible(true);
        if (dialog.isApproved()){
            try {
                Processor toRun = dialog.getSelectedProcessor().getClass().getConstructor().newInstance();
                toRun.submitTask(dialog.getProcessorInput(), statusListener);
                processorListModel.addElement(toRun);
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
        loader = new DirClassLoader("pl.edu.pwr.lab4.processors", dir.toPath());
        try {
            processors = new ArrayList<>(loader.loadProcessorsFromFile());
            uiUtils.showSuccessMessage("Successfully imported "+processors.size()+" classes");

        } catch (IOException ex) {
            uiUtils.showErrorMessage("Bad directory selected.");
        }
    }
}
