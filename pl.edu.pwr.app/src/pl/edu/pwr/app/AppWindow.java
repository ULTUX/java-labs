package pl.edu.pwr.app;

import pl.edu.pwr.dir_hash.Snapshot;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.Vector;

public class AppWindow extends JFrame implements ActionListener {

    //Declare all buttons
    private final JButton takeDirectorySnapshotB = new JButton("Save directory snapshot");
    private final JButton importDirectorySnapshotB = new JButton("Import directory snapshot");
    private final JButton compareB = new JButton("Look for changes");
    private final JButton showFileB = new JButton("Show file");

    //Declare all panels
    private final JPanel mainPanel = new JPanel();
    private final JPanel topPanel = new JPanel();
    private final JPanel botPanel = new JPanel();
    private final JPanel centerPanel = new JPanel();

    //Declare components and base layouts
    private final BorderLayout layout = new BorderLayout(10, 10);

    private final JList<String> pathList = new JList<>();
    private final JLabel pathListLabel = new JLabel("List of modified files");

    private Snapshot importedSnapshot, currentSnapshot;

    public static void main(String[] args) {
        new AppWindow();
    }


    public AppWindow() throws HeadlessException {
        super("Directory changes monitor");
        setSize(new Dimension(400, 500));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);

        //Add action listeners
        takeDirectorySnapshotB.addActionListener(this);
        importDirectorySnapshotB.addActionListener(this);
        compareB.addActionListener(this);
        showFileB.addActionListener(this);

        //Set layouts
        topPanel.setLayout(new GridLayout(0, 2));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        botPanel.setLayout(new GridLayout(0, 2));

        //Set mainPanel props
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setLayout(layout);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(botPanel, BorderLayout.SOUTH);

        //Set visibilities
        topPanel.setVisible(true);
        centerPanel.setVisible(true);
        botPanel.setVisible(true);


        //Set composition panels props
        topPanel.add(takeDirectorySnapshotB);
        topPanel.add(importDirectorySnapshotB);
        centerPanel.add(pathListLabel);

        //Set component props
        pathListLabel.setHorizontalAlignment(SwingConstants.LEFT);
        pathList.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        centerPanel.add(pathList);
        botPanel.add(compareB);
        botPanel.add(showFileB);
        pathList.setEnabled(true);

        //Add mainPanel to JFrame
        add(mainPanel);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
       if (e.getSource() instanceof JComponent) {
           var src = e.getSource();
           if (src.equals(takeDirectorySnapshotB)){
                takeSnapshot();
           }
           else if (src.equals(importDirectorySnapshotB)){
                importSnapshot();
           }
           else if (src.equals(compareB)){
               compareSnapshots();
           }
           else if (src.equals(showFileB)){
                showFile();
           }
       }
    }

    /**
     * Take snapshot of desired directory and save it to file.
     */
    private void takeSnapshot() {
        String dirPath = null;
        var fileChooser = new JFileChooser(FileSystems.getDefault().getPath(".").toFile());
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retOpt = fileChooser.showDialog(this, "Select");
        if (retOpt == JFileChooser.APPROVE_OPTION) {
            dirPath = fileChooser.getSelectedFile().toString();
        }
        if (dirPath != null) {
            Snapshot snap = new Snapshot(dirPath);
            String fileName = LocalDateTime.now().toString().replace(":", "-") + ".snap";
            try {
                Snapshot.saveSnapshotToFile(FileSystems.getDefault().getPath(fileName), snap);
                JOptionPane.showMessageDialog(this, "Successfully created and exported snapshot under the name: " + fileName, "Exported", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Created snapshot");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void importSnapshot() {
        var fileChooser = new JFileChooser(FileSystems.getDefault().getPath(".").toFile());
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        var filter = new FileNameExtensionFilter("Snapshot files", "snap");
        fileChooser.setFileFilter(filter);
        fileChooser.setAcceptAllFileFilterUsed(false);
        int retOpt = fileChooser.showDialog(this, "Select");
        if (retOpt == JFileChooser.APPROVE_OPTION) {
            try {
                importedSnapshot = Snapshot.loadSnapshotFromFile(fileChooser.getSelectedFile().toPath());
                if (currentSnapshot != null && !currentSnapshot.getDirectory().equals(importedSnapshot.getDirectory())){
                    JOptionPane.showMessageDialog(this, "Imported snapshot points to different directory than snapshot you just took", "Error", JOptionPane.ERROR_MESSAGE);
                    importedSnapshot = null;
                    return;
                }
                JOptionPane.showMessageDialog(this, "Successfully imported snapshot of directory: " +
                        importedSnapshot.getDirectory(), "Imported", JOptionPane.INFORMATION_MESSAGE);
                System.out.println("Successfully imported snapshot");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void compareSnapshots() {
        if (importedSnapshot == null){
            JOptionPane.showMessageDialog(this, "You didn't import directory snapshot yet, please do that first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        currentSnapshot = new Snapshot(importedSnapshot.getDirectory());
        DefaultListModel<String> model = new DefaultListModel<>();
        model.addAll(Snapshot.dirCmp(currentSnapshot, importedSnapshot));
        pathList.setModel(model);
        Snapshot.dirCmp(currentSnapshot, importedSnapshot).forEach(System.out::println);
    }

    private void showFile() {
        if (pathList.getSelectedValue() == null){
            JOptionPane.showMessageDialog(this, "Please select a file first", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (Desktop.isDesktopSupported()){
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(FileSystems.getDefault().getPath(pathList.getSelectedValue()).toFile().getParentFile());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "There was an internal error, the snapshot file may be corrupted.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Your environment does not support desktop.", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

}
