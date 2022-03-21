package pl.edu.pwr.quizapp;

public enum LocalizableStrings {
    WINDOW_TITLE("windowTitle"),
    LANGUAGE_CHANGE_LABEL("languageChangeLabel"),
    ANSWER_SUBMIT_BUTTON("answerSubmitButton");

    private String resourceName;

    LocalizableStrings(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return resourceName;
    }
}
