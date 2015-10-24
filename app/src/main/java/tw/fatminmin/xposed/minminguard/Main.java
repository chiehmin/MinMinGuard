package tw.fatminmin.xposed.minminguard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XC_MethodReplacement;
import tw.fatminmin.xposed.minminguard.blocker.HostBlock;
import tw.fatminmin.xposed.minminguard.blocker.UrlFiltering;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Ad2iction;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AdMarvel;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adfurikun;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Admob;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AdmobGms;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amazon;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amobee;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AppBrain;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Bonzai;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Chartboost;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Domob;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Facebook;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Flurry;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.GmsDoubleClick;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Hodo;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.ImpAct;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Inmobi;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Intowow;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.KuAd;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Madvertise;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.MasAd;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.MdotM;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Millennial;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.MoPub;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Mobclix;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Nend;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Og;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Onelouder;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.OpenX;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.SmartAdserver;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Smarti;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Startapp;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.TWMads;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Tapfortap;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.UnityAds;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Vpadn;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Vpon;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Waystorm;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Yahoo;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.mAdserve;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.OneWeather;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod._2chMate;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit,
                             IXposedHookLoadPackage {


    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static String MODULE_PATH = null;
    public static XSharedPreferences pref;
    public static Set<String> urls;
    public static Resources res;


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {

        pref = new XSharedPreferences(MY_PACKAGE_NAME, Common.MOD_PREFS);
        pref.makeWorldReadable();
        Util.pref = pref;

        MODULE_PATH = startupParam.modulePath;

        res = XModuleResources.createInstance(MODULE_PATH, null);
        byte[] array = XposedHelpers.assetAsByteArray(res, "host/output_file");
        String decoded = new String(array);
        String[] sUrls = decoded.split("\n");

        urls = new HashSet<>();
        for(String url : sUrls) {
            urls.add(url);
        }
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals(MY_PACKAGE_NAME)) {
            Class<?> util = XposedHelpers.findClass("tw.fatminmin.xposed.minminguard.blocker.Util", lpparam.classLoader);
            XposedBridge.hookAllMethods(util, "xposedEnabled", XC_MethodReplacement.returnConstant(true));
        }

        pref.reload();

        final String packageName = lpparam.packageName;

        if (pref.getBoolean(packageName + "_host", false)) {
            Util.log(packageName, packageName + " is using host blocking now");
            HostBlock.block(lpparam);
        }

        Class<?> activity = XposedHelpers.findClass("android.app.Activity", lpparam.classLoader);
        XposedBridge.hookAllMethods(activity, "onCreate", new XC_MethodHook() {
           @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                Context context = (Context) param.thisObject;
                
                if(pref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false) || pref.getBoolean(packageName, false)) {
                    adNetwork(packageName, lpparam, false, context);
                    appSpecific(packageName, lpparam);

                    if(Main.pref.getBoolean(packageName + "_url", false))
                    {
                        UrlFiltering.removeWebViewAds(packageName, lpparam, false);
                    }
                    
                    nameBasedBlocking(packageName, lpparam);
                    
                }
                else {
                    adNetwork(packageName, lpparam, true, context);
                }
            }  
        });    
        if(pref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false) || pref.getBoolean(packageName, false)) {
            XposedBridge.hookAllMethods(activity, "setContentView", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Activity ac = (Activity)(param.thisObject);
                    ViewGroup root = (ViewGroup) ac.getWindow().getDecorView().findViewById(android.R.id.content);
                    clearAdViewInLayout(packageName, root);
                }
            });
        }
    }
    
    static final ArrayList<String> banners = new ArrayList<String>(Arrays.asList(
        Ad2iction.banner, Adfurikun.banner, AdMarvel.banner, Admob.banner, AdmobGms.banner, Amazon.banner, Amobee.banner, AppBrain.banner, Bonzai.banner,
        Chartboost.banner, Domob.banner, Facebook.banner, Flurry.banner, GmsDoubleClick.banner, Hodo.banner, ImpAct.banner, Inmobi.banner, Intowow.banner, KuAd.banner, mAdserve.banner,
        Madvertise.banner, MasAd.banner, MdotM.banner, Millennial.banner, Mobclix.banner, MoPub.banner, Nend.banner, Og.banner,  
        Onelouder.banner, OpenX.banner, SmartAdserver.banner, Smarti.banner, Startapp.banner, Tapfortap.banner, TWMads.banner, Vpadn.banner,
        Vpon.banner, Waystorm.banner, Yahoo.banner));
    static final ArrayList<String> bannerPrefix = new ArrayList<String>(Arrays.asList(
        Ad2iction.bannerPrefix, Adfurikun.bannerPrefix, AdMarvel.bannerPrefix, Admob.bannerPrefix, AdmobGms.bannerPrefix, Amazon.bannerPrefix, Amobee.bannerPrefix, Bonzai.bannerPrefix,
        Chartboost.bannerPrefix, Domob.bannerPrefix, Facebook.bannerPrefix, Flurry.bannerPrefix, GmsDoubleClick.bannerPrefix, Hodo.bannerPrefix, ImpAct.bannerPrefix, Inmobi.bannerPrefix, Intowow.bannerPrefix, KuAd.bannerPrefix, mAdserve.bannerPrefix,
        Madvertise.bannerPrefix, MasAd.bannerPrefix, MdotM.bannerPrefix, Millennial.bannerPrefix, Mobclix.bannerPrefix, MoPub.bannerPrefix, Nend.bannerPrefix, Og.bannerPrefix,
        Onelouder.bannerPrefix, OpenX.bannerPrefix, SmartAdserver.bannerPrefix, Smarti.bannerPrefix, Startapp.bannerPrefix, Tapfortap.bannerPrefix, TWMads.bannerPrefix, Vpadn.bannerPrefix,
        Vpon.bannerPrefix, Waystorm.bannerPrefix, Yahoo.bannerPrefix));
    static
    {
        bannerPrefix.add("com.google.ads");
    }

    private static boolean isAdView(String name)
    {
        if(banners.contains(name))
        {
            return true;
        }
        // detect adview obfuscate by proguard
        for(String prefix : bannerPrefix)
        {
            if(name.startsWith(prefix))
            {
                return true;
            }
        }
        return false;
    }

    private static void clearAdViewInLayout(final String packageName, final View view) {
        
        if(isAdView(view.getClass().getName())) {
            removeAdView(view, packageName, true);
            Util.log(packageName, "clearAdViewInLayout: " + view.getClass().getName());
        }
        
        if(view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for(int i = 0; i < vg.getChildCount(); i++) {
                clearAdViewInLayout(packageName, vg.getChildAt(i));
            }
        }
    }
    
    private static void nameBasedBlocking(final String packageName, final LoadPackageParam lpparam) {
        
        Class<?> viewGroup = XposedHelpers.findClass("android.view.ViewGroup", lpparam.classLoader);
        XposedBridge.hookAllMethods(viewGroup, "addView", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                View v = (View) param.args[0];

                if(isAdView(v.getClass().getName()))
                {
                    removeAdView(v, packageName, true);
                    Util.log(packageName, "Name based blocking: " + v.getClass().getName());
                }

            }
        });
    }
    
    private static void adNetwork(String packageName, LoadPackageParam lpparam, boolean test, Context context) {

        if(Adfurikun.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Adfurikun");
        }
        if(AdMarvel.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "AdMarvel");
        }
        if(Admob.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "AdMob");
        }
        if(AdmobGms.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "AdMobGms");
        }
        if(Amazon.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Amazon");
        }
        if(Amobee.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Amobee");
        }
        if(AppBrain.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "AppBrain");
        }
        if(Bonzai.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Bonzai");
        }
        if(Chartboost.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Chartboost");
        }
        if(Domob.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Domob");
        }
        if(Facebook.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Facebook");
        }
        if(Flurry.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Flurry");
        }
        if(GmsDoubleClick.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "GmsDoubleClick");
        }
        if(Hodo.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "HODo");
        }
        if(Inmobi.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Inmobi");
        }
        if(Intowow.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Intowow");
        }
        if(KuAd.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "KuAd");
        }
        if(mAdserve.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "mAdserve");
        }
        if(Madvertise.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Madvertise");
        }
        if(MasAd.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "MasAd");
        }
        if(MdotM.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "MdotM");
        }
        if(Millennial.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Millennial");
        }
        if(Mobclix.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Mobclix");
        }
        if(MoPub.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "MoPub");
        }
        if(Nend.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Nend");
        }
        if(Og.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Og");
        }
        if(Onelouder.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Onelouder");
        }
        if(OpenX.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "OpenX");
        }
        if(SmartAdserver.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "SmartAdserver");
        }
        if(Startapp.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Startapp");
        }
        if(Tapfortap.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Tapfortap");
        }
        if(TWMads.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "TWMads");
        }
        if(UnityAds.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "UnityAds");
        }
        if(Vpadn.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Vpadn");
        }
        if(Vpon.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Vpon");
        }
        if(Waystorm.handleLoadPackage(packageName, lpparam, test)) {
            Util.notifyAdNetwork(context, packageName, "Waystorm");
        }
    }

    private static void appSpecific(String packageName, LoadPackageParam lpparam) {
        _2chMate.handleLoadPackage(packageName, lpparam, false);
        OneWeather.handleLoadPackage(packageName, lpparam, false);
    }

    public static void removeAdView(final View view, final String packageName, final boolean apiBased, final boolean first, final float heightLimit) {

        float adHeight = convertPixelsToDp(view.getHeight());

        if(first || (adHeight > 0 && adHeight <= heightLimit)) {

            LayoutParams params = view.getLayoutParams();
            params.height = 0;
            view.setLayoutParams(params);
        }
        ViewTreeObserver observer= view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float heightDp = convertPixelsToDp(view.getHeight());
                if(heightDp <= heightLimit) {

                    LayoutParams params = view.getLayoutParams();
                    params.height = 0;
                    view.setLayoutParams(params);
                }
            }
        });

        if(view.getParent() != null && view.getParent() instanceof ViewGroup) {
            float currentLimit = heightLimit;
            if(first)
            {
                currentLimit = Math.max(adHeight + 5, currentLimit);
            }
            removeAdView((View)view.getParent(), packageName, apiBased, false, currentLimit);
        }
    }

    public static void removeAdView(final View view, final String packageName, final boolean apiBased) {
        removeAdView(view, packageName, apiBased, true, 50);
    }

    private static float convertPixelsToDp(float px){
        DisplayMetrics metrics = res.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }
}
