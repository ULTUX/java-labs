package pl.edu.pwr.quizapp;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.util.Locale;
import java.util.prefs.Preferences;

public class QuizFrame {
    private JPanel mainPanel;
    private JTextField questionTextField;
    private JButton submitQuestionButton;
    private JComboBox<String> languageSelector;
    private JLabel languageLabel;
    LanguageManager languageManager = new LanguageManager();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new QuizFrame();
    }

    public QuizFrame() {
        JFrame frame = new JFrame(languageManager.getLocalizedString(LocalizableStrings.WINDOW_TITLE));
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        languageLabel.setText(languageManager.getLocalizedString(LocalizableStrings.LANGUAGE_CHANGE_LABEL));
        languageSelector.setModel(new DefaultComboBoxModel<>(new String[]{"en_US", "pl_PL"}));
        languageChanged();

        languageSelector.addActionListener(actionEvent -> {
            languageChanged();
        });
    }

    public void languageChanged() {
        String[] languageCode = ((String) languageSelector.getSelectedItem()).split("_");
        languageManager.setLocale(new Locale(languageCode[0], languageCode[1]));
    }
}
