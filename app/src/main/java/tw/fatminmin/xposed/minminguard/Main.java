package tw.fatminmin.xposed.minminguard;

import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.NameBlocking;
import tw.fatminmin.xposed.minminguard.blocker.UrlFiltering;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Ad2iction;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AdMarvel;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adbert;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adfurikun;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Admob;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AdmobGms;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amazon;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amobee;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AppBrain;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Bonzai;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Chartboost;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Clickforce;
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
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.MobFox;
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
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.NextMedia;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.OneWeather;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.PeriodCalendar;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod._2chMate;
import android.app.Activity;
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
                             IXposedHookLoadPackage,
                             IXposedHookInitPackageResources{


    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static String MODULE_PATH = null;
    public static XSharedPreferences pref;
    public static Set<String> patterns;
    public static Resources res;

    public static Blocker[] blockers = {
            /* Popular adnetwork */
            new Ad2iction(), new Adbert(), new Adfurikun(), new AdMarvel(), new Admob(), new AdmobGms(), new Amazon(), new Amobee(), new AppBrain(), new Bonzai(), new Chartboost(), new Clickforce(), new Domob(), new Facebook(), new Flurry(), new GmsDoubleClick(), new Hodo(), new ImpAct(), new Inmobi(), new Intowow(), new KuAd(), new mAdserve(), new Madvertise(), new MasAd(), new MdotM(), new Millennial(), new Mobclix(), new MobFox(), new MoPub(), new Nend(), new Og(), new Onelouder(), new OpenX(), new SmartAdserver(), new Smarti(), new Startapp(), new Tapfortap(), new TWMads(), new UnityAds(), new Vpadn(), new Vpon(), new Waystorm(), new Yahoo()
            /* Custom Mod*/
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

        patterns = new HashSet<>();
        for(String url : sUrls) {
            patterns.add(url);
        }
        array = XposedHelpers.assetAsByteArray(res, "host/mmg_pattern");
        decoded = new String(array);
        sUrls = decoded.split("\n");
        for(String url : sUrls) {
            patterns.add(url);
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


        if (pref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false) || pref.getBoolean(packageName, false)) {

            // Api based blocking
            ApiBlocking.handle(packageName, lpparam, true);
            appSpecific(packageName, lpparam);

            // Name based blocking
            NameBlocking.nameBasedBlocking(packageName, lpparam);

            // url filtering
            if (Main.pref.getBoolean(packageName + "_url", false)) {
                UrlFiltering.removeWebViewAds(packageName, lpparam);
            }

        } else {
            ApiBlocking.handle(packageName, lpparam, false);
        }
    }



    private static void appSpecific(String packageName, LoadPackageParam lpparam) {
        _2chMate.handleLoadPackage(packageName, lpparam, true);
        OneWeather.handleLoadPackage(packageName, lpparam, true);
        //NextMedia.handleLoadPackage(packageName, lpparam, true);
    }

    public static void removeAdView(View view, String packageName, boolean remove) {

        if (remove) {
            Util.notifyRemoveAdView(view.getContext(), packageName, 1);
            removeAdView(view, packageName, true, 51);
        }
    }

    public static void removeAdView(final View view, final String packageName, final boolean first, final float heightLimit) {

        float adHeight = convertPixelsToDp(view.getHeight());

        if(first || (adHeight > 0 && adHeight <= heightLimit)) {

            LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
            }
            else {
                params.height = 0;
            }
            view.setLayoutParams(params);
        }
        /*
            adview may not have been created
            reference: http://stackoverflow.com/questions/8170915/getheight-returns-0-for-all-android-ui-objects
        */
        ViewTreeObserver observer= view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float heightDp = convertPixelsToDp(view.getHeight());
                if (first || heightDp <= heightLimit) {

                    LayoutParams params = view.getLayoutParams();
                    if (params == null) {
                        params = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
                    }
                    else {
                        params.height = 0;
                    }
                    view.setLayoutParams(params);
                }
            }
        });

        if(view.getParent() != null && view.getParent() instanceof ViewGroup) {
            float currentLimit = heightLimit;
            ViewGroup parent = (ViewGroup) view.getParent();
            if(first)
            {
                currentLimit = Math.max(adHeight + 5, currentLimit);
                /*
                  Some adview(AdbertAdview) still showing after set height to 0
                  In this case, I have to set its visibility to GONE
                 */
                if(parent.getChildCount() == 1) {
                    view.setVisibility(View.GONE);
                }
            }
            removeAdView(parent, packageName, false, currentLimit);
        }
    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = res.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {

        pref.reload();

        final String packageName = resparam.packageName;

        if (pref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false) || pref.getBoolean(packageName, false)) {
            PeriodCalendar.handleInitPackageResources(resparam);
        }
    }
}
