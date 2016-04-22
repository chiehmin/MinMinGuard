package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Amobee extends Blocker {
    
    public static final String BANNER = "com.amobee.adsdk.AdManager";
    public static final String BANNER_PREFIX = "com.amobee.adsdk";
    
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.amobee.adsdk.AdManager", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "getAd", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect Amobee getAd in " + packageName);
                    if(removeAd) {
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
    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
}
