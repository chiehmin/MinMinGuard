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

public class Flurry extends Blocker {
    
    public final static String banner = "com.flurry.android.FlurryAds";
    public final static String bannerPrefix = "com.flurry.android";

    public final static String nativeAd = "com.flurry.android.ads.FlurryAdNative";

	@Override
	public String getBannerPrefix() {
		return bannerPrefix;
	}

	@Override
	public String getBanner() {
		return banner;
	}
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {

            ApiBlocking.removeBanner(packageName, banner, "displayAd", lpparam, removeAd);
            ApiBlocking.blockAdFunction(packageName, banner, "fetchAd", lpparam, removeAd);
            ApiBlocking.blockAdFunction(packageName, nativeAd, "fetchAd", lpparam, removeAd);

			Util.log(packageName, packageName + " uses FlurryAds");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
