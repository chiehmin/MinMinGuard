package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Ironsource extends Blocker
{
    public static final String SuperSonicAdsAdapter = "com.ironsource.adapters.supersonicads.SupersonicAdsAdapter";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, SuperSonicAdsAdapter, "showRewardedVideo", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, SuperSonicAdsAdapter, "showInterstitial", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, SuperSonicAdsAdapter, "showOfferwall", lpparam);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, SuperSonicAdsAdapter, "isRewardedVideoAvailable", false, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, SuperSonicAdsAdapter, "isInterstitialReady", false, lpparam);

        return result;
    }

    @Override
    public String getBanner()
    {
        return null;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }
}
