package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.Util;

import java.util.ArrayList;

public final class Viafree
{
    private static final String pkg = "viafree.android";
    private static final String className = "com.viafree.android.videoplayer.ad.models.Freewheel";
    private static final String method = "f";

    private Viafree() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static boolean handleLoadPackage(final String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        if (!lpparam.packageName.contains(pkg))
        {
            return false;
        }

        XposedHelpers.findAndHookMethod(className, lpparam.classLoader, method, new XC_MethodHook()
        {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable
            {
                param.setResult(new ArrayList<Object>());
                Util.notifyRemoveAdView(null, packageName, 1);
            }
        });

        return true;
    }
}
