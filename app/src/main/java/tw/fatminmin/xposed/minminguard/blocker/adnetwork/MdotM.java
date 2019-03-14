package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

//TODO Fix formatting
public class MdotM extends Blocker
{

    public static final String BANNER = "com.mdotm.android.view.MdotMAdView";
    public static final String BANNER_PREFIX = "com.mdotm.android.view";

    public static final String INTER_ADS = "com.mdotm.android.view.MdotMInterstitial";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadBannerAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadInterstitial", lpparam);

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
