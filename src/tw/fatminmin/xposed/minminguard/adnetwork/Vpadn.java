package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Vpadn {
    public final static String banner = "com.vpadn.ads.VpadnBanner";
    public final static String bannerPrefix = "com.vpadn.ads";
    public final static String inter = "com.vpadn.ads.VpadnInterstitialAd";
    public final static String nativeAd = "com.vpadn.ads.VpadnNativeAd";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            
            Class<?> adView = XposedHelpers.findClass(banner, lpparam.classLoader);
            Class<?> InterAds = XposedHelpers.findClass(inter, lpparam.classLoader);
            Class<?> vpadnNativeAd = XposedHelpers.findClass(nativeAd, lpparam.classLoader);
            
            XposedBridge.hookAllMethods(adView, "loadAd" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect VpadnBanner loadAd in " + packageName);
                            
                            if(!test) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            

            XposedBridge.hookAllMethods(InterAds, "show" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect VpadnInterstitialAd show in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(vpadnNativeAd, "loadAd" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect vpadnNativeAd loadAd in " + packageName);

                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(vpadnNativeAd, "registerViewForInteraction" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect vpadnNativeAd registerViewForInteraction in " + packageName);

                    if(!test) {
                        View nativeAd = (View) param.args[0];
                        Main.removeAdView(nativeAd, packageName, true);
                        param.setResult(new Object());
                    }
                }
            });
            
            Util.log(packageName, packageName + " uses Vpadn");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
