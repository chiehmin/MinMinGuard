package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import android.view.View;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Startapp extends Blocker {
    
    public static final String BANNER = "com.startapp.android.publish.HtmlAd";
    public static final String BANNER_FUNC = "show";
    public static final String BANNER_PREFIX = "com.startapp.android.publish";

    public static final String NATIVE_AD = "com.startapp.android.publish.nativead.StartAppNativeAd";
    public static final String NATIVE_AD_FUNC = "getNativeAds";

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
        result |= ApiBlocking.removeBanner(packageName, BANNER, BANNER_FUNC, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, NATIVE_AD_FUNC, lpparam, removeAd);
        return result;
    }
}
