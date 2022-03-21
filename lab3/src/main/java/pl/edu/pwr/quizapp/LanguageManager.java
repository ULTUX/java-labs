package pl.edu.pwr.quizapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LanguageManager {
    private Locale locale = new Locale("en", "US");
    private Preferences prefs = Preferences.userNodeForPackage(getClass());
    List<Runnable> eventSubscribers = new ArrayList<>();
    ResourceBundle localeBundle = ResourceBundle.getBundle("LocaleBundle", locale);

    public LanguageManager() {
        prefs.put("languageCode", locale.toString());
    }

    public String getLocalizedString(LocalizableStrings message) {
        return localeBundle.getString(message.toString());
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.localeBundle = ResourceBundle.getBundle("LocaleBundle", locale);
        System.out.printf("Language changed to %s.", locale);
    }

}
