package tw.fatminmin.xposed.minminguard.blocker;

import android.content.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;

/**
 * Created by fatminmin on 2015/10/27.
 */
public class ApiBlocking {
    public static void handle(Context context, String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean test) {
        for (Blocker blocker : Main.blockers) {
            String name = blocker.getClass().getSimpleName();
            Boolean result = blocker.handleLoadPackage(packageName, lpparam, test);
            if(result) {
                Util.notifyAdNetwork(context, packageName, name);
            }
        }
    }
}
