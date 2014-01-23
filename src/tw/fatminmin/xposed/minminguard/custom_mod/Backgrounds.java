package tw.fatminmin.xposed.minminguard.custom_mod;

import android.view.View;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Backgrounds {
    public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
        if(!resparam.packageName.equals("com.ogqcorp.bgh")) {
                return;
        }
        
        resparam.res.hookLayout("com.ogqcorp.bgh", "layout", "inc_ads", new XC_LayoutInflated() {
                
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
  
                        View ad = (View) liparam.view.findViewById(
                                        liparam.res.getIdentifier("ads", "id", "com.ogqcorp.bgh"));
                        
                        ad.setVisibility(View.GONE);                                        
                }
        });
    }
}
