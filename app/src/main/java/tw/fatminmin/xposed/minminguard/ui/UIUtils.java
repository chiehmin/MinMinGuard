package tw.fatminmin.xposed.minminguard.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

/**
 * Created by fatminmin on 2015/10/25.
 */
public final class UIUtils {

    private UIUtils() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    static public void restartApp(Context context, String packageName)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(packageName);

        Intent it = context.getPackageManager().getLaunchIntentForPackage(packageName);
        it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }
}
