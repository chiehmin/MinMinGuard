package tw.fatminmin.xposed.minminguard.blocker;

import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import de.robv.android.xposed.XposedBridge;
import tw.fatminmin.xposed.minminguard.BuildConfig;
import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.Main;

import java.io.File;

public final class Util
{
    // change it to false for release build
    public static boolean DEBUG = BuildConfig.DEBUG;

    public static final String TAG = "MinMinGuard";
    public static final String PACKAGE = "tw.fatminmin.xposed.minminguard";

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

    static public void notifyRemoveAdView(final Context context, final String pkgName, final int blockNum)
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

        Main.notifyWorker.submit(task);
    }
}
