package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Amobee {
    
    public final static String banner = "com.amobee.adsdk.AdManager";
    public final static String bannerPrefix = "com.amobee.adsdk";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.amobee.adsdk.AdManager", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "getAd", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect Amobee getAd in " + packageName);
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
                
            });
            
            Util.log(packageName, packageName + " uses Amobee");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
