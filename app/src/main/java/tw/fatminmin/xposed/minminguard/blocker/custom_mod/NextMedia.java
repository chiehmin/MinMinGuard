package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by fatminmin on 2016/2/7.
 */
public class NextMedia  {

    final public static String adapter = "com.nextmediatw.apple.tw.adapter.NewsListAdapter";
    final public static String adapterFunc1 = "addAD";
    final public static String adapterFunc2 = "reloadAd";
    final public static String adapterFunc3 = "reloadAdInRange";

    static public void handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean removeAd) {
        ApiBlocking.blockAdFunctionWithNull(packageName, adapter, adapterFunc1, lpparam, removeAd);
        ApiBlocking.blockAdFunctionWithNull(packageName, adapter, adapterFunc2, lpparam, removeAd);
        ApiBlocking.blockAdFunctionWithNull(packageName, adapter, adapterFunc3, lpparam, removeAd);
    }
}
