package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Avocarrot extends Blocker
{

    private static final String PREFIX = "com.avocarrot.androidsdk";

    private static final String AD_CONTROLLER = "com.avocarrot.androidsdk.BaseController";
    private static final String AD_CONTROLLER_FUNC = "loadAd";

    @Override
    public boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunction(packageName, AD_CONTROLLER, AD_CONTROLLER_FUNC, lpparam);

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
        return PREFIX;
    }
}   