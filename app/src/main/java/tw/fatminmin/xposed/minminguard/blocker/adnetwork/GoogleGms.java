package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class GoogleGms extends Blocker
{

    private static final String BANNER = "com.google.android.gms.ads.AdView";
    private static final String BANNER_PREFIX = "com.google.android.gms.ads";

    private static final String SEARCH_BANNER = "com.google.android.gms.ads.search.SearchAdView";
    private static final String INTER_ADS = "com.google.android.gms.ads.InterstitialAd";

    private static final String AD_LOADER = "com.google.android.gms.ads.AdLoader";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        result |= ApiBlocking.removeBanner(packageName, SEARCH_BANNER, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAds", lpparam);

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
