package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;

/**
 * Created by fatminmin on 2016/2/7.
 */
//FIXME What is this?
public final class NextMedia
{

    public static final String AD_UTILS = "com.nextmediatw.data.AdUtils";

    private NextMedia() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    static public void handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        ApiBlocking.blockAdFunctionWithResult(packageName, AD_UTILS, "skipAd", true, lpparam);
    }
}
