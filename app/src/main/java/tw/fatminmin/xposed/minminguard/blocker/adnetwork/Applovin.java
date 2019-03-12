package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Applovin extends Blocker
{
    public static final String MAX_ADVIEW = "com.applovin.mediation.ads.MaxAdView";
    public static final String MAX_INTER = "com.applovin.mediation.ads.MaxInterstitialAd";
    public static final String MAX_REWARDED = "com.applovin.mediation.ads.MaxRewardedAd";
    public static final String SDK_ENABLED = "com.applovin.sdk.AppLovinSdk";

    public static final String NATIVE_ADS = "com.applovin.impl.sdk.NativeAdServiceImpl";
    public static final String APPLOVIN_ADS = "com.applovin.impl.sdk.AppLovinAdServiceImpl";
    public static final String APPLOVIN_ADS_NESTED = APPLOVIN_ADS + "$a";

    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, MAX_ADVIEW, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, MAX_INTER, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, MAX_REWARDED, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, SDK_ENABLED, "isEnabled", false, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "loadNativeAds", Integer.class, String.class, "com.applovin.nativeAds.AppLovinNativeAdLoadListener", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, APPLOVIN_ADS_NESTED, "adReceived", "com.applovin.sdk.AppLovinAd", lpparam, removeAd);
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