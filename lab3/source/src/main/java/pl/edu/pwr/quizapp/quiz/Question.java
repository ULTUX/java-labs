package pl.edu.pwr.quizapp.quiz;

import pl.edu.pwr.quizapp.lang.LocalizableStrings;

import java.util.Arrays;
import java.util.Objects;

public class Question implements Cloneable {
    private final LocalizableStrings questionText;
    private String[] userAnswer;
    private final VerifyFunction<String[], Boolean> getResults;
    private final LocalizableStrings questionAnswer;

    public Question(LocalizableStrings questionText, LocalizableStrings answerText, VerifyFunction<String[], Boolean> getResults) {
        this.getResults = getResults;
        this.questionText = questionText;
        this.questionAnswer = answerText;
    }

    public boolean checkResults() {
        try {
            return getResults.apply(userAnswer);
        } catch (Exception e) {
            return false;
        }
    }

    public LocalizableStrings getQuestionText() {
        return questionText;
    }

    public String[] getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String[] userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return getQuestionText() == question1.getQuestionText() && Arrays.equals(getUserAnswer(), question1.getUserAnswer())
                && Objects.equals(getResults, question1.getResults) && getQuestionAnswer() == question1.getQuestionAnswer();
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getQuestionText(), getResults, getQuestionAnswer());
        result = 31 * result + Arrays.hashCode(getUserAnswer());
        return result;
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
