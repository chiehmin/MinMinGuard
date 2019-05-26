package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

//TODO Function stuff
public class Startapp extends Blocker
{

    // API document: https://github.com/StartApp-SDK/Documentation/wiki/Android-InApp-Documentation

    private static final String SDK = "com.startapp.android.publish.StartAppSDK";
    private static final String SDK_INIT = "init";

    private static final String BANNER = "com.startapp.android.publish.HtmlAd";
    private static final String BANNER_FUNC = "show";
    private static final String BANNER_PREFIX = "com.startapp.android.publish";

    private static final String NATIVE_AD = "com.startapp.android.publish.nativead.StartAppNativeAd";
    private static final String NATIVE_AD_FUNC = "getNativeAds";

    private static final String INTER = "com.startapp.android.publish.StartAppAd";
    private static final String INTER_FUNC1 = "showAd";
    private static final String INTER_FUNC2 = "showSplash";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunction(packageName, SDK, SDK_INIT, lpparam);
        result |= ApiBlocking.removeBanner(packageName, BANNER, BANNER_FUNC, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_AD, NATIVE_AD_FUNC, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, INTER_FUNC1, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, INTER_FUNC2, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, INTER, INTER_FUNC2, lpparam);

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
