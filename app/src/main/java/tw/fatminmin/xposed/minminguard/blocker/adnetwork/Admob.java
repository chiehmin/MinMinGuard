package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Is not used anymore.
 *
 * @deprecated Doesnt do anything anymore. All Ads are loaded from GMS.
 */
@Deprecated
public class Admob extends Blocker
{

    public static final String BANNER = "com.google.ads.AdView";
    public static final String BANNER_PREFIX = "com.google.ads";

    public static final String INTER_ADS = "com.google.ads.InterstitialAd";

    // native ads
    public static final String AD_LOADER = "com.google.android.ads.AdLoader";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAd", lpparam);

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
