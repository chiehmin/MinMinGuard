package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Avocarrot extends Blocker {

    public static final String prefix = "com.avocarrot.androidsdk";

    public static final String adController = "com.avocarrot.androidsdk.BaseController";
    public static final String adControllerFunc = "loadAd";

    @Override
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.blockAdFunction(packageName, adController, adControllerFunc, lpparam, removeAd);
        return result;
    }

    @Override
    public String getBanner() { return null; }

    @Override
    public String getBannerPrefix() { return prefix; }
}   