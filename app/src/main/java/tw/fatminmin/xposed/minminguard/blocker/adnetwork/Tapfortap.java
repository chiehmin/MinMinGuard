package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Tapfortap extends Blocker
{

    private static final String BANNER = "com.tapfortap.AdView";
    private static final String BANNER_PREFIX = "com.tapfortap";

    private static final String INTER_ADS = "com.tapfortap.Interstitial";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        //TODO Does this need to use removeBanner????
        result = ApiBlocking.removeBanner(packageName, BANNER, "loadAds", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);

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
