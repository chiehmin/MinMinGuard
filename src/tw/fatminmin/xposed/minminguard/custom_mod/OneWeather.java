package tw.fatminmin.xposed.minminguard.custom_mod;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class OneWeather {
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.handmark.expressweather.billing.BillingUtils", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "isPurchased", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    if(!test) {
                        param.setResult(Boolean.valueOf(true));
                    }
                }
                
            });
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
    public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
        if(!resparam.packageName.equals("com.handmark.expressweather")) {
            return;
        }
        
        resparam.res.hookLayout("com.handmark.expressweather", "layout", "main_phone", new XC_LayoutInflated() {
                
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
  
                        View ad = (View) liparam.view.findViewById(
                                        liparam.res.getIdentifier("adview", "id", "com.handmark.expressweather"));
                        
                        ad.setVisibility(View.GONE);
                        
                        ad = (View) liparam.view.findViewById(
                                liparam.res.getIdentifier("share_ad_cover", "id", "com.handmark.expressweather"));
                
                        ad.setVisibility(View.GONE);
                }
        });
    }
}
