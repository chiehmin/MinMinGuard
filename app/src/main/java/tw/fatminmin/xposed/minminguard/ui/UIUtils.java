package tw.fatminmin.xposed.minminguard.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import tw.fatminmin.xposed.minminguard.R;

/**
 * Created by fatminmin on 2015/10/25.
 */
public final class UIUtils
{

    private UIUtils() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    static public void restartApp(Context context, String packageName)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        am.killBackgroundProcesses(packageName);

        Intent it = context.getPackageManager().getLaunchIntentForPackage(packageName);
        Activity a = (Activity) context;
        if (it != null)
        {
            if (a.getCurrentFocus() != null)
            {
                ((InputMethodManager) a.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(a.getCurrentFocus().getWindowToken(), 0);
            }

            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(it);
        }
        else
        {
            Toast.makeText(context, context.getString(R.string.msg_app_launch_fail), Toast.LENGTH_SHORT).show();
        }
    }
}
