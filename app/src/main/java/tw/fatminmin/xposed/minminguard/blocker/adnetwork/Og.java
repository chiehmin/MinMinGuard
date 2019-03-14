package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.blocker.ViewBlocking;

public class Og extends Blocker
{

    public static final String BANNER = "com.og.wa.AdWebView";
    public static final String BANNER_PREFIX = "com.og.wa";

    //FIXME Use APIBlocking Please....... Use fields for classNames
    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        try
        {
            final Class<?> adView = XposedHelpers.findClass("com.og.wa.AdWebView", lpparam.classLoader);
            final Class<?> webView = XposedHelpers.findClass("android.webkit.WebView", lpparam.classLoader);
            XposedBridge.hookAllMethods(webView, "loadUrl", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    if (adView.isInstance(param.thisObject))
                    {
                        Util.log(packageName, "Detect og AdWebView loadUrl in " + packageName);
                        param.setResult(new Object());
                        ViewBlocking.removeAdView(packageName, (View) param.thisObject);
                    }
                }
            });
            Util.log(packageName, packageName + " uses Og AdWebView");
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
