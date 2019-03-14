package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by fatminmin on 2015/10/30.
 * http://wiki.adbert.com.tw/doku.php
 */
public class Adbert extends Blocker
{

    public static final String BANNER = "com.adbert.AdbertADView";
    public static final String BANNER_PREFIX = "com.adbert";

    public static final String INTER_ADS = "com.adbert.AdbertInterstitialAD";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, BANNER, "start", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", lpparam);

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
