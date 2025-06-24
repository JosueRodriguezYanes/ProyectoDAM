package com.example.proyectofinaldam135;

import android.app.Activity;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;
import android.preference.PreferenceManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {
    // Método existente para fecha/hora
    public static String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Nueva implementación para manejo de temas
    public static class ThemeUtils {
        private static final String PREF_THEME = "pref_theme";
        private static final String THEME_LIGHT = "light";
        private static final String THEME_DARK = "dark";
        private static final String THEME_SYSTEM = "system";

        public static void applyTheme(Activity activity) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            String theme = preferences.getString(PREF_THEME, THEME_SYSTEM);

            switch (theme) {
                case THEME_LIGHT:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case THEME_DARK:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
                default:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
            }
        }

        public static void toggleTheme(Activity activity) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            String currentTheme = preferences.getString(PREF_THEME, THEME_SYSTEM);

            String newTheme;
            if (currentTheme.equals(THEME_LIGHT)) {
                newTheme = THEME_DARK;
            } else if (currentTheme.equals(THEME_DARK)) {
                newTheme = THEME_SYSTEM;
            } else {
                newTheme = THEME_LIGHT;
            }

            preferences.edit().putString(PREF_THEME, newTheme).apply();
            activity.recreate();
        }

        public static String getCurrentThemeName(Activity activity) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
            return preferences.getString(PREF_THEME, THEME_SYSTEM);
        }
    }
}