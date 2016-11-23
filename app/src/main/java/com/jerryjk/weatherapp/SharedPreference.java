package com.jerryjk.weatherapp;

/**
 * Created by jarek on 2016-11-17.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPreference {

    public static final String PREFS_NAME = "CITY_PREFS";
    public static final String PREFS_CITY = "CITY_PREFS_String";

    public SharedPreference() {
        super();
    }

    public void saveCityPrefs(Context context, String text) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //1
        editor = settings.edit(); //2
        editor.putString(PREFS_CITY, text); //3
        editor.commit(); //4
    }

    public String getCityPrefs(Context context) {
        SharedPreferences settings;
        String text;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_CITY, null);
        return text;
    }

    public void clearCityPrefs(Context context) {
        SharedPreferences settings;
        Editor editor;

        //settings = PreferenceManager.getDefaultSharedPreferences(context);
        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public void removeCityPrefs(Context context) {
        SharedPreferences settings;
        Editor editor;

        settings = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();

        editor.remove(PREFS_CITY);
        editor.commit();
    }

}