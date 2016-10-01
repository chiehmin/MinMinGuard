package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

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

/**
 * Created by bjosey on 2016/10/01.
 * http://dev.adplus.co.id/mobile-sdk/MobileAppDevelopers_en.pdf  (Archive: http://www.webcitation.org/6kvAJ7G6D)
 */
public class Adtech extends Blocker {

    public static final String BANNER = "com.adtech.mobilesdk.publisher.view.AdtechBannerView";
    public static final String BANNER_PREFIX = "com.adtech.mobilesdk.publisher.view";
    public static final String INTER_ADS = "com.adtech.mobilesdk.publisher.view.AdtechInterstitialView";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, BANNER, "load", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam, removeAd);

        return result;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }
}