package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class AppBrain extends Blocker
{
    private static final String BANNER = "com.appbrain.AppBrainBanner";

    private static final String INTER_ADS = "com.appbrain.AppBrain";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "requestAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "getAds", lpparam);

        return result;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }

    @Override
    public String getBanner()
    {
        return BANNER;
    }
}
