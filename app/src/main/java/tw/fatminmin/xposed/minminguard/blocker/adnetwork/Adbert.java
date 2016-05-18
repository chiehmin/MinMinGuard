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

/**
 * Created by fatminmin on 2015/10/30.
 * http://wiki.adbert.com.tw/doku.php
 */
public class Adbert extends Blocker {

    public static final String BANNER = "com.adbert.AdbertADView";
    public static final String BANNER_PREFIX = "com.adbert";
    public static final String INTER = "com.adbert.AdbertInterstitialAD";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {

        try {

            Class<?> bannerClazz = findClass(BANNER, lpparam.classLoader);
            Class<?> interClazz = findClass(INTER, lpparam.classLoader);

            XposedBridge.hookAllMethods(bannerClazz, "start", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect AdbertBanner start in " + packageName);

                    if (removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(interClazz, "loadAd",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect AdbertInter loadAd in " + packageName);

                    if(removeAd) {
                        param.setResult(new Object());
                    }
                }
            });

            Util.log(packageName, packageName + " uses AdbertBanner");
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
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
