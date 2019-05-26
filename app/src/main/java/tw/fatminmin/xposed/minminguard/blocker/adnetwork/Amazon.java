package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Amazon extends Blocker
{

    private static final String BANNER = "com.amazon.device.ads.AdLayout";
    private static final String BANNER_PREFIX = "com.amazon.device.ads";

    private static final String AD_REQUEST = "com.amazon.device.ads.DTBAdRequest";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "setListener", lpparam);
        result |= ApiBlocking.removeBannerWithResult(packageName, BANNER, "loadAd", true, lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, AD_REQUEST, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, AD_REQUEST, "internalLoadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, AD_REQUEST, "loadAdRequest", lpparam);

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
