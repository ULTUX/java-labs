package pl.edu.pwr.userdata;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystems;
import java.util.ArrayList;

public class MainFrame {
    private JList<UserData> userDataList;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JFormattedTextField firstNameField;
    private JFormattedTextField lastNameField;
    private JFormattedTextField ageField;
    private JLabel imgLabel;
    private JPanel mainPanel;
    private JButton loadUserDataButton;
    private JLabel isLoadedIndicator;

    ArrayList<WeakReference<UserData>> loadedUserData = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("MainFrame");
        frame.setContentPane(new MainFrame().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MainFrame() {
        ImageIcon icon = new ImageIcon("C:\\Users\\ULTUX\\IdeaProjects\\lab1\\lab2\\src\\image.jpg");
        Image img = icon.getImage();
        ImageIcon newIcon = new ImageIcon(img.getScaledInstance(150, 150, Image.SCALE_SMOOTH));
        imgLabel.setIcon(newIcon);
        imgLabel.setText("");

        loadUserDataButton.addActionListener(e -> {
            JFileChooser fChooser = new JFileChooser();
            fChooser.setAcceptAllFileFilterUsed(false);
            fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retState = fChooser.showOpenDialog(mainPanel);
            if (retState == JFileChooser.APPROVE_OPTION) {

            }
        });
    }
}
