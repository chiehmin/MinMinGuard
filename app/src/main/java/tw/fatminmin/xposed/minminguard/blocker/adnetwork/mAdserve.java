package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

public class mAdserve extends Blocker
{

    public static final String BANNER = "com.adsdk.sdk.banner.InAppWebView";
    public static final String BANNER_PREFIX = "com.adsdk.sdk.banner";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {

        try
        {
            //TODO Add blockConstructor to ApiBlocking, so we dont have to do this hack..
            Class<?> adView = XposedHelpers.findClass("com.adsdk.sdk.banner.InAppWebView", lpparam.classLoader);
            XposedBridge.hookAllConstructors(adView, new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    Util.log(packageName, "Detect mAdserve InAppWebView constructor in " + packageName);

                    param.setResult(new Object());
                }
            });

            Util.log(packageName, packageName + " uses mAdserve");
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
