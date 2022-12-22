package tw.fatminmin.xposed.minminguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.os.Build;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.NameBlocking;
import tw.fatminmin.xposed.minminguard.blocker.UrlFiltering;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Ad2iction;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AdMarvel;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adbert;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adcolony;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adfurikun;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Adtech;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amazon;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Amobee;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Aotter;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AppBrain;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Applovin;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Appnext;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.AppodealMRAID;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Avocarrot;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Bonzai;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Chartboost;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Clickforce;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Domob;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Facebook;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Flurry;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Freewheel;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.GoogleAdmob;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.GoogleGms;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.GoogleGmsDoubleClick;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Hodo;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Inmobi;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Intowow;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Ironsource;
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
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.SourcekitMRAID;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Startapp;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.TWMads;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Tapfortap;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.UnityAds;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Vpadn;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Vpon;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Vungle;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Waystorm;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.Yandex;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.mAdserve;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.NextMedia;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.OneWeather;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.Viafree;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod._2chMate;

public class Main implements IXposedHookZygoteInit, IXposedHookLoadPackage
{
    private static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    private static String MODULE_PATH;
    public static Set<String> patterns = new HashSet<>();
    public static Resources resources;
    public static Integer xposedVersionCode;
    private SharedPreferences pref;

    public static Blocker[] blockers = {
            /* Popular adnetwork */
            new Ad2iction(), new Adbert(), new Adcolony(), new Adfurikun(), new AdMarvel(), new AppodealMRAID(), new GoogleAdmob(), new GoogleGms(), new Adtech(), new Amazon(), new Amobee(),
            new Aotter(), new AppBrain(), new Applovin(), new Appnext(), new Avocarrot(), new Bonzai(), new Chartboost(), new Clickforce(), new Domob(), new Facebook(),
            new Freewheel(), new Flurry(), new GoogleGmsDoubleClick(), new Hodo(), new Inmobi(), new Intowow(), new Ironsource(), new KuAd(), new mAdserve(), new Madvertise(),
            new MasAd(), new MdotM(), new Millennial(), new Mobclix(), new MobFox(), new MoPub(), new Nend(), new Og(), new Onelouder(), new OpenX(), new SmartAdserver(), new SourcekitMRAID(),
            new Startapp(), new Tapfortap(), new TWMads(), new UnityAds(), new Vpadn(), new Vpon(), new Vungle(), new Waystorm(), new Yandex(),
            /* Custom Mod*/
    };

    private static boolean isEnabled(SharedPreferences pref, String pkgName)
    {
        String mode = pref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST);

        if (mode.equals(Common.VALUE_MODE_AUTO))
        {
            return true;
        }
        else if (mode.equals(Common.VALUE_MODE_BLACKLIST))
        {
            return pref.getBoolean(pkgName, false);
        }
        else
        {
            return !pref.getBoolean(Common.getWhiteListKey(pkgName), false);
        }
    }

    private static void appSpecific(String packageName, LoadPackageParam lpparam)
    {
        _2chMate.handleLoadPackage(packageName, lpparam);
        OneWeather.handleLoadPackage(packageName, lpparam);
        NextMedia.handleLoadPackage(packageName, lpparam);
        Viafree.handleLoadPackage(packageName, lpparam);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void initZygote(StartupParam startupParam) throws Throwable
    {
        try
        {
            xposedVersionCode = XposedBridge.getXposedVersion();
        }
        catch(Exception e)
        {
            xposedVersionCode = XposedBridge.XPOSED_BRIDGE_VERSION;
        }

        MODULE_PATH = startupParam.modulePath;

        if(xposedVersionCode < 90)
            UnpackResources();
        else
            Util.log(MY_PACKAGE_NAME, "Skipping resource unpacking for now");

        Util.notifyWorker = Executors.newSingleThreadExecutor();

        if (xposedVersionCode < 93) {
            /* This doesn't work in LSPosed API >=93 because prefs are stored in a random directory
               https://github.com/LSPosed/LSPosed/wiki/New-XSharedPreferences#for-the-hooked-app */
            String dataDir = "data/";
            if (Build.VERSION.SDK_INT > 23) dataDir = "user_de/0/";

            File f = new File("/data/" + dataDir + MY_PACKAGE_NAME + "/shared_prefs/" + Common.MOD_PREFS + ".xml");
            pref = new XSharedPreferences(f);
            Util.log(MY_PACKAGE_NAME, "Using LEGACY XSharedPreferences");
        } else {
            pref = new XSharedPreferences(MY_PACKAGE_NAME, Common.MOD_PREFS);
            Util.log(MY_PACKAGE_NAME, "Using NEW XSharedPreferences");
        }
    }

    private void UnpackResources()
    {
        try
        {
            resources = XModuleResources.createInstance(MODULE_PATH, null);
            byte[] array = XposedHelpers.assetAsByteArray(resources, "host/output_file");
            String decoded = new String(array);
            String[] sUrls = decoded.split("\n");

            Collections.addAll(patterns, sUrls);

            array = XposedHelpers.assetAsByteArray(resources, "host/mmg_pattern");
            decoded = new String(array);
            sUrls = decoded.split("\n");

            Collections.addAll(patterns, sUrls);
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam)
    {
        if (lpparam.packageName.equals(MY_PACKAGE_NAME)) {
            XposedHelpers.findAndHookMethod("tw.fatminmin.xposed.minminguard.blocker.Util", lpparam.classLoader, "xposedEnabled", XC_MethodReplacement.returnConstant(true));
            if (xposedVersionCode >= 93)
                XposedHelpers.findAndHookMethod("tw.fatminmin.xposed.minminguard.Common", lpparam.classLoader, "getPrefMode", XC_MethodReplacement.returnConstant(Context.MODE_WORLD_READABLE));
        }

        /**
         * https://developer.android.com/about/versions/11/privacy/package-visibility
         * API30 adds new restrictions limiting apps from seeing our custom content provider at content://tw.fatminmin.xposed.minminguard/
         * used to report back ad networks and removed ads.
         *
         * To get around this the target app needs:
         * ...
         *    <queries>
         *        <package android:name="tw.fatminmin.xposed.minminguard" />
         *    </queries>
         * ...
         * OR
         * ...
         *    <uses-permission android:name="android.permission.QUERY_ALL_PACKAGES"
         * ...
         * in its AndroidManifest.xml, this hack does tha latter.
         *
         * Note that this implies a) hooking System Framework in LSposed Manager and b) needs a reboot after enabling an app in MinMinGuard
         * because only android can access com.android.server.pm.permission.PermissionManagerService !!!
         *
         * The implementation is based on Mino260806's snippet (Thanks!)
         * https://forum.xda-developers.com/t/xposed-for-devs-how-to-dynamically-declare-permissions-for-a-target-app-without-altering-its-manifest-and-changing-its-signature.4440379/post-86833435
         *
         * More:
         * https://stackoverflow.com/questions/63563995/failed-to-find-content-provider-in-api-30
         * https://github.com/Lawiusz/xposed_lockscreen_visualizer/blob/master/app/src/main/java/pl/lawiusz/lockscreenvisualizerxposed/PermGrant.java
         */
        if (Build.VERSION.SDK_INT > 29) {
            if (lpparam.packageName.equals("android")) {
                try {
                    XposedBridge.hookAllMethods(XposedHelpers.findClass("com.android.server.pm.permission.PermissionManagerService", lpparam.classLoader), "restorePermissionState", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            String pkgName = (String) XposedHelpers.getObjectField(param.args[0], "packageName");
                            Util.log(MY_PACKAGE_NAME, "Package  " + pkgName + " is requesting permissions");
                            if (isEnabled(pref, pkgName)) {
                                List<String> permissions = (List<String>) XposedHelpers.getObjectField(param.args[0], "requestedPermissions");
                                String query_all_perm = "android.permission.QUERY_ALL_PACKAGES";
                                if (!permissions.contains(query_all_perm)) {
                                    permissions.add(query_all_perm);
                                    Util.log(MY_PACKAGE_NAME, "Added " + query_all_perm + " permission to " + pkgName);
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Util.log(MY_PACKAGE_NAME, "PermissionManagerService -> " + e);
                }
            }
        }

        /**
         * https://developer.android.com/reference/android/app/Application.html#onCreate()
         * wait for the app started to get remote preferences
         */
        XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook()
        {
            @Override
            protected void afterHookedMethod(MethodHookParam param)
            {
                final String packageName = lpparam.packageName;
                Context context = Util.getCurrentApplication();

                if (null == context)
                {
                    Util.log(packageName, "failed to get context");
                    return;
                }

                if (isEnabled(pref, packageName))
                {
                    Util.log(packageName, "is enabled for MinMinGuard");

                    // Api based blocking
                    ApiBlocking.handle(packageName, lpparam, param);
                    appSpecific(packageName, lpparam);

                    // Name based blocking
                    NameBlocking.nameBasedBlocking(packageName, lpparam);

                    // url filtering
                    if (pref.getBoolean(packageName + "_url", false))
                    {
                        UrlFiltering.removeWebViewAds(packageName, lpparam);
                    }
                }
            }
        });
    }
}
