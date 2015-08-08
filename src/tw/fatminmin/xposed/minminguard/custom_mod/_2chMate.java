package tw.fatminmin.xposed.minminguard.custom_mod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Util;

import android.view.View;

public class _2chMate {
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        if (!packageName.equals("jp.co.airfront.android.a2chMate")) {
            return false;
        }

        try {
            final Class<?> viewGroupClass = XposedHelpers.findClass("android.view.ViewGroup", lpparam.classLoader);
            XposedBridge.hookAllMethods(viewGroupClass, "addView", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    final View view = (View) param.args[0];
                    if (view.getClass().getName().equals("jp.syoboi.a2chMate.view.MyAdView")) {
                        Util.log(packageName, "Detect 2chmate MyAdView in " + packageName);
                        if (!test) {
                            param.setResult(null);
                        }
                    }
                }
            });
            Util.log(packageName, packageName + " is 2chmate");
        } catch (ClassNotFoundError e) {
            return false;
        }

        return true;
    }
}
