package com.example.serega.mylauncher;


import android.graphics.drawable.Drawable;

public class AppInfo {

    private CharSequence label;
    private CharSequence name;
    private Drawable icon;
    private boolean onDesktop = false;

    public AppInfo(CharSequence label, CharSequence name, Drawable icon) {
        this.label = label;
        this.name = name;
        this.icon = icon;
    }

    public CharSequence getLabel() {
        return label;
    }

    public void setLabel(CharSequence label) {
        this.label = label;
    }

    public CharSequence getName() {
        return name;
    }

    public void setName(CharSequence name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isOnDesktop() {
        return onDesktop;
    }

    public void setOnDesktop(boolean onDesktop) {
        this.onDesktop = onDesktop;
    }
}
