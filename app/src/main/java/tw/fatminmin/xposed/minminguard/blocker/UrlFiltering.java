package tw.fatminmin.xposed.minminguard.blocker;

import static de.robv.android.xposed.XposedHelpers.findClass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Main;

public class UrlFiltering {
    private static boolean adExist = false;

    private UrlFiltering() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    static public boolean removeWebViewAds(final String packageName, LoadPackageParam lpparam) {

        try {

            Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);
            
            
            XposedBridge.hookAllMethods(adView, "loadUrl", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    String url = (String) param.args[0];
                    adExist = urlFiltering(url, "", param, packageName);
                    if(adExist) {
                        param.setResult(new Object());
                    }
                }
            });
            
            XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String data = (String) param.args[0];
                    adExist = urlFiltering("", data, param, packageName);
                    if(adExist) {
                        param.setResult(new Object());
                    }
                }

            });

            XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String url = (String) param.args[0];
                    String data = (String) param.args[1];
                    adExist = urlFiltering(url, data, param, packageName);
                    if(adExist) {
                        param.setResult(new Object());
                    }
                }
            });

        }
        catch(ClassNotFoundError e) {
            Util.log(packageName, packageName + "can not clear webview ads");
            return false;
        }
        return adExist;
    }

    static private boolean urlFiltering(String url, String data, MethodHookParam param, String packageName) {


        Util.log(packageName, "Url filtering");

        if(url == null) 
            url = "";
        
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        
        Util.log(packageName, packageName + " url:\n" + url);

        for(String adUrl : Main.patterns) {
            if(url.contains(adUrl)) {
                Util.log(packageName, "Detect " + packageName + " load url from " + adUrl);
                param.setResult(new Object());
                Main.removeAdView((View) param.thisObject, packageName, true);
                return true;
            }
        }

        Util.log(packageName, packageName + " data:\n" + data);

        try {
            data = URLDecoder.decode(data, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(String adUrl : Main.patterns) {
            if(data.contains(adUrl)) {
                Util.log(packageName, "Detect " + packageName + " load data from " + adUrl);
                param.setResult(new Object());
                Main.removeAdView((View) param.thisObject, packageName, true);
                return true;
            }
        }

        return false;
    }
}
