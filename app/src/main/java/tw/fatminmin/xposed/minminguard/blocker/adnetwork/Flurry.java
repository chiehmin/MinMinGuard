package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Flurry extends Blocker {
    
    public static final String BANNER = "com.flurry.android.FlurryAds";
    public static final String BANNER_PREFIX = "com.flurry.android";

    public static final String NATIVE_AD = "com.flurry.android.ads.FlurryAdNative";

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
        result |= ApiBlocking.removeBanner(packageName, BANNER, "displayAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, BANNER, "fetchAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, "fetchAd", lpparam, removeAd);

		return result;
	}
}
