package tw.fatminmin.xposed.minminguard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.FileObserver;

import java.io.File;

import androidx.core.content.ContextCompat;

/**
 * Created by Xspeed on 2019/7/7.
 */
public class PrefFileManager
{
    private final Context mContext;
    private final FileObserver mFileObserver;
    private boolean mSelfAttrChange;

    private PrefFileManager(Context context)
    {
        mContext = Build.VERSION.SDK_INT >= 24 && !ContextCompat.isDeviceProtectedStorage(context)
                ? ContextCompat.createDeviceProtectedStorageContext(context) : context;

        mFileObserver = new FileObserver(mContext.getFilesDir().getParentFile() + "/shared_prefs", FileObserver.ATTRIB)
        {
            @Override
            public void onEvent(int event, String path)
            {
                if ((event & FileObserver.ATTRIB) != 0)
                    onFileAttributesChanged(path);
            }
        };
        mFileObserver.startWatching();
    }

    public static synchronized PrefFileManager getInstance(Context context)
    {
        if (context == null) throw new IllegalArgumentException("Context cannot be null");
        if (context.getApplicationContext() != null) context = context.getApplicationContext();
        return new PrefFileManager(context);
    }

    public void fixFolderPermissionsAsync()
    {
        AsyncTask.execute(new Runnable() {
            @SuppressLint("SetWorldReadable")
            @SuppressWarnings("ResultOfMethodCallIgnored")
            @Override
            public void run() {
                File pkgFolder = mContext.getFilesDir().getParentFile();
                if (pkgFolder.exists())
                {
                    pkgFolder.setExecutable(true, false);
                    pkgFolder.setReadable(true, false);
                }
                File cacheFolder = mContext.getCacheDir();
                if (cacheFolder.exists())
                {
                    cacheFolder.setExecutable(true, false);
                    cacheFolder.setReadable(true, false);
                }
                File filesFolder = mContext.getFilesDir();
                if (filesFolder.exists())
                {
                    filesFolder.setExecutable(true, false);
                    filesFolder.setReadable(true, false);
                    for (File f : filesFolder.listFiles())
                    {
                        f.setExecutable(true, false);
                        f.setReadable(true, false);
                    }
                }
            }
        });
    }

    public void onPause()
    {
        mFileObserver.stopWatching();
        fixPermissions(true);
    }

    public void onResume()
    {
        fixPermissions(true);
        mFileObserver.startWatching();
    }

    @SuppressLint("SetWorldReadable")
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void fixPermissions(boolean force)
    {
        File sharedPrefsFolder = new File(mContext.getFilesDir().getParentFile(), "shared_prefs");
        if (sharedPrefsFolder.exists())
        {
            sharedPrefsFolder.setExecutable(true, false);
            sharedPrefsFolder.setReadable(true, false);
            File f = new File(sharedPrefsFolder, Common.MOD_PREFS + ".xml");
            if (f.exists())
            {
                mSelfAttrChange = !force;
                f.setReadable(true, false);
            }
        }
    }

    private void onFileAttributesChanged(String path)
    {
        if (path != null && path.endsWith(Common.MOD_PREFS + ".xml"))
        {
            if (mSelfAttrChange)
            {
                mSelfAttrChange = false;
                return;
            }
            fixPermissions(false);
        }
    }
}
