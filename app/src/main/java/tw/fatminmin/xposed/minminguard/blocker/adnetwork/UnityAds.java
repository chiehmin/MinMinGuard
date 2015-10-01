package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class UnityAds {
    public static boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> unityAds = findClass("com.unity3d.ads.android.UnityAds", lpparam.classLoader);

            XposedBridge.hookAllMethods(unityAds, "show", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect unityAds show in " + packageName);
                    if (!test) {
                        param.setResult(new Object());
                    }
                }
            });
            XposedBridge.hookAllMethods(unityAds, "canShow", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect unityAds canShow in " + packageName);
                    if (!test) {
                        param.setResult(Boolean.valueOf(false));
                    }
                }
            });
            XposedBridge.hookAllMethods(unityAds, "canShowAds", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect unityAds canShowAds in " + packageName);
                    if (!test) {
                        param.setResult(Boolean.valueOf(false));
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

