package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import static de.robv.android.xposed.XposedHelpers.findClass;

/**
 * Created by fatminmin on 2016/2/7.
 */
public class MobFox extends Blocker {

    public final static String banner = "com.adsdk.sdk.waterfall.Banner";
    public final static String bannerLoadFunc = "loadAd";
    public final static String bannerPrefix = "com.adsdk.sdk.waterfall";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        return ApiBlocking.removeBanner(packageName, banner, bannerLoadFunc, lpparam, removeAd);
    }

    @Override
    public String getBanner() {
        return banner;
    }

    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }
}
