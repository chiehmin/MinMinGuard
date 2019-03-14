package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Domob extends Blocker
{

    public static final String BANNER = "cn.domob.android.ads.DomobAdView";
    public static final String BANNER_PREFIX = "cn.domob.android.ads";

    public static final String INTER_ADS = "cn.domob.android.ads.DomobInterstitialAd";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "setAdEventListener", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "setInterstitialAdListener", lpparam);

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
