package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MoPub extends Blocker {
    
    public static final String BANNER = "com.mopub.mobileads.MoPubView";
    public static final String BANNER_PREFIX = "com.mopub.mobileads";
    public static final String INTER_ADS = "com.mopub.mobileads.MoPubInterstitial";
    public static final String NATIVE_AD = "com.mopub.nativeads.MoPubAdAdapter";

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "load", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "loadAds", lpparam, removeAd);

        return result;
	}
}
