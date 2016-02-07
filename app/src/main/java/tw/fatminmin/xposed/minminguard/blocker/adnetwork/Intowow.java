package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.app.Activity;
import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Intowow extends Blocker {

    public final static String banner = "com.intowow.sdk.BannerAD";
    public final static String bannerFunc = "onStart";
    public final static String bannerPrefix = "com.intowow.sdk";

    public final static String splashAd = "com.intowow.sdk.SplashAD";
    public final static String splashAdFunc = "show";

    public final static String I2WAPI = "com.intowow.sdk.I2WAPI";
    public final static String apiFunc1 = "requesSingleOfferAD";
    public final static String apiFunc2 = "requesSplashAD";
    public final static String apiFunc3 = "requestHybridSplashAD";

    public final static String contentAdHelper = "com.intowow.sdk.ContentADHelper";
    public final static String helperFunc = "requestAD";

    public final static String streamHelper = "com.intowow.sdk.StreamHelper";
    public final static String streamHelperFunc1 = "getAD";
    public final static String streamHelperFunc2 = "setActive";
    public final static String streamHelperFunc3 = "setListener";


    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }

    @Override
    public String getBanner() {
        return banner;
    }
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, banner, bannerFunc, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, splashAd, splashAdFunc, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, apiFunc1, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, apiFunc2, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, apiFunc3, null, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, contentAdHelper, helperFunc, null, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, streamHelper, streamHelperFunc1, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, streamHelper, streamHelperFunc2, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, streamHelper, streamHelperFunc3, lpparam, removeAd);


        return result;
    }
}
