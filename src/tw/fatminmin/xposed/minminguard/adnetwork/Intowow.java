package tw.fatminmin.xposed.minminguard.adnetwork;

import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Intowow {

    public final static String banner = "com.intowow.sdk.ui.view.InstreamADView";
    public final static String bannerPrefix = "com.intowow.sdk.ui.view";

    public static boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adview = findClass(banner, lpparam.classLoader);

            XposedBridge.hookAllMethods(adview, "start", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect intowow adview start in " + packageName);
                    if (!test) {
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
}
