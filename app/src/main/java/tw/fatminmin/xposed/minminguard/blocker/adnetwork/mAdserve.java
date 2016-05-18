package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class mAdserve extends Blocker {
    
    public static final String BANNER = "com.adsdk.sdk.banner.InAppWebView";
    public static final String BANNER_PREFIX = "com.adsdk.sdk.banner";

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
            Class<?> adView = XposedHelpers.findClass("com.adsdk.sdk.banner.InAppWebView", lpparam.classLoader);
            XposedBridge.hookAllConstructors(adView, new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect mAdserve InAppWebView constructor in " + packageName);
                    if(removeAd) {
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
