package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class AdMarvel extends Blocker
{

    public static final String BANNER = "com.admarvel.android.ads.AdMarvelView";
    public static final String BANNER_PREFIX = "com.admarvel.android.ads";

    public static final String INTER_ADS = "com.admarvel.android.ads.AdMarvelInterstitialAds";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "requestNewAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "requestNewInterstitialAd", lpparam);

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
