package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by fatminmin on 2016/2/7.
 */
public class MobFox extends Blocker {

    public static final String BANNER = "com.adsdk.sdk.waterfall.Banner";
    public static final String BANNER_LOAD_FUNC = "loadAd";
    public static final String BANNER_PREFIX = "com.adsdk.sdk.waterfall";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        return ApiBlocking.removeBanner(packageName, BANNER, BANNER_LOAD_FUNC, lpparam, removeAd);
    }

    @Override
    public String getBanner() {
        return BANNER;
    }

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }
}
