package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Vpadn extends Blocker {
    public static final String BANNER = "com.vpadn.ads.VpadnBanner";
    public static final String BANNER_PREFIX = "com.vpadn.ads";
    public static final String INTER = "com.vpadn.ads.VpadnInterstitialAd";
    public static final String NATIVE_AD = "com.vpadn.ads.VpadnNativeAd";

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        try {
            
            Class<?> adView = XposedHelpers.findClass(BANNER, lpparam.classLoader);
            Class<?> interAds = XposedHelpers.findClass(INTER, lpparam.classLoader);
            Class<?> vpadnNativeAd = XposedHelpers.findClass(NATIVE_AD, lpparam.classLoader);
            
            XposedBridge.hookAllMethods(adView, "loadAd" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect VpadnBanner loadAd in " + packageName);
                            
                            if(removeAd) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            

            XposedBridge.hookAllMethods(interAds, "show" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect VpadnInterstitialAd show in " + packageName);
                    
                    if(removeAd) {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(vpadnNativeAd, "loadAd" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect vpadnNativeAd loadAd in " + packageName);

                    if(removeAd) {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(vpadnNativeAd, "registerViewForInteraction" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect vpadnNativeAd registerViewForInteraction in " + packageName);

                    if(removeAd) {
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
