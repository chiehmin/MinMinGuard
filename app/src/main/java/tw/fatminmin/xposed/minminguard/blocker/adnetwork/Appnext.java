package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Appnext extends Blocker
{
    public static final String INTER_ADS = "com.appnext.ads.interstitial.Interstitial";

    public static final String VIDEO_ADS = "com.appnext.ads.fullscreen.";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "showAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, VIDEO_ADS, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VIDEO_ADS, "showAd", lpparam);

        return result;
    }

    @Override
    public String getBanner()
    {
        return null;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }
}
