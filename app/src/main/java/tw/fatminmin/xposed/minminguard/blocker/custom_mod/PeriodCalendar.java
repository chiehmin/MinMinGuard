package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import android.view.View;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import tw.fatminmin.xposed.minminguard.blocker.ViewBlocking;

/**
 * Created by fatminmin on 2015/10/30.
 */
final class PeriodCalendar
{

    private static String pkgName = "com.popularapp.periodcalendar";

    private PeriodCalendar() throws InstantiationException
    {
        throw new InstantiationException("This class is not for instantiation");
    }

    //TODO Lets check if we are using an Xposed version that supports resource hooking
    public static void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam)
    {
        if (!resparam.packageName.equals(pkgName))
        {
            return;
        }

        resparam.res.hookLayout(pkgName, "layout", "native_ad", new XC_LayoutInflated()
        {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam)
            {

                View ad = liparam.view.findViewById(liparam.res.getIdentifier("native_layout", "id", pkgName));

                ViewBlocking.removeAdView(pkgName, ad);
            }
        });
    }
}
