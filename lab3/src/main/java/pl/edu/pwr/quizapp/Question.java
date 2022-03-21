package pl.edu.pwr.quizapp;

import java.io.IOException;

public class Question {
    private final String question;
    private String userAnswer;
    private final String jsonAnswerPath;
    private final String apiURL;
    private final boolean isAnswerNumeric;
    private double marginOfError = 0;
    private String realAnswer;

    public Question(String question, String userAnswer, String jsonAnswerPath, String apiURL, boolean isAnswerNumeric) {
        this.question = question;
        this.userAnswer = userAnswer;
        this.jsonAnswerPath = jsonAnswerPath;
        this.apiURL = apiURL;
        this.isAnswerNumeric = isAnswerNumeric;
    }

    public boolean verifyAnswer() throws IOException {
        JSONDataParser parser = new JSONDataParser(apiURL, jsonAnswerPath);
        String answer = parser.getValue();
        realAnswer = answer;
        if (isAnswerNumeric){
            double numericAnswer = Double.parseDouble(answer);
            if (Math.abs(numericAnswer - Double.parseDouble(userAnswer)) <= marginOfError) return true;
        }
        return userAnswer.strip().equals(answer.strip());
    }


    public String getRealAnswer() {
        return realAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getJsonAnswerPath() {
        return jsonAnswerPath;
    }

    public boolean isAnswerNumeric() {
        return isAnswerNumeric;
    }

    public double getMarginOfError() {
        return marginOfError;
    }

    public void setMarginOfError(double marginOfError) {
        this.marginOfError = marginOfError;
    }

}
