package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class mAdserve {
    
    public final static String banner = "com.adsdk.sdk.banner.InAppWebView";
    public final static String bannerPrefix = "com.adsdk.sdk.banner";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.adsdk.sdk.banner.InAppWebView", lpparam.classLoader);
            XposedBridge.hookAllConstructors(adView, new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect mAdserve InAppWebView constructor in " + packageName);
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
                
            });
            
            Util.log(packageName, packageName + " uses mAdserve");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
