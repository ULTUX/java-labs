package pl.edu.pwr.userdata.ui;

import pl.edu.pwr.userdata.datamodels.UserData;
import pl.edu.pwr.userdata.dataproviders.DefaultDataProvider;
import pl.edu.pwr.userdata.dataproviders.IDataProvider;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.util.Locale;

public class MainFrame {
    private JList<String> userDataList;
    private int lastSelectedListId = -1;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private JFormattedTextField firstNameField;
    private JFormattedTextField lastNameField;
    private JFormattedTextField ageField;
    private JLabel imgLabel;
    private JPanel mainPanel;
    private JButton loadUserDataButton;
    private JLabel isLoadedIndicator;
    private JRadioButton radioButton1;
    IDataProvider dataProvider;


    public MainFrame() {
        JFrame frame = new JFrame("User data browser");
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        userDataList.addListSelectionListener(e -> {
            if (userDataList.getSelectedIndex() == lastSelectedListId) return;
            if (userDataList.getModel().getSize() == 0) return;
            lastSelectedListId = userDataList.getSelectedIndex();
            try {
                UserData user = dataProvider.getUserData(String.valueOf(userDataList.getSelectedValue()));
                firstNameField.setText(user.getFirstName());
                lastNameField.setText(user.getLastName());
                ageField.setText(String.valueOf(user.getAge()));
                imgLabel.setIcon(user.getProfilePic());
                imgLabel.setText("");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(mainPanel, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loadUserDataButton.addActionListener(e -> {
            JFileChooser fChooser = new JFileChooser();
            fChooser.setAcceptAllFileFilterUsed(false);
            fChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int retState = fChooser.showOpenDialog(mainPanel);
            if (retState == JFileChooser.APPROVE_OPTION) {
                dataProvider = new DefaultDataProvider(fChooser.getSelectedFile());
                dataProvider.setNotificationComponent(radioButton1);
                DefaultListModel<String> jListModel = new DefaultListModel<>();
                jListModel.addAll(dataProvider.getUserIds());
                userDataList.setModel(jListModel);
            }
        });
    }

}
