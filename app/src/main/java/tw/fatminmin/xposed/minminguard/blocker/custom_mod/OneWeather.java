package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

//FIXME Use newer XposedHelper class
public final class OneWeather
{
    private static final String LAYOUT = "com.handmark.expressweather";

    private OneWeather() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        try
        {
            Class<?> adView = XposedHelpers.findClass("com.handmark.expressweather.billing.BillingUtils", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "isPurchased", new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {

                    param.setResult(Boolean.valueOf(true));
                }
            });
        }
        catch (ClassNotFoundError e)
        {
            return false;
        }
        return true;
    }

    public static void handleInitPackageResources(InitPackageResourcesParam resparam)
    {
        if (!resparam.packageName.equals(LAYOUT))
        {
            return;
        }

        resparam.res.hookLayout(LAYOUT, "layout", "main_phone", new XC_LayoutInflated()
        {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable
            {

                View ad = (View) liparam.view.findViewById(liparam.res.getIdentifier("adview", "id", LAYOUT));

                ad.setVisibility(View.GONE);

                ad = (View) liparam.view.findViewById(liparam.res.getIdentifier("share_ad_cover", "id", LAYOUT));

                ad.setVisibility(View.GONE);
            }
        });
    }
}
