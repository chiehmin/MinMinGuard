package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Yahoo extends Blocker {

    public static final String BANNER_PREFIX = "com.yahoo.mobile.client.share.android.ads.views";
    public static final String BANNER = "com.yahoo.mobile.client.android.weather.ui.view.AdView";
    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        return false;
    }

}
