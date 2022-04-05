package pl.edu.pwr.lab5.client.ui;

import pl.edu.pwr.lab5.api.AnalysisService;
import pl.edu.pwr.lab5.client.io.CsvFileReader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ServiceLoader;

public class MainFrame extends JFrame {
    private JButton loadDataButton;
    private JComboBox<AnalysisServiceItem> analysisSelector;
    private DefaultComboBoxModel<AnalysisServiceItem> analysisSelectorModel;
    private JTable inputTable;
    private JPanel mainPanel;
    private JButton startCalculationsButton;
    ServiceLoader<AnalysisService> analysisServices;

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
