package pl.edu.pwr.quizapp;

import com.formdev.flatlaf.FlatDarkLaf;
import pl.edu.pwr.quizapp.lang.LanguageManager;
import pl.edu.pwr.quizapp.lang.LocalizableStrings;
import pl.edu.pwr.quizapp.quiz.Question;
import pl.edu.pwr.quizapp.quiz.Questions;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Locale;

public class QuizFrame extends JFrame {

    private JPanel mainPanel;
    private JTextField answerTextField1;
    private JButton submitQuestionButton;
    private JComboBox<String> languageSelector;
    private JLabel languageLabel;
    private JLabel confirmationLabel;
    private JLabel fPartLabel;
    private JTextField answerTextField2;
    private JLabel sPartLabel;
    private JButton nextQuestionButton;
    private final LanguageManager languageManager = LanguageManager.getInstance();
    private final Questions questions;
    private Question currentQuestion = null;
    private boolean resultsShown = false;
    private String userAnswer1;
    private String userAnswer2;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        new QuizFrame();
    }

    public QuizFrame() {
        super();
        this.questions = new Questions();
        setTitle(languageManager.getLocalizedString(LocalizableStrings.WINDOW_TITLE));
        setContentPane(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 120);
        setVisible(true);

        languageSelector.setModel(new DefaultComboBoxModel<>(new String[]{"en_US", "pl_PL"}));
        languageSelector.addActionListener(actionEvent -> languageChanged());
        languageSelector.setSelectedItem(languageManager.getLocale().toString());

        languageManager.addLanguageChangeListener(() -> {
            languageLabel.setText(languageManager.getLocalizedString(LocalizableStrings.LANGUAGE_CHANGE_LABEL));
            setTitle(languageManager.getLocalizedString(LocalizableStrings.WINDOW_TITLE));
            submitQuestionButton.setText(languageManager.getLocalizedString(LocalizableStrings.ANSWER_SUBMIT_BUTTON));
            nextQuestionButton.setText(languageManager.getLocalizedString(LocalizableStrings.NEXT_QUESTION_BUTTON));
            if (currentQuestion != null) handleNextQuestion();
            if (resultsShown) checkResults();
        });

        submitQuestionButton.addActionListener(e -> {
            userAnswer1 = answerTextField1.getText();
            answerTextField1.setText("");
            userAnswer2 = answerTextField2.getText();
            answerTextField2.setText("");
            resultsShown = true;
            checkResults();
        });

        nextQuestionButton.addActionListener(e -> {
            resultsShown = false;
            confirmationLabel.setText("");
            currentQuestion = questions.getRandomQuestion(currentQuestion);
            handleNextQuestion();
        });


        currentQuestion = questions.getRandomQuestion(currentQuestion);
        handleNextQuestion();
    }

    private void checkResults() {
        currentQuestion.setUserAnswer(new String[]{userAnswer1, userAnswer2});
        boolean results = currentQuestion.checkResults();
        MessageFormat answerFormat = new MessageFormat(languageManager.getLocalizedString(currentQuestion.getQuestionAnswer()));
        Object[] answerArgs = {userAnswer1, results ? 1 : 0, userAnswer2};
        confirmationLabel.setText(answerFormat.format(answerArgs));
        resultsShown = true;

    }

    private void handleNextQuestion() {
        String[] questionStrings = languageManager.getLocalizedString(currentQuestion.getQuestionText()).split("\\|");
        fPartLabel.setText(questionStrings[0]);
        sPartLabel.setText(questionStrings[1]);

    }

    public void languageChanged() {
        String[] languageCode = ((String) languageSelector.getSelectedItem()).split("_");
        languageManager.setLocale(new Locale(languageCode[0], languageCode[1]));
    }

}
