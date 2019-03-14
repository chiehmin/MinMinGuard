package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class GmsDoubleClick extends Blocker
{

    public static final String BANNER = "com.google.android.gms.ads.doubleclick.PublisherAdView";
    public static final String BANNER_PREFIX = "com.google.android.gms.ads.doubleclick";

    public static final String INTER_ADS = "com.google.android.gms.ads.doubleclick.PublisherInterstitialAd";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        //This wont do anything. The loadAd method takes an argument. Therefor it cannot be hooked with removeBanner
        //result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "loadAd", "com.google.android.gms.ads.doubleclick.PublisherAdRequest", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", "com.google.android.gms.ads.doubleclick.PublisherAdRequest", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);

        return result;
    }

    @Override
    public String getBannerPrefix()
    {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner()
    {
        return BANNER;
    }
}
