package tw.fatminmin.xposed.minminguard;

import java.util.HashSet;
import java.util.Set;

import tw.fatminmin.xposed.minminguard.adnetwork.AdMarvel;
import tw.fatminmin.xposed.minminguard.adnetwork.Admob;
import tw.fatminmin.xposed.minminguard.adnetwork.Amazon;
import tw.fatminmin.xposed.minminguard.adnetwork.Amobee;
import tw.fatminmin.xposed.minminguard.adnetwork.Bonzai;
import tw.fatminmin.xposed.minminguard.adnetwork.Flurry;
import tw.fatminmin.xposed.minminguard.adnetwork.Inmobi;
import tw.fatminmin.xposed.minminguard.adnetwork.KuAd;
import tw.fatminmin.xposed.minminguard.adnetwork.Madvertise;
import tw.fatminmin.xposed.minminguard.adnetwork.MdotM;
import tw.fatminmin.xposed.minminguard.adnetwork.Millennial;
import tw.fatminmin.xposed.minminguard.adnetwork.MoPub;
import tw.fatminmin.xposed.minminguard.adnetwork.Nend;
import tw.fatminmin.xposed.minminguard.adnetwork.Og;
import tw.fatminmin.xposed.minminguard.adnetwork.Onelouder;
import tw.fatminmin.xposed.minminguard.adnetwork.OpenX;
import tw.fatminmin.xposed.minminguard.adnetwork.SmartAdserver;
import tw.fatminmin.xposed.minminguard.adnetwork.Startapp;
import tw.fatminmin.xposed.minminguard.adnetwork.TWMads;
import tw.fatminmin.xposed.minminguard.adnetwork.Vpon;
import tw.fatminmin.xposed.minminguard.adnetwork.mAdserve;
import tw.fatminmin.xposed.minminguard.custom_mod.Backgrounds;
import tw.fatminmin.xposed.minminguard.custom_mod.OneWeather;
import tw.fatminmin.xposed.minminguard.custom_mod.Train;
import tw.fatminmin.xposed.minminguard.custom_mod._2chMate;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.RelativeLayout;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit,
                             IXposedHookLoadPackage,
                             IXposedHookInitPackageResources {


    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static String MODULE_PATH = null;
    public static XSharedPreferences pref;
    public static Set<String> urls;
    public static Resources res;


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
    }

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {

        pref.reload();

        final String packageName = lpparam.packageName;
        
        if(pref.getBoolean(packageName, false)) {

            adNetwork(packageName, lpparam);
            appSpecific(packageName, lpparam);

            UrlFiltering.removeWebViewAds(packageName, lpparam, false);
        }
    }

    private static void adNetwork(String packageName, LoadPackageParam lpparam) {
        AdMarvel.handleLoadPackage(packageName, lpparam, false);
        Admob.handleLoadPackage(packageName, lpparam, false);
        Amazon.handleLoadPackage(packageName, lpparam, false);
        Amobee.handleLoadPackage(packageName, lpparam, false);
        Bonzai.handleLoadPackage(packageName, lpparam, false);
        Flurry.handleLoadPackage(packageName, lpparam, false);
        Inmobi.handleLoadPackage(packageName, lpparam, false);
        KuAd.handleLoadPackage(packageName, lpparam, false);
        mAdserve.handleLoadPackage(packageName, lpparam, false);
        Madvertise.handleLoadPackage(packageName, lpparam, false);
        MdotM.handleLoadPackage(packageName, lpparam, false);
        Millennial.handleLoadPackage(packageName, lpparam, false);
        MoPub.handleLoadPackage(packageName, lpparam, false);
        Nend.handleLoadPackage(packageName, lpparam, false);
        Og.handleLoadPackage(packageName, lpparam, false);
        Onelouder.handleLoadPackage(packageName, lpparam, false);
        OpenX.handleLoadPackage(packageName, lpparam, false);
        SmartAdserver.handleLoadPackage(packageName, lpparam, false);
        Startapp.handleLoadPackage(packageName, lpparam, false);
        TWMads.handleLoadPackage(packageName, lpparam, false);
        Vpon.handleLoadPackage(packageName, lpparam, false);
    }

    private static void appSpecific(String packageName, LoadPackageParam lpparam) {
        _2chMate.handleLoadPackage(packageName, lpparam, false);
        OneWeather.handleLoadPackage(packageName, lpparam, false);
    }

    public static void removeAdView(final View view, final String packageName, final boolean apiBased) {
        
        if(convertPixelsToDp(view.getHeight()) > 0 && convertPixelsToDp(view.getHeight()) <= 55) {
            view.setVisibility(View.GONE);
        }
        else {
            ViewTreeObserver observer= view.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    float heightDp = convertPixelsToDp(view.getHeight());
                    if(heightDp <= 55) {
                        view.setVisibility(View.GONE);
                    }
                }
            });
        }
        
        final ViewParent parent = view.getParent();
        if(parent instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) parent;
            final boolean recursive = pref.getBoolean(packageName + "_recursive", false);
            final boolean relative = vg.getLayoutParams() instanceof RelativeLayout.LayoutParams;
            
            
            if(recursive || !relative) {
                removeAdView(vg, packageName, apiBased);
            }
            else {
                for(int i = 0; i < vg.getChildCount(); i++) {
                    vg.getChildAt(i).setVisibility(View.GONE);
                }
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
        Backgrounds.handleInitPackageResources(resparam);
        Train.handleInitPackageResources(resparam);
    }
}
