package tw.fatminmin.xposed.minminguard.blocker;

import android.content.Context;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;

/**
 * Created by fatminmin on 2015/10/27.
 */
public final class ApiBlocking
{

    public static void handle(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final XC_MethodHook.MethodHookParam param)
    {
        Context context = (Context) (param.thisObject);

        for (Blocker blocker : Main.blockers)
        {
            try
            {
                String name = blocker.getClass().getSimpleName();
                boolean result = blocker.handleLoadPackage(packageName, lpparam);

                if (result)
                {
                    Util.notifyAdNetwork(context, packageName, name);
                }
            }
            catch (Exception e)
            {
                Util.log(packageName, e.toString());
            }
        }
    }

    /*
        Used for blocking banner function and removing banner
     */
    public static boolean removeBanner(final String packageName, final String bannerClass, final String bannerFunc, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        return removeBannerWithResult(packageName, bannerClass, bannerFunc, new Object(), lpparam);
    }

    public static boolean removeBannerWithResult(final String packageName, final String bannerClass, final String bannerFunc, final Object result, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            Util.hookAllMethods(bannerClass, lpparam.classLoader, bannerFunc, new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("removeBannerWithResult: Detect %s %s in %s", bannerClass, bannerFunc, packageName);

                    Util.log(packageName, debugMsg);

                    ViewBlocking.removeAdView(packageName, (View) param.thisObject);

                    param.setResult(result);
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("removeBannerWithResult: Method %s not found in %s.", bannerFunc, bannerClass));
            }

            return false;
        }
        return true;
    }

    /*
        Used for blocking ad functions
     */
    public static boolean blockAdFunction(final String packageName, final String adClass, final String adFunc, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            Util.hookAllMethods(adClass, lpparam.classLoader, adFunc, new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("blockAdFunction: Detect %s %s in %s", adClass, adFunc, packageName);

                    Util.log(packageName, debugMsg);
                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(new Object());
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunction: Method %s not found in %s.", adFunc, adFunc));
            }

            return false;
        }

        return true;
    }

    public static boolean blockAdFunctionExact(final String packageName, final String adClass, final String adFunc, final Object parameter, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            XposedHelpers.findAndHookMethod(adClass, lpparam.classLoader, adFunc, parameter, new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("Detect %s %s(%s) in %s", adClass, adFunc, parameter.toString(), packageName);

                    Util.log(packageName, debugMsg);

                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(new Object());
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunction: Method %s(%s) not found in %s.", adFunc, parameter.toString(), adClass));
            }

            return false;
        }

        return true;
    }

    public static boolean blockAdFunctionExact(final String packageName, final String adClass, final String adFunc, final Object parameter1, final Object parameter2, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            XposedHelpers.findAndHookMethod(adClass, lpparam.classLoader, adFunc, parameter1, parameter2, new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("Detect %s %s(%s, %s) in %s", adClass, adFunc, parameter1.toString(), parameter2.toString(), packageName);

                    Util.log(packageName, debugMsg);

                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(new Object());
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunction: Method %s(%s, %s) not found in %s.", adFunc, parameter1.toString(), parameter2.toString(), adClass));
            }

            return false;
        }

        return true;
    }

    public static boolean blockAdFunctionExact(final String packageName, final String adClass, final String adFunc, final Object parameter1, final Object parameter2, final Object parameter3, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            XposedHelpers.findAndHookMethod(adClass, lpparam.classLoader, adFunc, parameter1, parameter2, parameter3, new XC_MethodHook()
            {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("Detect %s %s(%s, %s, %s) in %s", adClass, adFunc, parameter1.toString(), parameter2.toString(), parameter3.toString(), packageName);

                    Util.log(packageName, debugMsg);

                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(new Object());
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunction: Method %s(%s, %s, %s) not found in %s.", adFunc, parameter1.toString(), parameter2.toString(), parameter3.toString(), adClass));
            }

            return false;
        }

        return true;
    }

    public static boolean blockAdFunctionWithResult(final String packageName, final String adClass, final String adFunc, final Object result, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            XposedHelpers.findAndHookMethod(adClass, lpparam.classLoader, adFunc, new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("blockAdFunctionWithResult: Detect %s %s in %s", adClass, adFunc, packageName);

                    Util.log(packageName, debugMsg);

                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(result);
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunctionWithResult: Method %s not found in %s.", adFunc, adClass));
            }

            return false;
        }

        return true;
    }


    public static boolean blockAdFunctionWithResultExact(final String packageName, final String adClass, final String adFunc, final Object parameter, final Object result, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            XposedHelpers.findAndHookMethod(adClass, lpparam.classLoader, adFunc, parameter, new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("Detect %s %s(%s) in %s", adClass, adFunc, parameter.toString(), packageName);

                    Util.log(packageName, debugMsg);

                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(result);
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunctionWithResult: Method %s(%s) not found in %s.", adFunc, parameter.toString(), adClass));
            }

            return false;
        }

        return true;
    }


    public static boolean blockAdFunctionWithResultExact(final String packageName, final String adClass, final String adFunc, final Object parameter1, final Object parameter2, final Object result, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        try
        {
            XposedHelpers.findAndHookMethod(adClass, lpparam.classLoader, adFunc, parameter1, parameter2, new XC_MethodHook()
            {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable
                {
                    String debugMsg = String.format("Detect %s %s(%s, %s) in %s", adClass, adFunc, parameter1.toString(), parameter2.toString(), packageName);

                    Util.log(packageName, debugMsg);

                    Util.notifyRemoveAdView(null, packageName, 1);

                    param.setResult(result);
                }
            });
        }
        catch (ClassNotFoundError | NoSuchMethodError e)
        {
            if (e instanceof NoSuchMethodError)
            {
                Util.log(packageName, String.format("blockAdFunctionWithResultExact: Method %s(%s, %s) not found in %s.", adFunc, parameter1.toString(), parameter2.toString(), adClass));
            }

            return false;
        }

        return true;
    }
}