package tw.fatminmin.xposed.minminguard.adnetwork;

import java.lang.reflect.Method;

import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Onelouder {
    
    public final static String banner = "com.onelouder.adlib.AdView";
    public final static String bannerPrefix = "com.onelouder.adlib";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.onelouder.adlib.AdView", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "setVisibility", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect onelouder AdView setVisibility in " + packageName);
                    
                    param.args[0] = View.GONE;
                }
                
            });
            
            final Method method = XposedHelpers.findMethodBestMatch(adView, "setVisibility", new Class[]{Integer.class});
            
            XposedBridge.hookAllMethods(adView, "addProxiedAdView", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect onelouder addProxiedAdView in " + packageName);
                    
                    
                    method.invoke(param.thisObject, View.GONE);
                    param.setResult(new Object());
                    
                }
                
            });
            
            XposedBridge.hookAllMethods(adView, "resume", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect onelouder AdView resume in " + packageName);
                    
                    
                    method.invoke(param.thisObject, View.GONE);
                    param.setResult(new Object());
                }
                
            });
            
            Class<?> Interad  = XposedHelpers.findClass("com.onelouder.adlib.AdInterstitial", lpparam.classLoader);
            XposedBridge.hookAllMethods(Interad, "displayInterstitial", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect onelouder AdInterstitial displayInterstitial in " + packageName);
                    param.setResult(new Object());
                }
                
            });
            
            
            Util.log(packageName, packageName + " uses onelouder");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
