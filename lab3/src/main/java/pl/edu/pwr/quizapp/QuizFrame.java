package pl.edu.pwr.quizapp;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.plaf.multi.MultiLabelUI;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Locale;

public class QuizFrame {

    private final JFrame mainFrame;
    private JPanel mainPanel;
    private JTextField sampleAnswerTextField;
    private JButton submitQuestionButton;
    private JComboBox<String> languageSelector;
    private JLabel languageLabel;
    private JLabel confirmationLabel;
    private JLabel questionLabel;
    LanguageManager languageManager = new LanguageManager();
    Questions questions = new Questions();
    Question currentQuestion = null;
    String lastAnswer = null;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new QuizFrame();
    }

    public QuizFrame() {
        mainFrame = new JFrame(languageManager.getLocalizedString(LocalizableStrings.WINDOW_TITLE));
        mainFrame.setContentPane(mainPanel);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(1000, 20);
        mainFrame.setVisible(true);

        languageLabel.setText(languageManager.getLocalizedString(LocalizableStrings.LANGUAGE_CHANGE_LABEL));
        languageSelector.setModel(new DefaultComboBoxModel<>(new String[]{"en_US", "pl_PL"}));
        languageChanged();

        languageSelector.addActionListener(actionEvent -> {
            languageChanged();
        });

        languageManager.addLanguageChangeListener(() -> {
            languageLabel.setText(languageManager.getLocalizedString(LocalizableStrings.LANGUAGE_CHANGE_LABEL));
            mainFrame.setTitle(languageManager.getLocalizedString(LocalizableStrings.WINDOW_TITLE));
            submitQuestionButton.setText(languageManager.getLocalizedString(LocalizableStrings.ANSWER_SUBMIT_BUTTON));
        });

        submitQuestionButton.addActionListener((e) -> {
            checkResults();
        });

        currentQuestion = questions.getRandomQuestion();
        handleNextQuestion();
    }

    private void checkResults() {
        String answer = sampleAnswerTextField.getText();
        lastAnswer = answer;
        sampleAnswerTextField.setText("");
        currentQuestion.setUserAnswer(answer);
        boolean results = currentQuestion.checkResults();
        MessageFormat answerFormat = new MessageFormat(languageManager.getLocalizedString(currentQuestion.getQuestionAnswer()));
        Object answerArgs[] = {answer, results ? 1 : 0};
        confirmationLabel.setText(answerFormat.format(answerArgs));

    }

    private void handleNextQuestion() {
        questionLabel.setText(languageManager.getLocalizedString(currentQuestion.getQuestion()));
        mainFrame.pack();

    }

    public void languageChanged() {
        String[] languageCode = ((String) languageSelector.getSelectedItem()).split("_");
        languageManager.setLocale(new Locale(languageCode[0], languageCode[1]));
        if (currentQuestion != null) handleNextQuestion();
    }
}
