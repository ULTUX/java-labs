package pl.edu.pwr.quizapp.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LanguageManager {

    private static LanguageManager instance;

    private static boolean useListLocale = true;

    private Locale locale = new Locale("en", "US");
    private Preferences prefs = Preferences.userNodeForPackage(getClass());
    List<Runnable> eventSubscribers = new ArrayList<>();
    ResourceBundle localeBundle = ResourceBundle.getBundle("LocaleBundle", locale);
    ResourceBundle localeBundleClass = ResourceBundle.getBundle("pl.edu.pwr.quizapp.lang.LocaleClassBundle", locale);

    public LanguageManager() {
        prefs.put("languageCode", locale.toString());
    }

    public String getLocalizedString(LocalizableStrings message) {
        if (useListLocale) return localeBundleClass.getString(message.toString());
        return localeBundle.getString(message.toString());
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        this.localeBundle = ResourceBundle.getBundle("LocaleBundle", locale);
        this.localeBundleClass = ResourceBundle.getBundle("pl.edu.pwr.quizapp.lang.LocaleClassBundle", locale);
        eventSubscribers.forEach(Runnable::run);
    }

    public void addLanguageChangeListener(Runnable runnable) {
        eventSubscribers.add(runnable);
    }

    public void deleteLanguageChangeListener(Runnable runnable) {
        eventSubscribers.remove(runnable);
    }

    public static LanguageManager getInstance() {
        if (instance == null) instance = new LanguageManager();
        return instance;
    }

    public static boolean isUseListLocale() {
        return useListLocale;
    }

    public static void setUseListLocale(boolean useListLocale) {
        LanguageManager.useListLocale = useListLocale;
    }


}
