package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.os.Bundle;
import android.view.View;

import java.util.EnumSet;

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
    public static final String BANNER = "com.facebook.ads.AdView";
    public static final String BANNER_PREFIX = "com.facebook.ads";
    public static final String INTER = "com.facebook.ads.InterstitialAd";
    public static final String NATIVE_AD = "com.facebook.ads.NativeAd";
    public static final String NATIVE_ADS_MGR = "com.facebook.ads.NativeAdsManager";
    public static final String AUDIENCE_NETWORK = "com.facebook.ads.AudienceNetworkActivity";

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

        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.removeBanner(packageName, BANNER, "setAdListener", lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, INTER, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, "loadAd", EnumSet.class, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, "loadAdFromBid", String.class, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, "loadAdFromBid", EnumSet.class, String.class, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAd", EnumSet.class, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAdFromBid", String.class, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAdFromBid", EnumSet.class, String.class, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS_MGR, "loadAds", EnumSet.class, lpparam, removeAd);

        result |= customHandle(packageName, lpparam, removeAd);

        return result;
    }

    public boolean customHandle(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {
            /*Class<?> facebookNativeAd = findClass(NATIVE_AD, lpparam.classLoader);

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
            });*/

            XposedHelpers.findAndHookMethod(AUDIENCE_NETWORK,
                    lpparam.classLoader,
                    "onCreate",
                    Bundle.class,
                    new XC_MethodHook()
                    {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                        {
                            Util.log(packageName, "Set Bundle to new Bundle()");
                            param.args[0] = new Bundle();
                        }
                    });

        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
