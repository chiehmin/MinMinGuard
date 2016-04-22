package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.app.Activity;
import android.view.View;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Intowow extends Blocker {

    public static final String BANNER = "com.intowow.sdk.BannerAD";
    public static final String BANNER_FUNC = "onStart";
    public static final String BANNER_PREFIX = "com.intowow.sdk";

    public static final String SPLASH_AD = "com.intowow.sdk.SplashAD";
    public static final String SPLASH_AD_FUNC = "show";

    public static final String I2WAPI = "com.intowow.sdk.I2WAPI";
    public static final String API_FUNC_1 = "requesSingleOfferAD";
    public static final String API_FUNC_2 = "requesSplashAD";
    public static final String API_FUNC_3 = "requestHybridSplashAD";

    public static final String CONTENT_AD_HELPER = "com.intowow.sdk.ContentADHelper";
    public static final String HELPER_FUNC = "requestAD";

    public static final String STREAM_HELPER = "com.intowow.sdk.StreamHelper";
    public static final String STREAM_HELPER_FUNC_1 = "getAD";
    public static final String STREAM_HELPER_FUNC_2 = "setActive";
    public static final String STREAM_HELPER_FUNC_3 = "setListener";


    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, BANNER, BANNER_FUNC, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, SPLASH_AD, SPLASH_AD_FUNC, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, API_FUNC_1, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, API_FUNC_2, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, API_FUNC_3, null, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, CONTENT_AD_HELPER, HELPER_FUNC, null, lpparam, removeAd);

//        result |= ApiBlocking.blockAdFunctionWithResult(packageName, streamHelper, streamHelperFunc1, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, STREAM_HELPER, STREAM_HELPER_FUNC_2, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, STREAM_HELPER, STREAM_HELPER_FUNC_3, lpparam, removeAd);


        return result;
    }
}
