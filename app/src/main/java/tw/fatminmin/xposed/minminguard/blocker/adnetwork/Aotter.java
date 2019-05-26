package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

/**
 * Created by fatminmin on 5/18/16.
 */
public class Aotter extends Blocker
{

    private static final String BANNER_PREFIX = "com.aotter.net.trek.ads";
    private static final String NATIVE_ADS = "com.aotter.net.trek.ads.TKAdN";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "setAdListener", lpparam);
        result |= ApiBlocking.blockAdFunction(packageName, NATIVE_ADS, "registerViewForInteraction", lpparam);

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
        return BANNER_PREFIX;
    }
}
