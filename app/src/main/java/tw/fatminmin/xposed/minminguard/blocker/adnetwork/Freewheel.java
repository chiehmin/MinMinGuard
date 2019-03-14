package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import org.w3c.dom.Element;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Freewheel extends Blocker
{
    public static final String AdInstace = "tv.freewheel.ad.AdInstance";

    public static final String Ad = "tv.freewheel.ad.Ad";

    public static final String Slot = "tv.freewheel.ad.slot.Slot";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        result |= ApiBlocking.blockAdFunction(packageName, Ad, "parse", lpparam);
        /*result |= ApiBlocking.blockAdFunctionWithResult(packageName, AdInstace, "isPlayable", false, lpparam, removeAd);
        result |= ApiBlocking.blockAdFunctionWithResult(packageName, AdInstace, "isRequiredToShow", false, lpparam, removeAd);
        */

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