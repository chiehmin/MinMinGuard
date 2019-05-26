package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

//TODO Fix formatting
public class MoPub extends Blocker
{

    private static final String BANNER = "com.mopub.mobileads.MoPubView";
    private static final String BANNER_PREFIX = "com.mopub.mobileads";

    private static final String INTER_ADS = "com.mopub.mobileads.MoPubInterstitial";
    private static final String NATIVE_AD = "com.mopub.nativeads.MoPubAdAdapter";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAds", lpparam);

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
