package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class AdmobGms extends Blocker
{

    public static final String BANNER = "com.google.android.gms.ads.AdView";
    public static final String BANNER_PREFIX = "com.google.android.gms.ads";

    public static final String SEARCH_BANNER = "com.google.android.gms.ads.search.SearchAdView";
    public static final String INTER_ADS = "com.google.android.gms.ads.InterstitialAd";

    public static final String AD_LOADER = "com.google.android.gms.ads.AdLoader";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        //result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        //result |= ApiBlocking.removeBanner(packageName, SEARCH_BANNER, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "loadAd", "com.google.android.gms.ads.AdRequest", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, SEARCH_BANNER, "loadAd", "com.google.android.gms.ads.search.SearchAdRequest", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", "com.google.android.gms.ads.AdRequest", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAd", "com.google.android.gms.ads.AdRequest", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAds", "com.google.android.gms.ads.AdRequest", int.class, lpparam);

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
