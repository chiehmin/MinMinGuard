package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by fatminmin on 2016/2/7.
 */
public class MobFox extends Blocker
{

    private static final String BANNER = "com.adsdk.sdk.waterfall.Banner";
    private static final String BANNER_PREFIX = "com.adsdk.sdk.waterfall";

    @Override
    //TODO Check if this needs to use removeBanner
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
    }

    @Override
    public String getBanner()
    {
        return BANNER;
    }

    @Override
    public String getBannerPrefix()
    {
        return BANNER_PREFIX;
    }
}
