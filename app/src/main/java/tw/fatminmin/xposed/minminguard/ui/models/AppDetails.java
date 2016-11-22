package tw.fatminmin.xposed.minminguard.ui.models;

import android.graphics.drawable.Drawable;

public class AppDetails {

    private String name;
    private String packageName;
    private Drawable icon;
    private boolean isEnabled;

    public AppDetails(String name, String packageName, Drawable icon, boolean isEnabled) {
        this.name = name;
        this.packageName = packageName;
        this.icon = icon;
        this.isEnabled = isEnabled;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
