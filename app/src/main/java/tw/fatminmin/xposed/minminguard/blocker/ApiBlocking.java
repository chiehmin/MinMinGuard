package tw.fatminmin.xposed.minminguard.blocker;

import android.content.Context;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.Main;

/**
 * Created by fatminmin on 2015/10/27.
 */
public class ApiBlocking {
    public static void handle(Context context, String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean test) {
        for (Class network : Main.adNetworks) {
            try {
                Boolean result =
                        (Boolean) XposedHelpers.callStaticMethod(network, "handleLoadPackage", packageName, lpparam, test);
                if(result) {
                    Util.notifyAdNetwork(context, packageName, network.getSimpleName());
                }

            } catch (NoSuchMethodError e) {
                e.printStackTrace();
            }
        }
    }
}
