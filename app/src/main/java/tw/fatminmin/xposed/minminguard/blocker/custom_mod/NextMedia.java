package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;

/**
 * Created by fatminmin on 2016/2/7.
 */
public class NextMedia  {

    public static final String AD_UTILS = "com.nextmediatw.data.AdUtils";
    static public void handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean removeAd) {
        ApiBlocking.blockAdFunctionWithResult(packageName, AD_UTILS, "skipAd", Boolean.valueOf(true), lpparam, removeAd);
    }
}
