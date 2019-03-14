package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

//TODO Fix formatting
public class MoPub extends Blocker
{

    public static final String BANNER = "com.mopub.mobileads.MoPubView";
    public static final String BANNER_PREFIX = "com.mopub.mobileads";

    public static final String INTER_ADS = "com.mopub.mobileads.MoPubInterstitial";
    public static final String NATIVE_AD = "com.mopub.nativeads.MoPubAdAdapter";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        //TODO Does this need to use removeBanner?
        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAds", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAds", String.class, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAds", String.class, "com.mopub.nativeads.RequestParameters", lpparam);

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
