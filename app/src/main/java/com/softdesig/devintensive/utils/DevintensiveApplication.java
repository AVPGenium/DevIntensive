package com.softdesig.devintensive.utils;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.preference.PreferenceManager;

/**
 * Created by Admin on 29.06.2016.
 */
public class DevintensiveApplication extends Application {
    public static SharedPreferences sSharedPreferences;

    @Override
    public void onCreate(){
        super.onCreate();
        sSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
