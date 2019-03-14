package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Vungle extends Blocker
{
    public static final String VUNGLE_WARREN = "com.vungle.warren.Vungle";

    public static final String VUNGLE_WARREN_STORAGE = "com.vungle.warren.Storage";

    public static final String VUNGLE_PUBLISHER = "com.vungle.publisher.VunglePub";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, VUNGLE_WARREN_STORAGE, "findValidAdvertisementForPlacement", String.class, null, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_WARREN, "loadAd", String.class, "com.vungle.warren.LoadAdCallback", "com.vungle.warren.PublisherDirectDownload", lpparam);
        //result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_WARREN, "canPlayAd", String.class, lpparam);
        //result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_WARREN, "canPlayAd", "com.vungle.warren.model.Advertisement", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_PUBLISHER, "loadAd", String.class, lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE_PUBLISHER, "isAdPlayable", String.class, lpparam);

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
