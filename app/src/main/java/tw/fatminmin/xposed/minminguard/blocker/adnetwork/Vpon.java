package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Vpon extends Blocker
{

    public static final String BANNER = "com.vpon.ads.VponBanner";
    public static final String BANNER_PREFIX = "com.vpon.ads";
    public static final String INTER_ADS = "com.vpon.ads.VponInterstitialAd";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        //TODO Does this need to use removebanner?
        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
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
