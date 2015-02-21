package tw.fatminmin.xposed.minminguard.adnetwork;

import android.view.View;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class Facebook {
    public final static String banner = "com.facebook.ads.AdView";
    public final static String bannerPrefix = "com.facebook.ads";
    public final static String inter = "com.facebook.ads.InterstitialAd";
    public final static String nativeAd = "com.facebook.ads.NativeAd";

    public static boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean test) {
        try {

            Class<?> facebookBanner = findClass(banner, lpparam.classLoader);
            Class<?> facebookInter = findClass(inter, lpparam.classLoader);
            Class<?> facebookNativeAd = findClass(nativeAd, lpparam.classLoader);

            XposedBridge.hookAllMethods(facebookBanner, "loadAd", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect facebookBanner loadAd in " + packageName);

                    if (!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(facebookInter, "loadAd",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect facebookInter loadAd in " + packageName);

                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(facebookNativeAd, "loadAd",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect facebookNativeAd loadAd in " + packageName);

                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(facebookNativeAd, "registerViewForInteraction",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect facebookNativeAd registerViewForInteraction in " + packageName);

                    if(!test) {
                        View nativeAd = (View) param.args[0];
                        Main.removeAdView(nativeAd, packageName, true);
                        param.setResult(new Object());
                    }
                }
            });


            Util.log(packageName, packageName + " uses facebook");
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
