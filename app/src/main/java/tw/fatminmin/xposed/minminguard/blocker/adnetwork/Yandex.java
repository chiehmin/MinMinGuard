package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by duxa174 on 2018/06/13.
 * https://tech.yandex.ru/mobile-ads/doc/dg/intro/about-docpage/
**/

public class Yandex extends Blocker {

    public static final String BANNER_PREFIX = "com.yandex.mobile.ads";

    public static final String BANNER = "com.yandex.mobile.ads.AdView";
    public static final String INTERSTITIAL = "com.yandex.mobile.ads.InterstitialAd";
    public static final String NATIVE = "com.yandex.mobile.ads.nativeads.NativeAdLoader";
    public static final String NATIVE_APP_INSTALL = "com.yandex.mobile.ads.nativeads.NativeAppInstallAdView";
    public static final String NATIVE_CONTENT = "com.yandex.mobile.ads.nativeads.NativeContentAdView";
    public static final String NATIVE_IMAGE = "com.yandex.mobile.ads.nativeads.NativeImageAdView";
    public static final String VIDEO = "com.yandex.mobile.ads.video.YandexVideoAds";

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return BANNER;
    }


    @Override
    public boolean handleLoadPackage(final String packageName, final XC_LoadPackage.LoadPackageParam lpparam, final boolean removeAd) {
        boolean result = false;

        result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, INTERSTITIAL, "loadAd", lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, NATIVE, "loadAd", lpparam, removeAd);
//        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_APP_INSTALL, "getNativeAd", lpparam, removeAd);
//        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_CONTENT, "getNativeAd", lpparam, removeAd);
//        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_IMAGE, LOAD_AD, lpparam, removeAd);

        result |= ApiBlocking.blockAdFunction(packageName, VIDEO, "loadBlocksInfo", lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, VIDEO, "loadVideoAds", lpparam, removeAd);

        return result;
    }
}