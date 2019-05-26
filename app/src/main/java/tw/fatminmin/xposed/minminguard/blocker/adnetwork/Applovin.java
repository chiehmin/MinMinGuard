package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Applovin extends Blocker
{
    private static final String MAX_ADVIEW = "com.applovin.mediation.ads.MaxAdView";
    private static final String MAX_INTER = "com.applovin.mediation.ads.MaxInterstitialAd";
    private static final String MAX_REWARDED = "com.applovin.mediation.ads.MaxRewardedAd";

    private static final String SDK_ENABLED = "com.applovin.sdk.AppLovinSdk";

    private static final String NATIVE_ADS = "com.applovin.impl.sdk.NativeAdServiceImpl";

    private static final String APPLOVIN_ADS = "com.applovin.impl.sdk.AppLovinAdServiceImpl";
    private static final String APPLOVIN_ADS_NESTED = APPLOVIN_ADS + "$a";

    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunction(packageName, MAX_ADVIEW, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, MAX_INTER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, MAX_REWARDED, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, SDK_ENABLED, "isEnabled", false, lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "loadNativeAds", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, APPLOVIN_ADS_NESTED, "adReceived", lpparam);
        return result;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }

    @Override
    public String getBanner()
    {
        return null;
    }
}