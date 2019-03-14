package tw.fatminmin.xposed.minminguard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import com.crossbowffs.remotepreferences.RemotePreferences;
import de.robv.android.xposed.*;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.*;
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.*;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.NextMedia;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.OneWeather;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod.Viafree;
import tw.fatminmin.xposed.minminguard.blocker.custom_mod._2chMate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Main implements IXposedHookZygoteInit, IXposedHookLoadPackage
{
    public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
    public static String MODULE_PATH = null;
    public static Set<String> patterns;
    public static Resources resources;

    public static Blocker[] blockers = {
            /* Popular adnetwork */
            new Ad2iction(), new Adbert(), new Adcolony(), new Adfurikun(), new AdMarvel(), new GoogleAdmob(), new GoogleGms(), new Adtech(), new Amazon(), new Amobee(),
            new Aotter(), new AppBrain(), new Applovin(), new Appnext(), new Avocarrot(), new Bonzai(), new Chartboost(), new Clickforce(), new Domob(), new Facebook(),
            new Freewheel(), new Flurry(), new GoogleGmsDoubleClick(), new Hodo(), new Inmobi(), new Intowow(), new Ironsource(), new KuAd(), new mAdserve(), new Madvertise(),
            new MasAd(), new MdotM(), new Millennial(), new Mobclix(), new MobFox(), new MoPub(), new Nend(), new Og(), new Onelouder(), new OpenX(), new SmartAdserver(),
            new Startapp(), new Tapfortap(), new TWMads(), new UnityAds(), new Vpadn(), new Vpon(), new Vungle(), new Waystorm(), new Yandex(),
            /* Custom Mod*/
    };

    public static ExecutorService notifyWorker;

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

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable
    {
        MODULE_PATH = startupParam.modulePath;

        resources = XModuleResources.createInstance(MODULE_PATH, null);
        byte[] array = XposedHelpers.assetAsByteArray(resources, "host/output_file");
        String decoded = new String(array);
        String[] sUrls = decoded.split("\n");
        patterns = new HashSet<>();

        for (String url : sUrls)
        {
            patterns.add(url);
        }

        array = XposedHelpers.assetAsByteArray(resources, "host/mmg_pattern");
        decoded = new String(array);
        sUrls = decoded.split("\n");

        for (String url : sUrls)
        {
            patterns.add(url);
        }

        notifyWorker = Executors.newSingleThreadExecutor();
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable
    {

        if (lpparam.packageName.equals(MY_PACKAGE_NAME))
            XposedHelpers.findAndHookMethod("tw.fatminmin.xposed.minminguard.blocker.Util", lpparam.classLoader, "xposedEnabled", XC_MethodReplacement.returnConstant(true));

        /**
         * https://developer.android.com/reference/android/app/Application.html#onCreate()
         * wait for the app started to get remote preferences
         */
        XposedHelpers.findAndHookMethod("android.app.Application", lpparam.classLoader, "onCreate", new XC_MethodHook()
        {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable
            {
                final String packageName = lpparam.packageName;
                Context context = Util.getCurrentApplication();

                if (null == context)
                {
                    Util.log(packageName, "failed to get context");
                    return;
                }

                SharedPreferences pref = new RemotePreferences(context, "tw.fatminmin.xposed.minminguard.modesettings", Common.MOD_PREFS);

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
