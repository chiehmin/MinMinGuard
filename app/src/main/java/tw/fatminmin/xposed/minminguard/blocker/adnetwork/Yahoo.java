package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Yahoo extends Blocker {

    public final static String bannerPrefix = "com.yahoo.mobile.client.share.android.ads.views";
    public final static String banner = "com.yahoo.mobile.client.android.weather.ui.view.AdView";
    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }

    @Override
    public String getBanner() {
        return banner;
    }
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        return false;
    }

}
