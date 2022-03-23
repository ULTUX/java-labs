package pl.edu.pwr.quizapp.lang;

import java.util.ListResourceBundle;

public class LocaleClassBundle_en_US extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"windowTitle", "Quiz app"},
                {"languageChangeLabel","Language"},
                {"answerSubmitButton", "Submit"},
                {"confirmationLabel","Yes, you are right:"},
                {"nextQuestionButton","Next random question"},
                {"qFindCityIn","City | is located in:"},
                {"aFindCityIn","{0} is {1,choice,0.0#not|1.0#indeed} located in {2}."},
                {"qAlternativeNames","City | is sometimes called"},
                {"aAlternativeNames","You are {1,choice,0.0#wrong|1.0#right}, city {1} is {1,choice,0.0#not|1.0#sometimes} called {2}."},
        };
    }
}
