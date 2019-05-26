package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Flurry extends Blocker
{

    private static final String BANNER = "com.flurry.android.FlurryAds";
    private static final String BANNER_PREFIX = "com.flurry.android";

    private static final String NATIVE_AD = "com.flurry.android.ads.FlurryAdNative";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "displayAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "fetchAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "fetchAd", lpparam);

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
