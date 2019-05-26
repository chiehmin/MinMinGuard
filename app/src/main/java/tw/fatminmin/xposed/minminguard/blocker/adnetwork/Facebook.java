package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Facebook extends Blocker
{
    private static final String BANNER = "com.facebook.ads.AdView";
    private static final String BANNER_PREFIX = "com.facebook.ads";

    private static final String INTER = "com.facebook.ads.InterstitialAd";

    private static final String NATIVE_AD = "com.facebook.ads.NativeAd";
    private static final String NATIVE_ADS_MGR = "com.facebook.ads.NativeAdsManager";

    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        result |= ApiBlocking.removeBanner(packageName, BANNER, "setAdListener", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, INTER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, "loadAdFromBid", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAdFromBid", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS_MGR, "loadAds", lpparam);

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
