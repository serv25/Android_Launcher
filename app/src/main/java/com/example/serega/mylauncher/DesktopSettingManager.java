package com.example.serega.mylauncher;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;

public class DesktopSettingManager {

    private Context context;
    private SharedPreferences sharedPreferences;
    private HashSet<String> stringSet;
    private static final String KEY = "******";
    private static final int MAX_AMOUNT_OF_APPS = 12;

    public DesktopSettingManager(Context context) {
        this.context = context;
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

    public ArrayList<AppInfo> getDesktopApps(){
        ArrayList<AppInfo> desktopApps = new ArrayList<>();

        stringSet = (HashSet<String>) sharedPreferences.getStringSet(KEY, new HashSet<String>());
        Gson gson = new Gson();
        AppForJson appForJson;
        Drawable icon = null;
        AppInfo appInfo;

        for(String str : stringSet){
            appForJson = gson.fromJson(str, AppForJson.class);
            try {
                icon = context.getPackageManager().getApplicationIcon(appForJson.name);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            appInfo = new AppInfo(appForJson.label, appForJson.name, icon);
            desktopApps.add(appInfo);
        }

        return desktopApps;
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
