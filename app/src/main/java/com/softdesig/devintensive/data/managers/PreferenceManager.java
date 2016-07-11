package com.softdesig.devintensive.data.managers;

import android.content.SharedPreferences;
import android.net.Uri;

import com.softdesig.devintensive.utils.ConstantManager;
import com.softdesig.devintensive.utils.DevintensiveApplication;

import java.util.ArrayList;
import java.util.List;

public class PreferenceManager {
    private SharedPreferences mSharedPreferences;
    private static final String[] USER_FIELDS = {
            ConstantManager.USER_PHONE_KEY,
            ConstantManager.USER_EMAIL_KEY,
            ConstantManager.USER_VK_KEY,
            ConstantManager.USER_GIT_KEY,
            ConstantManager.USER_ABOUT_KEY
    };

    private static final String[] USER_VALUES = {
            ConstantManager.USER_RATING_KEY,
            ConstantManager.USER_CODE_LINES_KEY,
            ConstantManager.USER_PROJECT_KEY
    };

    public PreferenceManager() {
        mSharedPreferences = DevintensiveApplication.getSharedPreferences();
    }

    public void saveUserProfileData(List<String> userFields){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_FIELDS.length; i++) {
            editor.putString(USER_FIELDS[i], userFields.get(i));
        }
        editor.apply();
    }

    public List<String> loadUserProfileData(){
        List<String> userFields = new ArrayList<>();
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_PHONE_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_EMAIL_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_VK_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_GIT_KEY, "null"));
        userFields.add(mSharedPreferences.getString(ConstantManager.USER_ABOUT_KEY, "null"));
        return userFields;
    }

    public void saveUserPhoto(Uri uri){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_PHOTO_KEY, uri.toString());
        editor.apply();
    }

    public Uri loadUserPhoto(){
        return Uri.parse(mSharedPreferences.getString(ConstantManager.USER_PHOTO_KEY, "android.resource://com.softdesign.devintensive/drawable/header_bg_1"));
    }

    public void saveAuthTocken(String authTocken){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.AUTH_TOCKEN_KEY, authTocken);
        editor.apply();
    }

    public String getAuthTocken(){
        return mSharedPreferences.getString(ConstantManager.AUTH_TOCKEN_KEY, "null");
    }

    public void saveUserId(String userId){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(ConstantManager.USER_ID_KEY, userId);
        editor.apply();
    }

    public String getUserId(){
        return mSharedPreferences.getString(ConstantManager.USER_ID_KEY, "null");
    }

    public void saveUserProfileValues(int[] userValues){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        for (int i = 0; i < USER_VALUES.length; i++) {
            editor.putString(USER_VALUES[i], String.valueOf(userValues[i]));
        }
        editor.apply();
    }

    public List<String> loadUserProfileValues(){
        List<String> userValues = new ArrayList<>();
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_RATING_KEY, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_CODE_LINES_KEY, "0"));
        userValues.add(mSharedPreferences.getString(ConstantManager.USER_PROJECT_KEY, "0"));
        return userValues;
    }
}
