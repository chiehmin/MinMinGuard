package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MasAd {
    
    public final static String banner = "mediba.ad.sdk.android.openx.MasAdView";
    public final static String bannerPrefix = "mediba.ad.sdk.android.openx";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adView = XposedHelpers.findClass("mediba.ad.sdk.android.openx.MasAdView", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "b", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect MasAdAdView update in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
                
            });
            Util.log(packageName, packageName + " uses MasAd");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
