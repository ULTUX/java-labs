package pl.edu.pwr.quizapp.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class LanguageManager {

    private static final String PREF_LANGUAGE_PATH = "languageCode";
    private static LanguageManager instance;
    private static boolean useListLocale = true;
    private Locale locale;
    private final Preferences prefs = Preferences.userNodeForPackage(getClass());
    List<Runnable> eventSubscribers = new ArrayList<>();
    ResourceBundle localeBundle;
    ResourceBundle localeBundleClass;

    public LanguageManager() {
        var localeString = prefs.get(PREF_LANGUAGE_PATH, "en_US").split("_");
        locale = new Locale(localeString[0], localeString[1]);
        try {
            if (prefs.nodeExists(PREF_LANGUAGE_PATH)) prefs.put(PREF_LANGUAGE_PATH, locale.toString());
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
        eventSubscribers.forEach(Runnable::run);
        localeBundle = ResourceBundle.getBundle("LocaleBundle", locale);
        localeBundleClass = ResourceBundle.getBundle("pl.edu.pwr.quizapp.lang.LocaleClassBundle", locale);
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
        prefs.put(PREF_LANGUAGE_PATH, locale.toString());
    }

    public void addLanguageChangeListener(Runnable runnable) {
        runnable.run();
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

    public Locale getLocale() {
        return locale;
    }
}
