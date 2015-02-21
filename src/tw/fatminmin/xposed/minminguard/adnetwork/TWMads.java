package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class TWMads {
    
    public final static String banner = "com.taiwanmobile.pt.adp.view.TWMAdView";
    public final static String bannerPrefix = "com.taiwanmobile.pt.adp.view";
    public final static String inter = "com.taiwanmobile.pt.adp.view.TWMInterstitialAd";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            
            Class<?> adView = XposedHelpers.findClass(banner, lpparam.classLoader);
            Class<?> InterAds = XposedHelpers.findClass(inter, lpparam.classLoader);

            XposedBridge.hookAllMethods(adView, "activeAd" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect TWMAdView activeAd in " + packageName);
                            
                            if(!test) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            

            XposedBridge.hookAllMethods(InterAds, "loadAd" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect TWMAdViewInterstitial show in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });
            
            Util.log(packageName, packageName + " uses TWMads");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
