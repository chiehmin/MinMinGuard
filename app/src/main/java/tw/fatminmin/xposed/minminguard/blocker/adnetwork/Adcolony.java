package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Adcolony extends Blocker
{
    private static final String Adcolony_Storage = "com.adcolony.sdk.ab";

    private static final String INTER_ADS = "com.adcolony.sdk.AdColonyInterstitial";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result = ApiBlocking.blockAdFunctionWithResultExact(packageName, Adcolony_Storage, "a", String.class, 0.0d, lpparam);

        result |= ApiBlocking.blockAdFunctionWithResult(packageName, INTER_ADS, "isExpired", true, lpparam);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, INTER_ADS, "show", false, lpparam);

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
