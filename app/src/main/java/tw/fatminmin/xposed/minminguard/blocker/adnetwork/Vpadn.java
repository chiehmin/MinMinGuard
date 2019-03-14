package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

//TODO Fix formatting
public class Vpadn extends Blocker
{
    public static final String BANNER = "com.vpadn.ads.VpadnBanner";
    public static final String BANNER_PREFIX = "com.vpadn.ads";

    public static final String INTER_ADS = "com.vpadn.ads.VpadnInterstitialAd";
    public static final String NATIVE_ADS = "com.vpadn.ads.VpadnNativeAd";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "registerViewForInteraction", lpparam);

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
