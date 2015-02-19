package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Hodo {
    public final static String banner = "com.hodo.HodoADView";
    public final static String bannerPrefix = "com.hodo";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            
            Class<?> adView = XposedHelpers.findClass("com.hodo.HodoADView", lpparam.classLoader);
            
            XposedBridge.hookAllMethods(adView, "requestAD" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect HodoADView requestAD in " + packageName);
                            
                            if(!test) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            
            XposedBridge.hookAllMethods(adView, "setListener" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect HodoADView setListener in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
            });
            
            Util.log(packageName, packageName + " uses HODo");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
