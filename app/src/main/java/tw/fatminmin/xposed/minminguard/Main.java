package tw.fatminmin.xposed.minminguard;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
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

    public static Class[] adNetworks = {
            Ad2iction.class, Adfurikun.class, AdMarvel.class, Admob.class, AdmobGms.class, Amazon.class,
            Amobee.class, AppBrain.class, Bonzai.class, Chartboost.class, Domob.class, Facebook.class, Flurry.class,
            GmsDoubleClick.class, Hodo.class, ImpAct.class, Inmobi.class, Intowow.class, KuAd.class, mAdserve.class,
            Madvertise.class, MasAd.class, MdotM.class, Millennial.class, Mobclix.class, MoPub.class, Nend.class,
            Og.class, Onelouder.class, OpenX.class, SmartAdserver.class, Smarti.class, Startapp.class, Tapfortap.class,
            TWMads.class, UnityAds.class, Vpadn.class, Vpon.class, Waystorm.class, Yahoo.class
    };

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

    private static boolean isAdView(Context context, String pkgName, String clazzName)
    {
        // corner case
        if(clazzName.startsWith("com.google.ads")) {
            return true;
        }
        for (Class network : adNetworks) {
            try {
                Field fBanner = network.getDeclaredField("banner");
                Field fBannerPrefix = network.getDeclaredField("bannerPrefix");

                String banner = (String) fBanner.get(null);
                String bannerPrefix = (String) fBannerPrefix.get(null);

                // prefix is used to detect adview obfuscate by proguard
                if(banner.equals(clazzName) || clazzName.startsWith(bannerPrefix))
                {
                    Util.notifyAdNetwork(context, pkgName, network.getSimpleName());
                    return true;
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static void clearAdViewInLayout(final String packageName, final View view) {
        
        if(isAdView(view.getContext(), packageName, view.getClass().getName())) {
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

                if(isAdView(v.getContext(), packageName, v.getClass().getName()))
                {
                    removeAdView(v, packageName, true);
                    Util.log(packageName, "Name based blocking: " + v.getClass().getName());
                }

            }
        });
    }
    
    private static void adNetwork(String packageName, LoadPackageParam lpparam, boolean test, Context context) {

        for (Class network : adNetworks) {
            try {
                // primitive boolean (Should not use Boolean.class)
                Method handleLoadPackage =
                        network.getDeclaredMethod("handleLoadPackage", String.class, LoadPackageParam.class, boolean.class);
                // invoke a static function
                Boolean result = (Boolean) handleLoadPackage.invoke(null, packageName, lpparam, test);
                if(result) {
                    Util.notifyAdNetwork(context, packageName, network.getSimpleName());
                }

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void appSpecific(String packageName, LoadPackageParam lpparam) {
        _2chMate.handleLoadPackage(packageName, lpparam, false);
        OneWeather.handleLoadPackage(packageName, lpparam, false);
    }

    public static void removeAdView(final View view, final String packageName, final boolean apiBased, final boolean first, final float heightLimit) {

        Util.notifyRemoveAdView(view.getContext(), packageName, 1);

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
