package tw.fatminmin.xposed.minminguard.custom_mod;

import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Train {
	
	
	protected static String pkg = "idv.nightgospel.TWRailScheduleLookUp";
	
	public static void handleLoadPackage(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals(pkg))
			return;
		
		XposedHelpers.findAndHookMethod("com.waystorm.ads.WSAdBanner", lpparam.classLoader, "setWSAdListener"
				, "com.waystorm.ads.WSAdListener"	
				, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(pkg, "Prevent WSAdBanner setWSAdListener " + pkg);
					
					param.setResult(new Object());
					
				}
			});
	}
	
	 public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
		if(!resparam.packageName.equals(pkg))
			return;
		
		resparam.res.hookLayout("idv.nightgospel.TWRailScheduleLookUp", "layout", "adlayout", new XC_LayoutInflated() {
			
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				
				Util.log(pkg, "Handle train ad layout");
				
				View ad = (View) liparam.view.findViewById(
						liparam.res.getIdentifier("adLayout", "id", "idv.nightgospel.TWRailScheduleLookUp"));
				
				ad.setVisibility(View.GONE);
						
			}
		});
		
		
	}

}
