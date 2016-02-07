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

public class Startapp extends Blocker {
    
    public final static String banner = "com.startapp.android.publish.HtmlAd";
    public final static String bannerFunc = "show";
    public final static String bannerPrefix = "com.startapp.android.publish";

    public final static String nativeAd = "com.startapp.android.publish.nativead.StartAppNativeAd";
    public final static String nativeAdFunc = "getNativeAds";

    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }

    @Override
    public String getBanner() {
        return banner;
    }
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, banner, bannerFunc, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, nativeAd, nativeAdFunc, lpparam, removeAd);
        return result;
    }
}
