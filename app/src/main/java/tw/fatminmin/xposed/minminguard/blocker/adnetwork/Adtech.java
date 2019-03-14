package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by bjosey on 2016/10/01.
 * http://dev.adplus.co.id/mobile-sdk/MobileAppDevelopers_en.pdf  (Archive: http://www.webcitation.org/6kvAJ7G6D)
 */
public class Adtech extends Blocker
{

    public static final String BANNER = "com.adtech.mobilesdk.publisher.view.AdtechBannerView";
    public static final String BANNER_PREFIX = "com.adtech.mobilesdk.publisher.view";

    public static final String INTER_ADS = "com.adtech.mobilesdk.publisher.view.AdtechInterstitialView";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "load", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam);

        return result;
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