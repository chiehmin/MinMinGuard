package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by fatminmin on 2016/2/7.
 */
public class NextMedia  {

    final public static String adUtils = "com.nextmediatw.data.AdUtils";
    static public void handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean removeAd) {
        ApiBlocking.blockAdFunctionWithResult(packageName, adUtils, "skipAd", Boolean.valueOf(true), lpparam, removeAd);
    }
}
