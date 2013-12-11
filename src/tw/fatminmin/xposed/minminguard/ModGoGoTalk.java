package tw.fatminmin.xposed.minminguard;

import android.view.View;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ModGoGoTalk {
	
	static String pkg = "gogolook.callgogolook2";
	static String android_id = "adarea";
	
	static String[] modLayout = { "about_version_activity", "blocklist_setting", "contactlist_activity"
							, "favorite_activity", "main_activity", "result_activity", "result_activity2"
							, "search_activity", "setting_activity", "whoscore_activity" };
	
	
	void modMethod(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals(pkg))
			return;
		
		XposedBridge.log("Hacking " + pkg + "'s Methods");
		
		XposedHelpers.findAndHookMethod("com.vpon.ads.VponBanner", lpparam.classLoader, "loadAd"
				, "com.vpon.ads.VponAdRequest"	
				, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					XposedBridge.log("Prevent VponBanner loadAd in " + pkg);
					
					param.setResult(new Object());
					
				}
			});
		XposedHelpers.findAndHookMethod("com.google.ads.AdView", lpparam.classLoader, "loadAd", 
				"com.google.ads.AdRequest", new XC_MethodHook() {
			
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					XposedBridge.log("Prevent google loadAd in " + pkg);
					
					param.setResult(new Object());
					

				}
			
				});
		
	}
	
	void modLayout(InitPackageResourcesParam resparam) {
		if(!resparam.packageName.equals(pkg)) {
			return;
		}
		
		for(final String layout : modLayout) {
			
			
			resparam.res.hookLayout(pkg, "layout", layout, new XC_LayoutInflated() {
				
				@Override
				public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
					
					
					XposedBridge.log("Hacking GoGoTalk layout: " + layout);
					
					View v = liparam.view.findViewById(
							liparam.res.getIdentifier(android_id, "id", pkg));
					
					setGoneRecursive(v);
				}
			});
		}	
	}
	
	void setGoneRecursive(View v) {
		
		v.setVisibility(View.GONE);
		
		if(v instanceof ViewGroup) {
			
			ViewGroup vg = (ViewGroup) v;
			
			for(int i = 0; i < vg.getChildCount(); i++) {
				setGoneRecursive(vg.getChildAt(i));
			}
		}
		
		
	}
	
}
