package tw.fatminmin.xposed.minminguard.blocker;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;

import java.io.File;
import java.util.concurrent.ExecutorService;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import tw.fatminmin.xposed.minminguard.BuildConfig;
import tw.fatminmin.xposed.minminguard.Common;

public final class Util
{
    // change it to false for release build
    public static boolean DEBUG = BuildConfig.DEBUG;

    public static ExecutorService notifyWorker;

    public static final String TAG = "MinMinGuard";
    private static final String PACKAGE = "tw.fatminmin.xposed.minminguard";

    private Util() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static Boolean xposedEnabled()
    {
        return false;
    }

    public static String getAppVersion(Context context)
    {
        String version = null;
        PackageManager pm = context.getPackageManager();

        try
        {
            version = pm.getPackageInfo(PACKAGE, 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
        }

        return version;
    }

    public static void log(String packageName, String msg)
    {
        if (DEBUG)
        {
            //Log.d(TAG, packageName + ": " + msg);
            XposedBridge.log(packageName + ": " + msg);
        }
    }

    public static Application getCurrentApplication()
    {
        try
        {
            return AndroidAppHelper.currentApplication();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveLog(final File dest, final Context context, final Handler handler)
    {
        handler.post(new Runnable()
        {

            @Override
            public void run()
            {
                try
                {

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void notifyAdNetwork(final Context context, final String pkgName, final String adNetwork)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ContentResolver resolver = context.getContentResolver();
                    Uri uri = Uri.parse("content://tw.fatminmin.xposed.minminguard/");
                    ContentValues values = new ContentValues();

                    values.put(Common.KEY_PKG_NAME, pkgName);
                    values.put(Common.KEY_NETWORK, adNetwork);
                    resolver.update(uri, values, null, null);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void notifyRemoveAdView(final Context context, final String pkgName, final int blockNum)
    {
        final Context mContext;

        if (context == null)
            mContext = getCurrentApplication();
        else
            mContext = context;

        Runnable task = new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ContentResolver resolver = mContext.getContentResolver();
                    Uri uri = Uri.parse("content://tw.fatminmin.xposed.minminguard/");
                    ContentValues values = new ContentValues();

                    values.put(Common.KEY_PKG_NAME, pkgName);
                    values.put(Common.KEY_BLOCK_NUM, blockNum);
                    resolver.update(uri, values, null, null);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };

        notifyWorker.submit(task);
    }

    public static void hookAllMethods(String className, ClassLoader classLoader, String method, XC_MethodHook callBack)
    {
        Class<?> clazz = XposedHelpers.findClass(className, classLoader);

        XposedBridge.hookAllMethods(clazz, method, callBack);
    }
}
