package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Og {
    
    public final static String banner = "com.og.wa.AdWebView";
    public final static String bannerPrefix = "com.og.wa";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            final Class<?> adView = XposedHelpers.findClass("com.og.wa.AdWebView", lpparam.classLoader);
            final Class<?> webView = XposedHelpers.findClass("android.webkit.WebView", lpparam.classLoader);
            XposedBridge.hookAllMethods(webView, "loadUrl", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    if(adView.isInstance(param.thisObject)) {
                        Util.log(packageName, "Detect og AdWebView loadUrl in " + packageName);
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
                
            });
            Util.log(packageName, packageName + " uses Og AdWebView");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
