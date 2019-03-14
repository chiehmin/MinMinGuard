package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Millennial extends Blocker
{

    public static final String BANNER = "com.millennialmedia.android.MMAdView";
    public static final String BANNER_PREFIX = "com.millennialmedia.android";

    public static final String INTER_ADS = "com.millennialmedia.android.MMInterstitial";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "getAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "display", lpparam);

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
