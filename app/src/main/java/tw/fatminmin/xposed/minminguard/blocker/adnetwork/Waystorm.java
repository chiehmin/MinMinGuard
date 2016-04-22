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

public class Waystorm extends Blocker {
    public static final String BANNER = "com.waystorm.ads.WSAdBanner";
    public static final String BANNER_PREFIX = "com.waystorm.ads";
    public static final String INTER = "com.waystorm.ads.WSAdInterstitial";

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {

            Class<?> waystormBanner = findClass(BANNER, lpparam.classLoader);
            Class<?> waystormInter = findClass(INTER, lpparam.classLoader);

            XposedBridge.hookAllMethods(waystormBanner, "loadAd", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect waystormBanner loadAd in " + packageName);

                    if (removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(waystormInter, "loadAd",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect waystormInter loadAd in " + packageName);

                    if(removeAd) {
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
