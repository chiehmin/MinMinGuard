package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class ImpAct extends Blocker {
    public static final String BANNER = "jp.co.dac.sdads.android.AdView";
    public static final String BANNER_PREFIX = "jp.co.dac.sdads.android";
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
