package tw.fatminmin.xposed.minminguard.adnetwork;

import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Waystorm {
    public final static String banner = "com.waystorm.ads.WSAdBanner";
    public final static String bannerPrefix = "com.waystorm.ads";
    public final static String inter = "com.waystorm.ads.WSAdInterstitial";

    public static boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean test) {
        try {

            Class<?> waystormBanner = findClass(banner, lpparam.classLoader);
            Class<?> waystormInter = findClass(inter, lpparam.classLoader);

            XposedBridge.hookAllMethods(waystormBanner, "loadAd", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect waystormBanner loadAd in " + packageName);

                    if (!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(waystormInter, "loadAd",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect waystormInter loadAd in " + packageName);

                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });

            Util.log(packageName, packageName + " uses Waystorm");
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
