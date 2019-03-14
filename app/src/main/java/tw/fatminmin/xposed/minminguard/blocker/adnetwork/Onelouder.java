package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import java.lang.reflect.Method;

public class Onelouder extends Blocker
{

    public static final String BANNER = "com.onelouder.adlib.AdView";
    public static final String BANNER_PREFIX = "com.onelouder.adlib";

    //TODO Use APIBlocking
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        try
        {
            Class<?> adView = XposedHelpers.findClass("com.onelouder.adlib.AdView", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "setVisibility", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    Util.log(packageName, "Detect onelouder AdView setVisibility in " + packageName);

                    param.args[0] = View.GONE;
                }
            });

            final Method method = XposedHelpers.findMethodBestMatch(adView, "setVisibility", new Class[]{Integer.class});

            XposedBridge.hookAllMethods(adView, "addProxiedAdView", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    Util.log(packageName, "Detect onelouder addProxiedAdView in " + packageName);

                    method.invoke(param.thisObject, View.GONE);
                    param.setResult(new Object());
                }
            });

            XposedBridge.hookAllMethods(adView, "resume", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    Util.log(packageName, "Detect onelouder AdView resume in " + packageName);

                    method.invoke(param.thisObject, View.GONE);
                    param.setResult(new Object());
                }
            });

            Class<?> interad = XposedHelpers.findClass("com.onelouder.adlib.AdInterstitial", lpparam.classLoader);
            XposedBridge.hookAllMethods(interad, "displayInterstitial", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    Util.log(packageName, "Detect onelouder AdInterstitial displayInterstitial in " + packageName);
                    param.setResult(new Object());
                }
            });

            Util.log(packageName, packageName + " uses onelouder");
        }
        catch (ClassNotFoundError e)
        {
            return false;
        }
        return true;
    }

    @Override
    public String getBannerPrefix()
    {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner()
    {
        return BANNER;
    }
}
