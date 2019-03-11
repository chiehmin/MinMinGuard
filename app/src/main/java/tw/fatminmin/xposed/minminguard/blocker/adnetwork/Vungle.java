package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Vungle extends Blocker
{
    public static final String VUNGLE = "com.vungle.warren.Vungle";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam, boolean removeAd)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, VUNGLE, "canPlayAd", "com.vungle.warren.model.Advertisement", lpparam, removeAd);

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
