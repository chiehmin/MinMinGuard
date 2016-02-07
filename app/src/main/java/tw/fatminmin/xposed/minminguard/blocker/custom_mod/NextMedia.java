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

    final public static String adapter = "com.nextmediatw.apple.tw.adapter.NewsListAdapter";
    final public static String adapterFunc1 = "addAD";
    final public static String adapterFunc2 = "reloadAd";
    final public static String adapterFunc3 = "reloadAdInRange";

    final public static String news = "com.nextmediatw.unit.News";
    final public static String newsFunc = "isAd";

    final public static String streamHelper = "com.nextmediatw.data.AdUtils.StreamHelper";
    final public static String streamHelperFunc1 = "setAdListener";
    final public static String streamHelperFunc2 = "getAd";
    final public static String streamHelperFunc3 = "preallocte";

    static public void handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean removeAd) {
        ApiBlocking.blockAdFunctionWithResult(packageName, adapter, adapterFunc1, null, lpparam, removeAd);
        ApiBlocking.blockAdFunctionWithResult(packageName, adapter, adapterFunc2, null, lpparam, removeAd);

        ApiBlocking.blockAdFunction(packageName, streamHelper, streamHelperFunc1, lpparam, removeAd);
        ApiBlocking.blockAdFunctionWithResult(packageName, streamHelper, streamHelperFunc2, null, lpparam, removeAd);
        ApiBlocking.blockAdFunction(packageName, streamHelper, streamHelperFunc3, lpparam, removeAd);

    }
}
