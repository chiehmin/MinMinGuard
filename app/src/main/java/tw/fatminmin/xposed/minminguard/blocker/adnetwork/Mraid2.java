package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Mraid2 extends Blocker {

    public static final String BANNER = "org.nexage.sourcekit.mraid.MRAIDView";
    public static final String BANNER_PREFIX = "org.nexage.sourcekit.mraid";

    public static final String AD_INTERSTITIAL = "org.nexage.sourcekit.mraid.MRAIDInterstitial";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam) {
        boolean result = false;
        result |= ApiBlocking.removeBanner(packageName,BANNER,"show",lpparam);
        result |= ApiBlocking.blockAdFunction(packageName,AD_INTERSTITIAL,"show",lpparam);

        return result;
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
