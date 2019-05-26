package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Inmobi extends Blocker
{

    private static final String BANNER = "com.inmobi.ads.IMBanner";
    private static final String BANNER_PREFIX = "com.inmobi.ads";

    private static final String INTER_ADS = "com.inmobi.ads.InMobiInterstitial";

    private static final String NATIVE_ADS = "com.inmobi.ads.InMobiNative";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "load", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "load", lpparam);

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
