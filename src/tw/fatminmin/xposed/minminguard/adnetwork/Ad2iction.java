package tw.fatminmin.xposed.minminguard.adnetwork;

import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Ad2iction {
    public final static String banner = "com.ad2iction.mobileads.Ad2ictionView";
    public final static String bannerPrefix = "com.ad2iction.mobileads";
    public final static String inter = "com.ad2iction.mobileads.Ad2ictionInterstitial";

    public static boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean test) {
        try {

            Class<?> ad2ictionBanner = findClass(banner, lpparam.classLoader);
            Class<?> ad2ictionInter = findClass(inter, lpparam.classLoader);

            XposedBridge.hookAllMethods(ad2ictionBanner, "loadAd", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect ad2ictionBanner loadAd in " + packageName);

                    if (!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(ad2ictionInter, "load",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect ad2ictionInter loadAd in " + packageName);

                    if(!test) {
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
}
