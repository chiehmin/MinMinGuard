package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

/**
 * Created by fatminmin on 5/18/16.
 */
public class Aotter extends Blocker {

    public static final String BANNER_PREFIX = "com.aotter.net.trek.ads";
    public static final String NATIVE_ADS = "com.aotter.net.trek.ads.TKAdN";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean removeAd) {
        try {
            ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "setAdListener", lpparam, removeAd);
            ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "registerViewForInteraction", lpparam, removeAd);

            Util.log(packageName, packageName + " uses Aotter");
        }
        catch(XposedHelpers.ClassNotFoundError e) {
            return false;
        }
        return true;
    }

    @Override
    public String getBanner() {
        return null;
    }

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }
}
