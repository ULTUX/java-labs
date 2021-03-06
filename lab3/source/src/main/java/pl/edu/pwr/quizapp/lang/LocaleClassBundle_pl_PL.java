package pl.edu.pwr.quizapp.lang;

import java.util.ListResourceBundle;

public class LocaleClassBundle_pl_PL extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"windowTitle","Aplikacja quizowa"},
                {"languageChangeLabel","Język"},
                {"answerSubmitButton","Zapisz wynik"},
                {"nextQuestionButton","Następne losowe pytanie"},
                {"qFindCityIn","Miasto | jest położone w:"},
                {"aFindCityIn","{1,choice,0.0#Nie|1.0#Tak}, miasto {0} {1,choice,0.0#nie|1.0#} leży w {2}."},
                {"qAlternativeNames","Miasto | inaczej nazywa się"},
                {"aAlternativeNames","To {1,choice,0.0#nie|1.0#} prawda, {1,choice,0.0#miasta|1.0#miasto} {0} {1,choice,0.0#nie|1.0#czasem} nazywa się {2}."},
                {"qCurrency","Kraj | używa waluty o kodzie"},
                {"aCurrency","To {1,choice,0.0#nie|1.0#} prawda, {1,choice,0.0#waluty|1.0#walutę} o kodzie {2} {1,choice,0.0#nie|1.0#} używa się w {0}."},
        };
    }
}
