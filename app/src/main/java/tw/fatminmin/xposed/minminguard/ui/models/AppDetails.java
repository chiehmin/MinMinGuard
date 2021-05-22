package tw.fatminmin.xposed.minminguard.ui.models;

import android.content.pm.ApplicationInfo;
import android.widget.ImageView;

import tw.fatminmin.xposed.minminguard.ui.image.GlideModule;

public class AppDetails
{

    private String name;
    private String packageName;
    private ApplicationInfo applicationInfo;
    private boolean isEnabled;

    public AppDetails(String name, String packageName, ApplicationInfo applicationInfo, boolean isEnabled)
    {
        this.name = name;
        this.applicationInfo = applicationInfo;
        this.packageName = packageName;
        this.isEnabled = isEnabled;
    }

    public String getName()
    {
        return name;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void loadIcon(ImageView imageView) {
        GlideModule.loadApplicationIcon(imageView.getContext(), applicationInfo, imageView);
    }

    public boolean isEnabled()
    {
        return isEnabled;
    }
}
