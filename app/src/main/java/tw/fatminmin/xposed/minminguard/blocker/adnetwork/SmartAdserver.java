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

public class SmartAdserver extends Blocker {
    
    public static final String BANNER = "com.smartadserver.android.library.ui.SASAdView";
    public static final String BANNER_PREFIX = "com.smartadserver.android.library.ui";

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
            Class<?> adView = XposedHelpers.findClass("com.smartadserver.android.library.ui.SASAdView", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "loadAd", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect SASAdView loadAd in " + packageName);
                    
                    if(removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
                
            });
            Util.log(packageName, packageName + " uses SmartAdserver");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
