package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class ImpAct extends Blocker {
    public final static String banner = "jp.co.dac.sdads.android.AdView";
    public final static String bannerPrefix = "jp.co.dac.sdads.android";
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
