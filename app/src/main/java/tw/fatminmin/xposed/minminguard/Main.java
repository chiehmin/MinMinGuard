package tw.fatminmin.xposed.minminguard;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adtech;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amazon;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amobee;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Aotter;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AppBrain;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Avocarrot;
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
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Yandex;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.mAdserve;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.NextMedia;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.OneWeather;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.PeriodCalendar;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod._2chMate;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.crossbowffs.remotepreferences.RemotePreferences;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookZygoteInit,
                             IXposedHookLoadPackage {


    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static String MODULE_PATH = null;
    public static Set<String> patterns;
    public static Resources res;

    public static Blocker[] blockers = {
            /* Popular adnetwork */
            new Ad2iction(), new Adbert(), new Adfurikun(), new AdMarvel(), new Admob(), new AdmobGms(), new Adtech(), new Amazon(), new Amobee(), new Aotter(), new AppBrain(), new Avocarrot(), new Bonzai(), new Chartboost(), new Clickforce(), new Domob(), new Facebook(), new Flurry(), new GmsDoubleClick(), new Hodo(), new ImpAct(), new Inmobi(), new Intowow(), new KuAd(), new mAdserve(), new Madvertise(), new MasAd(), new MdotM(), new Millennial(), new Mobclix(), new MobFox(), new MoPub(), new Nend(), new Og(), new Onelouder(), new OpenX(), new SmartAdserver(), new Smarti(), new Startapp(), new Tapfortap(), new TWMads(), new UnityAds(), new Vpadn(), new Vpon(), new Waystorm(), new Yahoo(), new Yandex()
            /* Custom Mod*/
    };

    public static ExecutorService notifyWorker;

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
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

        notifyWorker = Executors.newSingleThreadExecutor();
    }

    private static boolean isEnabled(SharedPreferences pref, String pkgName) {
        String mode = pref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST);

        if(mode.equals(Common.VALUE_MODE_AUTO)) {
            return true;
        } else if(mode.equals(Common.VALUE_MODE_BLACKLIST)) {
            return pref.getBoolean(pkgName, false);
        } else {
            /*
                com.joshua.jptt loads com.google.android.gms
                com.joshua.jptt D/MinMinGuard: com.google.android.gms_whitelist

                com.joshua.jptt D/MinMinGuard: parent pkgName: com.joshua.jptt
                com.joshua.jptt D/MinMinGuard: current pkgName: com.google.android.gms
             */
            return !pref.getBoolean(Common.getWhiteListKey(pkgName), false);
        }
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {

        if (lpparam.packageName.equals(MY_PACKAGE_NAME)) {
            Class<?> util = XposedHelpers.findClass("tw.fatminmin.xposed.minminguard.blocker.Util", lpparam.classLoader);
            XposedBridge.hookAllMethods(util, "xposedEnabled", XC_MethodReplacement.returnConstant(true));
        }

        /**
         * https://developer.android.com/reference/android/app/Application.html#onCreate()
         * wait for the app started to get remote preferences
         */
        findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                final String packageName = lpparam.packageName;
                Context context = Util.getCurrentApplication();

                if (null == context) {
                    Util.log(packageName, "failed to get context");
                    return;
                }

                SharedPreferences pref = new RemotePreferences(
                        context,
                        "tw.fatminmin.xposed.minminguard.modesettings",
                        Common.MOD_PREFS);

                if (isEnabled(pref, packageName)) {
                    Util.log(packageName, "is enabled for mmg");

                    // Api based blocking
                    ApiBlocking.handle(packageName, lpparam, true);
                    appSpecific(packageName, lpparam);

                    // Name based blocking
                    NameBlocking.nameBasedBlocking(packageName, lpparam);

                    // url filtering
                    if (pref.getBoolean(packageName + "_url", false)) {
                        UrlFiltering.removeWebViewAds(packageName, lpparam);
                    }

                } else {
                    // ApiBlocking.handle(packageName, lpparam, false);
                }
            }
        });

    }

    private static void appSpecific(String packageName, LoadPackageParam lpparam) {
        _2chMate.handleLoadPackage(packageName, lpparam, true);
        OneWeather.handleLoadPackage(packageName, lpparam, true);
        NextMedia.handleLoadPackage(packageName, lpparam, true);
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

        // preventing view not ready situation
        view.post(new Runnable() {
            @Override
            public void run() {
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

            }
        });
        if(view.getParent() != null && view.getParent() instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view.getParent();
            removeAdView(parent, packageName, false, heightLimit);
        }
    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = res.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

//    @Override
//    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
//
//        pref.reload();
//
//        final String packageName = resparam.packageName;
//
//        if (isEnabled(packageName)) {
//            PeriodCalendar.handleInitPackageResources(resparam);
//        }
//    }
}
