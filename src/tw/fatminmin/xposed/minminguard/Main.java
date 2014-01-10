package tw.fatminmin.xposed.minminguard;

import static de.robv.android.xposed.XposedHelpers.findClass;

import java.util.HashSet;
import java.util.Set;

import tw.fatminmin.xposed.minminguard.adnetwork.Admob;
import tw.fatminmin.xposed.minminguard.adnetwork.KuAd;
import tw.fatminmin.xposed.minminguard.adnetwork.MoPub;
import tw.fatminmin.xposed.minminguard.adnetwork.Vpon;
import tw.fatminmin.xposed.minminguard.custom_mod.ModTrain;
import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.content.res.XModuleResources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodHook.MethodHookParam;
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
	private static String MODULE_PATH = null;
	private static XSharedPreferences pref;
	private static Set<String> urls;
	private static Resources res;
	
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		pref = new XSharedPreferences(MY_PACKAGE_NAME);
		MODULE_PATH = startupParam.modulePath;
		XposedBridge.log(MODULE_PATH);
		
		res = XModuleResources.createInstance(MODULE_PATH, null);
		byte[] array = XposedHelpers.assetAsByteArray(res, "host/output_file");
		String decoded = new String(array);
		String[] sUrls = decoded.split("\n");
		
		urls = new HashSet<String>();
		for(String url : sUrls) {
			urls.add(url);
		}
		XposedBridge.log("Block url size: " + urls.size());
	}
	
	@SuppressLint("SdCardPath")
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		pref.reload();
		
		final String packageName = lpparam.packageName;
		
		if(pref.getBoolean(packageName, false)) {
			Admob.handleLoadPackage(packageName, lpparam, false);
			MoPub.handleLoadPackage(packageName, lpparam, false);
			Vpon.handleLoadPackage(packageName, lpparam, false);
			KuAd.handleLoadPackage(packageName, lpparam, false);
			removeWebViewAds(packageName, lpparam, false);
		}
	}
	
	
	boolean adExist = false;
	private boolean removeWebViewAds(final String packageName, LoadPackageParam lpparam, final boolean test) {
		
		try {
			
			Class<?> adView = findClass("android.webkit.WebView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "loadData", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					String data = (String) param.args[0];
					adExist = urlFiltering(data, param, packageName, test);
				}
				
			});
			
			XposedBridge.hookAllMethods(adView, "loadDataWithBaseURL", new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					String data = (String) param.args[1];
					adExist = urlFiltering(data, param, packageName, test);
				}
			});
			
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + "can not clear webview ads");
			return false;
		}
		return adExist;
	}
	
	private boolean urlFiltering(String data, MethodHookParam param, String packageName, boolean test) {
		
		boolean article = data.contains("title") && data.contains("body") && data.contains("head") && data.length() > 500;
		
		if(!article) {
			
			XposedBridge.log("Url filtering");
			
			String[] array = data.split("[/\\s)]");
			
			for(String hostname : array) {
				
				hostname = hostname.trim();
				
				if(hostname.contains(".") &&  hostname.length() > 5 && hostname.length() < 50) {
				
					if(urls.contains(hostname)) {
						
						XposedBridge.log("Detect Ads with hostname: " + hostname + " in " + packageName);
						if(!test) {
							param.setResult(new Object());
							removeAdView((View) param.thisObject, false);
							return true;
						}
						break;
					}
				}
			}
		}
		return false;
	}
	
	public static void removeAdView(View view, final boolean apiBased) {
		
		view.setVisibility(View.GONE);
		
		final ViewParent parent = view.getParent();
		if(parent instanceof ViewGroup) {
			final ViewGroup vg = (ViewGroup) parent;
			if(vg.getChildCount() == 1) {
				removeAdView(vg, apiBased);
			}
			else if(!apiBased){
				
				ViewTreeObserver observer= vg.getViewTreeObserver();
				observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		            @Override
		            public void onGlobalLayout() {
		            	float heightDp = convertPixelsToDp(vg.getHeight()); 
						if(heightDp <= 55) {
							
							for(int i = 0; i < vg.getChildCount(); i++)
								vg.getChildAt(i).setVisibility(View.GONE);
							
							removeAdView(vg, apiBased);
						}
		            }
		        });
			}
		}
		
	}
	
	private static float convertPixelsToDp(float px){
	    DisplayMetrics metrics = res.getDisplayMetrics();
	    float dp = px / (metrics.densityDpi / 160f);
	    return dp;
	}

	@Override
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		new ModTrain().modLayout(resparam);
	}
}
