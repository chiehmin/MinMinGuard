package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Chartboost extends Blocker {

    public static final String BANNER = "com.chartboost.sdk.Chartboost";
    public static final String BANNER_PREFIX = "com.chartboost.sdk";
    
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		boolean result = false;
		result |= ApiBlocking.blockAdFunction(packageName, BANNER, "showInterstitial", String.class, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "showRewardedVideo", String.class, lpparam, removeAd);
		result |= ApiBlocking.blockAdFunctionWithResult(packageName, BANNER, "hasInterstitial", String.class, false, lpparam, removeAd);
		result |= ApiBlocking.blockAdFunctionWithResult(packageName, BANNER, "hasRewardedVideo", String.class, false, lpparam, removeAd);
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
