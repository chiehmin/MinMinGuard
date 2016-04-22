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

public class TWMads extends Blocker {
    
    public static final String BANNER = "com.taiwanmobile.pt.adp.view.TWMAdView";
    public static final String BANNER_PREFIX = "com.taiwanmobile.pt.adp.view";
    public static final String INTER = "com.taiwanmobile.pt.adp.view.TWMInterstitialAd";

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

            XposedBridge.hookAllMethods(adView, "activeAd" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect TWMAdView activeAd in " + packageName);
                            
                            if(removeAd) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            

            XposedBridge.hookAllMethods(interAds, "loadAd" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect TWMAdViewInterstitial show in " + packageName);
                    
                    if(removeAd) {
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
