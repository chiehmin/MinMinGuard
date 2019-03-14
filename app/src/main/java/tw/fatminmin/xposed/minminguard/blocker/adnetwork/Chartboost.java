package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Chartboost extends Blocker
{

    public static final String BANNER = "com.chartboost.sdk.Chartboost";
    public static final String BANNER_PREFIX = "com.chartboost.sdk";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "showInterstitial", String.class, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "showRewardedVideo", String.class, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, BANNER, "hasInterstitial", String.class, false, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, BANNER, "hasRewardedVideo", String.class, false, lpparam);

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
