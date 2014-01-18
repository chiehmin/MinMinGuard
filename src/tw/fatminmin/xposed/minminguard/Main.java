package tw.fatminmin.xposed.minminguard;

import static de.robv.android.xposed.XposedHelpers.findClass;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import tw.fatminmin.xposed.minminguard.adnetwork.Admob;
import tw.fatminmin.xposed.minminguard.adnetwork.Amazon;
import tw.fatminmin.xposed.minminguard.adnetwork.Amobee;
import tw.fatminmin.xposed.minminguard.adnetwork.Flurry;
import tw.fatminmin.xposed.minminguard.adnetwork.Inmobi;
import tw.fatminmin.xposed.minminguard.adnetwork.KuAd;
import tw.fatminmin.xposed.minminguard.adnetwork.Madvertise;
import tw.fatminmin.xposed.minminguard.adnetwork.MoPub;
import tw.fatminmin.xposed.minminguard.adnetwork.Nend;
import tw.fatminmin.xposed.minminguard.adnetwork.OpenX;
import tw.fatminmin.xposed.minminguard.adnetwork.Vpon;
import tw.fatminmin.xposed.minminguard.adnetwork.mAdserve;
import tw.fatminmin.xposed.minminguard.custom_mod.ModTrain;
import tw.fatminmin.xposed.minminguard.custom_mod._2chMate;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit,
                             IXposedHookLoadPackage,
                             IXposedHookInitPackageResources {


    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    private static String MODULE_PATH = null;
    public static XSharedPreferences pref;
    private static Set<String> urls;
    private static Resources res;


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        
        pref = new XSharedPreferences(MY_PACKAGE_NAME);
        Util.pref = pref;
        
        MODULE_PATH = startupParam.modulePath;

        res = XModuleResources.createInstance(MODULE_PATH, null);
        byte[] array = XposedHelpers.assetAsByteArray(res, "host/output_file");
        String decoded = new String(array);
        String[] sUrls = decoded.split("\n");

        urls = new HashSet<String>();
        for(String url : sUrls) {
            urls.add(url);
        }
        XposedBridge.log("init");
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        pref.reload();

        final String packageName = lpparam.packageName;
        
        XposedBridge.log("packageName");
        
        if(pref.getBoolean(packageName, false)) {

            adNetwork(packageName, lpparam);
            appSpecific(packageName, lpparam);

            removeWebViewAds(packageName, lpparam, false);
        }
    }

    private static void adNetwork(String packageName, LoadPackageParam lpparam) {
        Admob.handleLoadPackage(packageName, lpparam, false);
        Amazon.handleLoadPackage(packageName, lpparam, false);
        Amobee.handleLoadPackage(packageName, lpparam, false);
        Flurry.handleLoadPackage(packageName, lpparam, false);
        KuAd.handleLoadPackage(packageName, lpparam, false);
        Inmobi.handleLoadPackage(packageName, lpparam, false);
        mAdserve.handleLoadPackage(packageName, lpparam, false);
        Madvertise.handleLoadPackage(packageName, lpparam, false);
        MoPub.handleLoadPackage(packageName, lpparam, false);
        Nend.handleLoadPackage(packageName, lpparam, false);
        OpenX.handleLoadPackage(packageName, lpparam, false);
        Vpon.handleLoadPackage(packageName, lpparam, false);
    }

    private static void appSpecific(String packageName, LoadPackageParam lpparam) {
        _2chMate.handleLoadPackage(packageName, lpparam, false);
    }


    static boolean adExist = false;
    static private boolean removeWebViewAds(final String packageName, LoadPackageParam lpparam, final boolean test) {


        try {

            Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);

            XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String data = (String) param.args[0];
                    adExist = urlFiltering("", data, param, packageName, test);
                }

            });

            XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    String url = (String) param.args[0];
                    String data = (String) param.args[1];
                    adExist = urlFiltering(url, data, param, packageName, test);
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

                if(urls.contains(hostname)) {

                    Util.log(packageName, "Detect Ads(url) with hostname: " + hostname + " in " + packageName);
                    if(!test) {
                        param.setResult(new Object());
                        removeAdView((View) param.thisObject, packageName, false);
                        return true;
                    }
                    break;
                }
            }
        }

        if(pref.getBoolean(packageName + "_url", true)) {
            
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

                    if(urls.contains(hostname)) {

                        Util.log(packageName, "Detect Ads(data) with hostname: " + hostname + " in " + packageName);
                        if(!test) {
                            param.setResult(new Object());
                            removeAdView((View) param.thisObject, packageName, false);
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }

    public static void removeAdView(View view, final String packageName, final boolean apiBased) {

        view.setVisibility(View.GONE);

        final ViewParent parent = view.getParent();
        if(parent instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) parent;
            if(apiBased && vg.getChildCount() == 1 && pref.getBoolean(packageName + "_recursive", false)) {
                removeAdView(vg, packageName, apiBased);
            }
            else if(!apiBased){

                ViewTreeObserver observer= vg.getViewTreeObserver();
                observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        float heightDp = convertPixelsToDp(vg.getHeight()); 
                        if(heightDp <= 55) {
                            vg.removeAllViews();
                        }
    	            }
                });
            }
        }

    }

    private static float convertPixelsToDp(float px){
        DisplayMetrics metrics = res.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    @Override
    public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
        new ModTrain().modLayout(resparam);
    }
}
