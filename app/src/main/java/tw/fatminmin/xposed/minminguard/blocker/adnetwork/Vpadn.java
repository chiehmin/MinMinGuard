package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Vpadn extends Blocker {
    public static final String BANNER = "com.vpadn.ads.VpadnBanner";
    public static final String BANNER_PREFIX = "com.vpadn.ads";
    public static final String INTER_ADS = "com.vpadn.ads.VpadnInterstitialAd";
    public static final String NATIVE_ADS = "com.vpadn.ads.VpadnNativeAd";

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        try {

            ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);
            ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam, removeAd);
            ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "loadAd", lpparam, removeAd);
            ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "registerViewForInteraction", lpparam, removeAd);
            
            Util.log(packageName, packageName + " uses Vpadn");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
