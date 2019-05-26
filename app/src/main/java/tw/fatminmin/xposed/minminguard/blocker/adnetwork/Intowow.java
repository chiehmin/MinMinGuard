package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

//TODO Fix all these declared fields.......
public class Intowow extends Blocker
{

    private static final String BANNER = "com.intowow.sdk.BannerAD";
    private static final String BANNER_FUNC = "onStart";
    private static final String BANNER_PREFIX = "com.intowow.sdk";

    private static final String SPLASH_AD = "com.intowow.sdk.SplashAD";
    private static final String SPLASH_AD_FUNC = "show";

    private static final String I2WAPI = "com.intowow.sdk.I2WAPI";
    private static final String API_FUNC_1 = "requesSingleOfferAD";
    private static final String API_FUNC_2 = "requesSplashAD";
    private static final String API_FUNC_3 = "requestHybridSplashAD";

    private static final String CONTENT_AD_HELPER = "com.intowow.sdk.ContentADHelper";
    private static final String HELPER_FUNC = "requestAD";

    private static final String STREAM_HELPER = "com.intowow.sdk.StreamHelper";

    public static final String STREAM_HELPER_FUNC_1 = "getAD";
    private static final String STREAM_HELPER_FUNC_2 = "setActive";
    private static final String STREAM_HELPER_FUNC_3 = "setListener";

    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.removeBanner(packageName, BANNER, BANNER_FUNC, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, SPLASH_AD, SPLASH_AD_FUNC, lpparam);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, API_FUNC_1, null, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, API_FUNC_2, null, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, I2WAPI, API_FUNC_3, null, lpparam);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, CONTENT_AD_HELPER, HELPER_FUNC, null, lpparam);

//        result |= ApiBlocking.blockAdFunctionWithResult(packageName, streamHelper, streamHelperFunc1, null, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunction(packageName, STREAM_HELPER, STREAM_HELPER_FUNC_2, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, STREAM_HELPER, STREAM_HELPER_FUNC_3, lpparam);

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
