package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class TWMads extends Blocker
{

    private static final String BANNER = "com.taiwanmobile.pt.adp.view.TWMAdView";
    private static final String BANNER_PREFIX = "com.taiwanmobile.pt.adp.view";
    private static final String INTER_ADS = "com.taiwanmobile.pt.adp.view.TWMInterstitialAd";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        //TODO Does this need to use removeBanner?
        result = ApiBlocking.removeBanner(packageName, BANNER, "activeAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", lpparam);

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
