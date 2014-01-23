package tw.fatminmin.xposed.minminguard;

import static de.robv.android.xposed.XposedHelpers.findClass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class UrlFiltering {
    static boolean adExist = false;
    static public boolean removeWebViewAds(final String packageName, LoadPackageParam lpparam, final boolean test) {

        try {

            Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);
            
            
            XposedBridge.hookAllMethods(adView, "loadUrl", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    String url = (String) param.args[0];
                    adExist = urlFiltering(url, "", param, packageName, test);
                    if(adExist && !test) {
                        param.setResult(new Object());
                    }
                }
            });
            
            XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String data = (String) param.args[0];
                    adExist = urlFiltering("", data, param, packageName, test);
                    if(adExist && !test) {
                        param.setResult(new Object());
                    }
                }

            });

            XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String url = (String) param.args[0];
                    String data = (String) param.args[1];
                    adExist = urlFiltering(url, data, param, packageName, test);
                    if(adExist && !test) {
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

    static private boolean urlFiltering(String url, String data, MethodHookParam param, String packageName, boolean test) {


        Util.log(packageName, "Url filtering");
        String[] array;

        if(url == null) 
            url = "";
        
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        array = url.split("[/\\s):]");
        
        Util.log(packageName, packageName + " url:\n" + url);
        
        for(String hostname : array) {

            hostname = hostname.trim();

            if(hostname.contains(".") &&  hostname.length() > 5 && hostname.length() < 50) {

                if(Main.urls.contains(hostname)) {

                    Util.log(packageName, "Detect Ads(url) with hostname: " + hostname + " in " + packageName);
                    if(!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, false);
                        return true;
                    }
                    break;
                }
            }
        }

        if(Main.pref.getBoolean(packageName + "_url", true)) {
            
            try {
                data = URLDecoder.decode(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            
            Util.log(packageName, packageName + " data:\n" + data);
            
            array = data.split("[/\\s):]");

            for(String hostname : array) {

                hostname = hostname.trim();

                if(hostname.contains(".") &&  hostname.length() > 5 && hostname.length() < 50) {

                    if(Main.urls.contains(hostname)) {

                        Util.log(packageName, "Detect Ads(data) with hostname: " + hostname + " in " + packageName);
                        if(!test) {
                            param.setResult(new Object());
                            Main.removeAdView((View) param.thisObject, packageName, false);
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }
}
