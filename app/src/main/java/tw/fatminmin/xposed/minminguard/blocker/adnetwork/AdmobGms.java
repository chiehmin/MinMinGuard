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

public class AdmobGms extends Blocker {

    public static final String BANNER = "com.google.android.gms.ads.AdView";
    public static final String BANNER_PREFIX = "com.google.android.gms.ads";

	public static final String SEARCH_BANNER = "com.google.android.gms.ads.search.SearchAdView";
	public static final String INTER_ADS = "com.google.android.gms.ads.InterstitialAd";

    // native ads
    public static final String AD_LOADER = "com.google.android.gms.ads.AdLoader";

	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);

        result |= ApiBlocking.removeBanner(packageName, SEARCH_BANNER, "loadAd", lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "loadAd", "com.google.android.gms.ads.AdRequest", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTER_ADS, "show", lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAd", "com.google.android.gms.ads.AdRequest", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, AD_LOADER, "loadAds", "com.google.android.gms.ads.AdRequest", int.class, lpparam, removeAd);

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
