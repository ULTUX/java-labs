package pl.edu.pwr.lab5.client.ui;

import pl.edu.pwr.lab5.api.AnalysisService;
import pl.edu.pwr.lab5.api.DataSet;
import pl.edu.pwr.lab5.client.ServiceRunner;
import pl.edu.pwr.lab5.client.io.CsvFileReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ServiceLoader;
import java.util.function.Consumer;

public class MainFrame extends JFrame {
    private JButton loadDataButton;
    private JComboBox<AnalysisServiceItem> analysisSelector;
    private final DefaultComboBoxModel<AnalysisServiceItem> analysisSelectorModel;
    private JTable inputTable;
    private JPanel mainPanel;
    private JButton startCalculationsButton;
    private final transient ServiceLoader<AnalysisService> analysisServices;
    private transient CsvFileReader readFile;
    private ServiceRunner runner = new ServiceRunner();

    public MainFrame() {
        super("Statistic analysis tool");
        this.setContentPane(this.mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.pack();

        analysisSelectorModel = new DefaultComboBoxModel<>();
        analysisServices = ServiceLoader.load(AnalysisService.class);
        analysisSelector.setModel(analysisSelectorModel);
        analysisServices.forEach(analysisService -> analysisSelectorModel.addElement(new AnalysisServiceItem(analysisService)));
        loadDataButton.addActionListener(e -> handleLoadData());
        startCalculationsButton.addActionListener(e -> handleStartCalc());
        runner.addListener(calculationListener);
    }

    private final Consumer<Object> calculationListener = o -> {
        if (o instanceof Exception){
            showErrorMessage("Could not compute, error: "+((Exception) o).getMessage());
            return;
        }
        var dataSet = (DataSet) o;
        ResultDialog dialog = new ResultDialog(dataSet);
        dialog.pack();
        dialog.setVisible(true);
    };

    private void handleStartCalc() {
        if (readFile == null) return;
        AnalysisService service = ((AnalysisServiceItem) analysisSelector.getSelectedItem()).service;
        var dataSet = new DataSet();
        dataSet.setData(readFile.getData());
        dataSet.setHeader(readFile.getHeaders());
        runner.setService(service);
        runner.setInputData(dataSet);
        runner.startCalc();

    }

    private void handleLoadData() {
        var fChooser = new JFileChooser();
        fChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fChooser.setFileFilter(new FileNameExtensionFilter("Comma-separated files (.csv)", "csv"));
        fChooser.setAcceptAllFileFilterUsed(false);
        fChooser.setMultiSelectionEnabled(false);

        int result = fChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fChooser.getSelectedFile();
            var fileReader = new CsvFileReader(selectedFile);
            try {
                fileReader.readFile();
                var tableModel = new DefaultTableModel(fileReader.getData(), fileReader.getHeaders());
                inputTable.setModel(tableModel);
                readFile = fileReader;
                startCalculationsButton.setEnabled(true);
            } catch (FileNotFoundException e) {
                showErrorMessage("Could not open selected file.");
            }

        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void showSuccessMessage(String message){
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static class AnalysisServiceItem {
        private final AnalysisService service;

        private AnalysisServiceItem(AnalysisService service) {
            this.service = service;
        }

        @Override
        public String toString() {
            return service.getName();
        }

        public AnalysisService getService() {
            return service;
        }
    }
}
