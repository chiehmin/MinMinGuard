package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Amazon extends Blocker {
    
    public static final String BANNER = "com.amazon.device.ads.AdLayout";
    public static final String BANNER_PREFIX = "com.amazon.device.ads";

    public static final String AD_REQUEST = "com.amazon.device.ads.DTBAdRequest";
    
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		boolean result = false;

		result |= ApiBlocking.removeBanner(packageName, BANNER, "setListener", lpparam, removeAd);
        result |= ApiBlocking.removeBannerWithResult(packageName, BANNER, "loadAd", new Boolean(true), lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, AD_REQUEST, "loadAd", "com.amazon.device.ads.DTBAdCallback", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, AD_REQUEST, "internalLoadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, AD_REQUEST, "loadAdRequest", lpparam, removeAd);

        return result;
	}
	@Override
	public String getBannerPrefix() {
		return BANNER_PREFIX;
	}

	@Override
	public String getBanner() {
		return BANNER;
	}
}
