package tw.fatminmin.xposed.minminguard;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findField;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import tw.fatminmin.xposed.minminguard.custom_mod.ModTrain;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView;
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
	
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		pref = new XSharedPreferences(MY_PACKAGE_NAME);
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
			Class<?> vg = findClass("android.view.ViewGroup", lpparam.classLoader);
			
			findAndHookMethod(vg, "addView", "android.view.View",
				new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						if(param.args[0] instanceof WebView) {
							
							XposedBridge.log("Remove WebView in " + packageName);
							
							param.setResult(new Object());
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
				removeAdView(vg);
			}
		}
		
	}
	
	void modTosRoot(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals("com.madhead.tos.zh"))
			return;
		
		XposedBridge.log("Hacking tos");
		
		Class<?> acs = findClass("com.madhead.tos.plugins.acs.AntiCheatSystem", lpparam.classLoader);
		
		
		findAndHookMethod(acs, "isDeviceRooted", new XC_MethodHook() {
			
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Set isDeviceRooted result false");
				
				param.setResult(Boolean.valueOf(false));
			}
			
		});
		
		findAndHookMethod(acs, "isSecure", "java.lang.String", new XC_MethodHook() {
			
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Set isSecure result true");
				
				param.setResult(Boolean.valueOf(true));
			}
			
		});
		
		findAndHookMethod(acs, "getRunningProcess", new XC_MethodHook() {
			
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Set getRunningProcess result empty string");
				
				param.setResult(new String(""));
			}
			
		});
		
	}
	
	void modEcdict(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals("com.csst.ecdict"))
			return;
		
		
		XposedBridge.log("we are in ecdict!");
		
		
		final Class<?> c = findClass("com.csst.ecdict.DetailActivity", lpparam.classLoader);
		
		final Field f = findField(c, "i");
		
		final Class<?> adc = findClass("com.google.ads.AdView", lpparam.classLoader);
		final Method adm = findMethodBestMatch(adc, "removeAllViews", new Class[]{});
				
		
		findAndHookMethod("com.csst.ecdict.DetailActivity", lpparam.classLoader, "onCreate", "android.os.Bundle", new XC_MethodHook() {
			
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("End of detail activity");
				
				f.setAccessible(true);
				Object ad = f.get(param.thisObject);
				if(ad != null) {
					adm.invoke(ad, new Object[0]);
				}
			}
		});
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		new ModTrain().modLayout(resparam);
	}
}
