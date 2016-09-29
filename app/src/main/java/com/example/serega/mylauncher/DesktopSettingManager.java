package com.example.serega.mylauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.HashSet;

public class DesktopSettingManager {

    private SharedPreferences sharedPreferences;
    private HashSet<String> stringSet;
    private static final String KEY = "******";
    private static final int MAX_AMOUNT_OF_APPS = 12;

    public DesktopSettingManager(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isAddedOnDesktop(AppInfo appInfo) {
        stringSet = (HashSet<String>) sharedPreferences.getStringSet(KEY, new HashSet<String>());
        if (stringSet == null) {
            stringSet = new HashSet<>();
            save(appInfo);
            return true;
        } else if (stringSet.size() < MAX_AMOUNT_OF_APPS) {
            String objAsString = getObjAsString(appInfo);
            if (!stringSet.contains(objAsString)) {
                save(appInfo);
                return true;
            }
        }
        return false;
    }

    private void save(AppInfo appInfo) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String objAsString = getObjAsString(appInfo);
        stringSet.add(objAsString);
        editor.putStringSet(KEY, stringSet);
        editor.apply();
    }

    private String getObjAsString(AppInfo appInfo) {
        AppForJson appForJson = new AppForJson(
                appInfo.getLabel().toString(),
                appInfo.getName().toString()
        );
        Gson gson = new Gson();
        return gson.toJson(appForJson);
    }

    class AppForJson {
        private String label;
        private String name;

        public AppForJson(String label, String name) {
            this.label = label;
            this.name = name;
        }
    }
}
