package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by duxa174 on 2018/06/13.
 * https://tech.yandex.ru/mobile-ads/doc/dg/intro/about-docpage/
 **/

public class Yandex extends Blocker
{
    private static final String BANNER = "com.yandex.mobile.ads.AdView";
    private static final String BANNER_PREFIX = "com.yandex.mobile.ads";

    private static final String INTERSTITIAL = "com.yandex.mobile.ads.InterstitialAd";

    private static final String NATIVE = "com.yandex.mobile.ads.nativeads.NativeAdLoader";
    //public static final String NATIVE_APP_INSTALL = "com.yandex.mobile.ads.nativeads.NativeAppInstallAdView";
    //public static final String NATIVE_CONTENT = "com.yandex.mobile.ads.nativeads.NativeContentAdView";
    //public static final String NATIVE_IMAGE = "com.yandex.mobile.ads.nativeads.NativeImageAdView";

    private static final String VIDEO = "com.yandex.mobile.ads.video.YandexVideoAds";

    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        //TODO Does this need to use removeBanner?
        result = ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTERSTITIAL, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE, "loadAd", lpparam);
//        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_APP_INSTALL, "getNativeAd", lpparam);
//        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_CONTENT, "getNativeAd", lpparam);
//        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_IMAGE, LOAD_AD, lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, VIDEO, "loadBlocksInfo", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VIDEO, "loadVideoAds", lpparam);

        return result;
    }

    @Override
    public String getBannerPrefix()
    {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner()
    {
        return BANNER;
    }
}