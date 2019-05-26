package tw.fatminmin.xposed.minminguard.blocker;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static de.robv.android.xposed.XposedHelpers.findClass;

public final class UrlFiltering
{
    private static boolean adExist = false;

    static public boolean removeWebViewAds(final String packageName, LoadPackageParam lpparam)
    {

        try
        {

            Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);

            XposedBridge.hookAllMethods(adView, "loadUrl", new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                {

                    String url = (String) param.args[0];
                    adExist = urlFiltering(url, "", param, packageName);
                    if (adExist)
                    {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                {

                    String data = (String) param.args[0];
                    adExist = urlFiltering("", data, param, packageName);
                    if (adExist)
                    {
                        param.setResult(new Object());
                    }
                }
            });

            XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param)
                {
                    String url = (String) param.args[0];
                    String data = (String) param.args[1];
                    adExist = urlFiltering(url, data, param, packageName);
                    if (adExist)
                    {
                        param.setResult(new Object());
                    }
                }
            });
        }
        catch (ClassNotFoundError e)
        {
            Util.log(packageName, packageName + "can not clear webview ads");
            return false;
        }
        return adExist;
    }

    static private boolean urlFiltering(String url, String data, MethodHookParam param, String packageName)
    {

        Util.log(packageName, "Url filtering");

        if (url == null)
            url = "";

        try
        {
            url = URLDecoder.decode(url, "UTF-8");
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        Util.log(packageName, packageName + " url:\n" + url);

        for (String adUrl : Main.patterns)
        {
            if (url.contains(adUrl))
            {
                Util.log(packageName, "Detect " + packageName + " load url from " + adUrl);

                ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                param.setResult(new Object());

                return true;
            }
        }

        Util.log(packageName, packageName + " data:\n" + data);

        try
        {
            data = URLDecoder.decode(data, "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        for (String adUrl : Main.patterns)
        {
            if (data.contains(adUrl))
            {
                Util.log(packageName, "Detect " + packageName + " load data from " + adUrl);
                ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                param.setResult(new Object());

                return true;
            }
        }

        return false;
    }
}
