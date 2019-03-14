package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.Util;

public final class _2chMate
{

    private _2chMate() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        if (!packageName.equals("jp.co.airfront.android.a2chMate"))
        {
            return false;
        }

        try
        {
            final Class<?> viewGroupClass = XposedHelpers.findClass("android.view.ViewGroup", lpparam.classLoader);
            XposedBridge.hookAllMethods(viewGroupClass, "addView", new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    final View view = (View) param.args[0];
                    if (view.getClass().getName().equals("jp.syoboi.a2chMate.view.MyAdView"))
                    {
                        Util.log(packageName, "Detect 2chmate MyAdView in " + packageName);

                        param.setResult(null);
                    }
                }
            });
            Util.log(packageName, packageName + " is 2chmate");
        }
        catch (ClassNotFoundError e)
        {
            return false;
        }

        return true;
    }
}
