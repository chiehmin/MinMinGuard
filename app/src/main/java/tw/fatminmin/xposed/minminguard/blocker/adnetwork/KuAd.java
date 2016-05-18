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

public class KuAd extends Blocker {
    
    public static final String BANNER = "com.waystorm.ads.WSAdBanner";
    public static final String BANNER_PREFIX = "com.waystorm.ads";

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
		result |= ApiBlocking.removeBanner(packageName, BANNER, "setWSAdListener", lpparam, removeAd);
		result |= ApiBlocking.removeBanner(packageName, BANNER, "setApplicationId", lpparam, removeAd);
		result |= ApiBlocking.removeBanner(packageName, BANNER, "mediationLoadAd", lpparam, removeAd);

		return result;
	}
}
