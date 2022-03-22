package pl.edu.pwr.quizapp;

public enum LocalizableStrings {
    WINDOW_TITLE("windowTitle"),
    LANGUAGE_CHANGE_LABEL("languageChangeLabel"),
    ANSWER_SUBMIT_BUTTON("answerSubmitButton"),
    CONFIRMATION_LABEL("confirmationLabel"),
    Q_FIND_CITY_IN_PL("qFindCityInPL"),
    A_FIND_CITY_IN_PL("aFindCityInPL"),
    Q_FIND_CITY_IN_GB("qFindCityInGB"),
    A_FIND_CITY_IN_GB("aFindCityInGB");

    private String resourceName;

    LocalizableStrings(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        return resourceName;
    }
}
