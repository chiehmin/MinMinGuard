package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

public class UnityAds extends Blocker {

    public static final String ADS = "com.unity3d.ads.android.UnityAds";

    @Override
    public String getBannerPrefix() {
        return null;
    }

    @Override
    public String getBanner() {
        return null;
    }
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.blockAdFunction(packageName, ADS, "show", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, ADS, "canShow", Boolean.valueOf(false), lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, ADS, "canShowAds", Boolean.valueOf(false), lpparam, removeAd);

        return result;
    }
}

