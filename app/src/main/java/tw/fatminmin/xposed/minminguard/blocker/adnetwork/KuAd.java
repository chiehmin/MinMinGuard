package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class KuAd extends Blocker
{

    public static final String BANNER = "com.waystorm.ads.WSAdBanner";
    public static final String BANNER_PREFIX = "com.waystorm.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "setWSAdListener", lpparam);
        result |= ApiBlocking.removeBanner(packageName, BANNER, "setApplicationId", lpparam);
        result |= ApiBlocking.removeBanner(packageName, BANNER, "mediationLoadAd", lpparam);

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
