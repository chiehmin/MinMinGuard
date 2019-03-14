package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class GoogleGmsDoubleClick extends Blocker
{
    public static final String PUBLISHER_BANNER = "com.google.android.gms.ads.doubleclick.PublisherAdView";
    public static final String BANNER_PREFIX = "com.google.android.gms.ads.doubleclick";

    public static final String INTER_ADS = "com.google.android.gms.ads.doubleclick.PublisherInterstitialAd";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, PUBLISHER_BANNER, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", lpparam);
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
        return PUBLISHER_BANNER;
    }
}
