package pl.edu.pwr.quizapp.quiz;

import pl.edu.pwr.quizapp.lang.LocalizableStrings;

public class Question implements Cloneable {
    private final LocalizableStrings question;
    private String[] userAnswer;
    private String realAnswer;
    private final VerifyFunction<String[], Boolean> analyzeQuestion;
    private final LocalizableStrings questionAnswer;

    public Question(LocalizableStrings question, LocalizableStrings answer, VerifyFunction<String[], Boolean> analyzeQuestion) {
        this.analyzeQuestion = analyzeQuestion;
        this.question = question;
        this.questionAnswer = answer;
    }

    public boolean checkResults(){
        try {
            return analyzeQuestion.apply(userAnswer);
        } catch (Exception e) {
            return false;
        }
    }

    public String getRealAnswer() {
        return realAnswer;
    }

    public LocalizableStrings getQuestion() {
        return question;
    }

    public String[] getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String[] userAnswer) {
        this.userAnswer = userAnswer;
    }

    public LocalizableStrings getQuestionAnswer() {
        return questionAnswer;
    }

    @Override
    public Question clone() {
        try {
            Question clone = (Question) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
