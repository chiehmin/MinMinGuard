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
 * Created by fatminmin on 2016/2/6.
 */
public class Clickforce extends Blocker {

    public static final String BANNER = "com.adcustom.sdk.ads.AdBanner";
    public static final String BANNER_PREFIX = "com.adcustom.sdk.ads";

    @Override
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> bannerClazz = findClass(BANNER, lpparam.classLoader);
            XposedBridge.hookAllMethods(bannerClazz, "show", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect Clickforce AdBanner show in " + packageName);

                    if (removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });
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
