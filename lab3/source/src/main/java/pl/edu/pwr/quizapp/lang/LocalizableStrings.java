package pl.edu.pwr.quizapp.lang;

public enum LocalizableStrings {
    WINDOW_TITLE("windowTitle"),
    LANGUAGE_CHANGE_LABEL("languageChangeLabel"),
    ANSWER_SUBMIT_BUTTON("answerSubmitButton"),
    NEXT_QUESTION_BUTTON("nextQuestionButton"),
    Q_FIND_CITY_IN("qFindCityIn"),
    A_FIND_CITY_IN("aFindCityIn"),
    Q_ALTERNATIVE_NAMES("qAlternativeNames"),
    A_ALTERNATIVE_NAMES("aAlternativeNames"),
    Q_CURRENCY("qCurrency"),
    A_CURRENCY("aCurrency");

    private final String resourceName;

    LocalizableStrings(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return resourceName;
    }
}
