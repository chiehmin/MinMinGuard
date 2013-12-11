package tw.fatminmin.xposed;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ModTrain extends ModApp {
	
	
	protected static String pkg = "idv.nightgospel.TWRailScheduleLookUp";
	
	@Override
	public void modMethod(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals(pkg))
			return;
		
		XposedBridge.log("Hacking " + pkg + "'s Methods");
		
		
		
		XposedHelpers.findAndHookMethod("com.vpon.ads.VponBanner", lpparam.classLoader, "loadAd"
			, "com.vpon.ads.VponAdRequest"	
			, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Prevent VponBanner loadAd " + pkg);
				
				param.setResult(new Object());
				
			}
		});
		
		
		XposedHelpers.findAndHookMethod("com.waystorm.ads.WSAdBanner", lpparam.classLoader, "setWSAdListener"
				, "com.waystorm.ads.WSAdListener"	
				, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					XposedBridge.log("Prevent WSAdBanner setWSAdListener " + pkg);
					
					param.setResult(new Object());
					
				}
			});
	}
	
	@Override
	public void modLayout(InitPackageResourcesParam resparam) {
		if(!resparam.packageName.equals(pkg))
			return;
		
		XposedBridge.log("Hacking " + pkg + "'s Layouts");
		
		
		resparam.res.hookLayout("idv.nightgospel.TWRailScheduleLookUp", "layout", "adlayout", new XC_LayoutInflated() {
			
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				
				XposedBridge.log("Handle train ad layout");
				
				View ad = (View) liparam.view.findViewById(
						liparam.res.getIdentifier("adLayout", "id", "idv.nightgospel.TWRailScheduleLookUp"));
				
				ad.setVisibility(View.GONE);
						
			}
		});
		
		
	}

}
