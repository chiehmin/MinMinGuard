package tw.fatminmin.xposed.minminguard.blocker.custom_mod;

import android.view.View;

import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import tw.fatminmin.xposed.minminguard.Main;

/**
 * Created by fatminmin on 2015/10/30.
 */
public final class PeriodCalendar {

    public static String pkgName = "com.popularapp.periodcalendar";

    private PeriodCalendar() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    public static void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) {
        if(!resparam.packageName.equals(pkgName)) {
            return;
        }

        resparam.res.hookLayout(pkgName, "layout", "native_ad", new XC_LayoutInflated() {

            @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {

                View ad = (View) liparam.view.findViewById(
                        liparam.res.getIdentifier("native_layout", "id", pkgName));

                Main.removeAdView(ad, pkgName, true);
            }
        });
    }
}
