package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import android.app.Activity;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class UnityAds extends Blocker
{

    public static final String UnityAds = "com.unity3d.ads.UnityAds";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, UnityAds, "show", lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, UnityAds, "canShow", false, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, UnityAds, "canShowAds", false, lpparam);

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

