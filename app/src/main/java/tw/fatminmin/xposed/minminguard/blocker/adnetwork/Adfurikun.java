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

public class Adfurikun extends Blocker {
    
    public static final String BANNER = "jp.tjkapp.adfurikunsdk.AdfurikunBase";
    public static final String BANNER_PREFIX = "jp.tjkapp.adfurikunsdk";
    
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> adView = XposedHelpers.findClass("jp.tjkapp.adfurikunsdk.AdfurikunBase", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "a", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect AdfurikunAdView update in " + packageName);
                    
                    if(removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
                
            });
            Util.log(packageName, packageName + " uses Adfurikun");
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
