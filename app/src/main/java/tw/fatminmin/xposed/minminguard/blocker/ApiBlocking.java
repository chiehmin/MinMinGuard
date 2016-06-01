package tw.fatminmin.xposed.minminguard.blocker;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by fatminmin on 2015/10/27.
 */
public final class ApiBlocking {
    
    private ApiBlocking() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static void handle(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {

        Class<?> activity = XposedHelpers.findClass("android.app.Application", lpparam.classLoader);
        XposedBridge.hookAllMethods(activity, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Context context = (Context)(param.thisObject);
                for (Blocker blocker : Main.blockers) {
                    String name = blocker.getClass().getSimpleName();
                    Boolean result = blocker.handleLoadPackage(packageName, lpparam, removeAd);
                    if(result) {
                        Util.notifyAdNetwork(context, packageName, name);
                    }
                }
            }
        });
    }
    /*
        Used for blocking banner function and removing banner
     */
    public static boolean removeBanner(final String packageName, final String banner, final String bannerFunc, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        return removeBannerWithResult(packageName, banner, bannerFunc, new Object(), lpparam, removeAd);
    }

    public static boolean removeBannerWithResult(final String packageName, final String banner, final String bannerFunc, final Object result, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> bannerClazz = findClass(banner, lpparam.classLoader);
            XposedBridge.hookAllMethods(bannerClazz, bannerFunc, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String debugMsg = String.format("Detect %s %s in %s", banner, bannerFunc, packageName);
                    Util.log(packageName, debugMsg);
                    if (removeAd) {
                        param.setResult(result);
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


    /*
        Used for blocking ad functions
     */
    public static boolean blockAdFunction(final String packageName, final String ad, final String adFunc, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> bannerClazz = findClass(ad, lpparam.classLoader);
            XposedBridge.hookAllMethods(bannerClazz, adFunc, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String debugMsg = String.format("Detect %s %s in %s", ad, adFunc, packageName);
                    Util.log(packageName, debugMsg);
                    if (removeAd) {
                        Util.notifyRemoveAdView(null, packageName, 1);
                        param.setResult(new Object());
                    }
                }
            });
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }
    public static boolean blockAdFunctionWithResult(final String packageName, final String ad, final String adFunc, final Object result, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> bannerClazz = findClass(ad, lpparam.classLoader);
            XposedBridge.hookAllMethods(bannerClazz, adFunc, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String debugMsg = String.format("Detect %s %s in %s", ad, adFunc, packageName);
                    Util.log(packageName, debugMsg);
                    if (removeAd) {
                        Util.notifyRemoveAdView(null, packageName, 1);
                        param.setResult(result);
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
