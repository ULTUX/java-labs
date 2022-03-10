package pl.edu.pwr.userdata;

import javax.swing.*;
import java.awt.*;
import java.lang.ref.WeakReference;
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
    }
}
