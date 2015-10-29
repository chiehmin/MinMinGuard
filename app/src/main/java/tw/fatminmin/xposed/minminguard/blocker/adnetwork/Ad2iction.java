package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Ad2iction extends Blocker {
    public final static String banner = "com.ad2iction.mobileads.Ad2ictionView";
    public final static String bannerPrefix = "com.ad2iction.mobileads";
    public final static String inter = "com.ad2iction.mobileads.Ad2ictionInterstitial";

    public String getName() {
        return getClass().getSimpleName();
    }

    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {

            Class<?> ad2ictionBanner = findClass(banner, lpparam.classLoader);
            Class<?> ad2ictionInter = findClass(inter, lpparam.classLoader);

            XposedBridge.hookAllMethods(ad2ictionBanner, "loadAd", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect ad2ictionBanner loadAd in " + packageName);

                    if (removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(ad2ictionInter, "load",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect ad2ictionInter loadAd in " + packageName);

                    if(removeAd) {
                        param.setResult(new Object());
                    }
                }
            });

            Util.log(packageName, packageName + " uses Ad2iction");
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }

    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }

    @Override
    public String getBanner() {
        return banner;
    }
}
