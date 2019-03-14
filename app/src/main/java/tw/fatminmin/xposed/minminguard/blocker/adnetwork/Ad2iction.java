package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Ad2iction extends Blocker
{
    public static final String BANNER = "com.ad2iction.mobileads.Ad2ictionView";
    public static final String BANNER_PREFIX = "com.ad2iction.mobileads";

    public static final String INTER_ADS = "com.ad2iction.mobileads.Ad2ictionInterstitial";

    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam);

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
