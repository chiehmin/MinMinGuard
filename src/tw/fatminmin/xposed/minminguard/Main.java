package tw.fatminmin.xposed.minminguard;

import static de.robv.android.xposed.XposedHelpers.findClass;

import java.util.Set;

import tw.fatminmin.xposed.minminguard.custom_mod.ModTrain;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit,
							 IXposedHookLoadPackage, 
							 IXposedHookInitPackageResources {
	
	
	public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
	private static XSharedPreferences pref;
	private static Set<String> urls;
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		pref = new XSharedPreferences(MY_PACKAGE_NAME);
		urls = pref.getStringSet("urls", null);
		
		XposedBridge.log("Number of Urls " + urls.size());
	}
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		pref.reload();
		
		final String packageName = lpparam.packageName;
		
		if(pref.getBoolean(packageName, false)) {
			
			boolean blocked = false;
			
			blocked |= handleAdmobAds(packageName, lpparam);
			blocked |= handleVponAds(packageName, lpparam);
			blocked |= handleKuAds(packageName, lpparam);
			
			if(blocked == false) {
				removeWebViewAds(packageName, lpparam);
			}
		}
	}
	
	
	private boolean handleAdmobAds(final String packageName, LoadPackageParam lpparam) {
		try {
			
			Class<?> admobBanner = findClass("com.google.ads.AdView", lpparam.classLoader);
			
			XposedHelpers.findAndHookMethod(admobBanner, "loadAd", 
					"com.google.ads.AdRequest", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							XposedBridge.log("Prevent Admob loadAd in " + packageName);
							
							param.setResult(new Object());
							
							removeAdView((View) param.thisObject);
						}
					
					});
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use Admob");
			return false;
		}
		return true;
	}
	private boolean handleVponAds(final String packageName, LoadPackageParam lpparam) {
		try {
			XposedHelpers.findAndHookMethod("com.vpon.ads.VponBanner", lpparam.classLoader, "loadAd"
					, "com.vpon.ads.VponAdRequest" ,new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							XposedBridge.log("Prevent VponBanner loadAd in " + packageName);
							
							param.setResult(new Object());
							
							removeAdView((View) param.thisObject);
						}
					});
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use Vpon");
			return false;
		}
		return true;
	}
	private boolean handleKuAds(final String packageName, LoadPackageParam lpparam) {
		try {
			
			Class<?> wsBanner = findClass("com.waystorm.ads.WSAdBanner", lpparam.classLoader);
			Class<?> wsListener = findClass("com.waystorm.ads.WSAdListener", lpparam.classLoader);
			
			XposedHelpers.findAndHookMethod(wsBanner, "setWSAdListener", "com.waystorm.ads.WSAdListener", 
					new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							XposedBridge.log("Prevent WSAdBanner setWSAdListener " + packageName);
							
							param.setResult(new Object());
							
							removeAdView((View) param.thisObject);
						}
					});
			
			XposedHelpers.findAndHookMethod(wsBanner, "setApplicationId", "java.lang.String",  
					new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							XposedBridge.log("Prevent WSAdBanner setApplicationId " + packageName);
							
							param.setResult(new Object());
							
							removeAdView((View) param.thisObject);
						}
					});
			XposedHelpers.findAndHookMethod(wsListener, "onReceived", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						
						XposedBridge.log("Prevent WSDlistener onreceived " + packageName);
						
						param.setResult(new Object());
					}
				});
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use kuAd");
			return false;
		}
		return true;
	}
	
	private void removeWebViewAds(final String packageName, LoadPackageParam lpparam) {
		
		try {
			
			Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);
			XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					
					String data = (String) param.args[0];
					XposedBridge.log("Data: " + data);
					
//					param.setResult(new Object());
//					removeAdView((View) param.thisObject);
				}
			});
			
			XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					XposedBridge.log("Prevent Ads in " + packageName);
					
					String data = (String) param.args[1];
					
					for(String url : urls) {
						
						if(data.contains(url)) {
							param.setResult(new Object());
							removeAdView((View) param.thisObject);
							break;
						}
					}
				}
			});
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + "can not clear webview ads");
		}
	}
	
	private void removeAdView(View view) {
		
		view.setVisibility(View.GONE);
		
		ViewParent parent = view.getParent();
		if(parent instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) parent;
			if(vg.getChildCount() == 1) {
				vg.removeAllViewsInLayout();
				removeAdView(vg);
			}
		}
		
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		new ModTrain().modLayout(resparam);
	}
}
