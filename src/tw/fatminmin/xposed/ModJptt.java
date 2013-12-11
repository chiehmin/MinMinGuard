package tw.fatminmin.xposed;

import android.view.View;
import android.widget.RelativeLayout;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ModJptt {
	static String pkg = "com.joshua.jptt";
	
	RelativeLayout article_ad_bottom, article_ad_top;
	
	
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
					
					
					View v = (View) param.thisObject;
					v.setVisibility(View.GONE);
					
					
					try {
						article_ad_bottom.setVisibility(View.GONE);
					}
					catch(Exception e) {
						XposedBridge.log("setVisibility gone failed" + pkg);
					}
				}
			});
		XposedHelpers.findAndHookMethod("com.google.ads.AdView", lpparam.classLoader, "loadAd", 
				"com.google.ads.AdRequest", new XC_MethodHook() {
			
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					XposedBridge.log("Prevent google loadAd in " + pkg);
					
					param.setResult(new Object());
					
					
					View v = (View) param.thisObject;
					v.setVisibility(View.GONE);
					
					try {
						article_ad_bottom.setVisibility(View.GONE);
					}
					catch(Exception e) {
						XposedBridge.log("setVisibility gone failed" + pkg);
					}
				}
			
				});
		
		/*Class<?> c = XposedHelpers.findClass("com.google.ads.mediation.customevent.CustomEventBanner", lpparam.classLoader);
		Class<?> c2 = XposedHelpers.findClass("com.joshua.jptt.VponCustomAd", lpparam.classLoader);
		
		
		Method m = XposedHelpers.findMethodExact(c, "requestBannerAd", 
				"com.google.ads.mediation.customevent.CustomEventBannerListener", 
				"android.app.Activity", 
				"java.lang.String",
				"java.lang.String",
				"com.google.ads.AdSize", 
				"com.google.ads.mediation.MediationAdRequest", 
				"java.lang.Object");
		
		Method m2 = XposedHelpers.findMethodExact(c2, "getVponAdRequestByMediationAdRequest", 
				"com.google.ads.mediation.MediationAdRequest");
		Method m3 = XposedHelpers.findMethodExact(c2, "getVponAdSizeByAdSize", 
				"com.google.ads.AdSize");
		XposedBridge.hookMethod(m, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Prevent requestBannerAd in " + pkg);
				
				
				param.setResult(new Object());
				
			}
		});
		
		XposedBridge.hookMethod(m2, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Prevent getVponAdRequestByMediationAdRequest in " + pkg);
				
				
				param.setResult(new Object());
				
			}
		});
		
		XposedBridge.hookMethod(m3, new XC_MethodHook() {
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Prevent getVponAdSizeByAdSize in " + pkg);
				
				
				param.setResult(new Object());
				
			}
		});*/
	}
	
	
	
	
	void modLayout(InitPackageResourcesParam resparam) {
		if(!resparam.packageName.equals(pkg)) {
			return;
		}
		
		resparam.res.hookLayout("com.joshua.jptt", "layout", "article_intext", new XC_LayoutInflated() {

			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				
				
				
				article_ad_bottom = (RelativeLayout) liparam.view.findViewById(
						liparam.res.getIdentifier("article_ad_bottom", "id", "com.joshua.jptt"));
				
				article_ad_top = (RelativeLayout) liparam.view.findViewById(
						liparam.res.getIdentifier("article_ad_top", "id", "com.joshua.jptt"));
				
				article_ad_bottom.setVisibility(View.GONE);
				article_ad_top.setVisibility(View.GONE);
				
				
//				article_ad_bottom.setLayoutParams(new LayoutParams(320, LayoutParams.WRAP_CONTENT));
//				article_ad_top.setLayoutParams(new LayoutParams(320, LayoutParams.WRAP_CONTENT));
				
			}
		});
	}
	
	
}
