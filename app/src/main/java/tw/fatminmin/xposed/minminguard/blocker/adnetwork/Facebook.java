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

public class Facebook extends Blocker {
    private static final String LOAD_AD = "loadAd";
    public final static String banner = "com.facebook.ads.AdView";
    public final static String bannerPrefix = "com.facebook.ads";
    public final static String inter = "com.facebook.ads.InterstitialAd";
    public final static String nativeAd = "com.facebook.ads.NativeAd";

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
        result |= ApiBlocking.removeBanner(packageName, banner, LOAD_AD, lpparam, removeAd);
        result |= ApiBlocking.removeBanner(packageName, banner, "setAdListener", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, inter, LOAD_AD, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, nativeAd, LOAD_AD, lpparam, removeAd);
        result |= customHandle(packageName, lpparam, removeAd);
        return result;
    }

    public boolean customHandle(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> facebookNativeAd = findClass(nativeAd, lpparam.classLoader);

            XposedBridge.hookAllMethods(facebookNativeAd, "registerViewForInteraction", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect facebookNativeAd registerViewForInteraction in " + packageName);

                    if (removeAd) {
                        View nativeAd = (View) param.args[0];
                        Main.removeAdView(nativeAd, packageName, true);
                        param.setResult(new Object());
                    }
                }
            });

        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
