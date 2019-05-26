package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Vungle extends Blocker
{
    private static final String VUNGLE_WARREN = "com.vungle.warren.Vungle";

    private static final String VUNGLE_WARREN_STORAGE = "com.vungle.warren.Storage";

    private static final String VUNGLE_PUBLISHER = "com.vungle.publisher.VunglePub";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunctionWithResult(packageName, VUNGLE_WARREN_STORAGE, "findValidAdvertisementForPlacement", null, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_WARREN, "loadAd", lpparam);

        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_PUBLISHER, "loadAd", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_PUBLISHER, "isAdPlayable", lpparam);

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
