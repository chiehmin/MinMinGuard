package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class AdmobGms extends Blocker {

	private static final String LOAD_AD = "loadAd";
    public static final String BANNER = "com.google.android.gms.ads.AdView";
    public static final String BANNER_PREFIX = "com.google.android.gms.ads";

	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {
			
			Class<?> admobBanner = findClass("com.google.android.gms.ads.AdView", lpparam.classLoader);
			Class<?> admobSearchBanner = findClass("com.google.android.gms.ads.search.SearchAdView", lpparam.classLoader);
			Class<?> admobInter = findClass("com.google.android.gms.ads.InterstitialAd", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(admobBanner, LOAD_AD, new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect AdmobGms Banner loadAd in " + packageName);
							
							if(removeAd) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			
			XposedBridge.hookAllMethods(admobSearchBanner, LOAD_AD, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect AdmobGms SearchBanner loadAd in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}
			
			});
			
			XposedBridge.hookAllMethods(admobInter, LOAD_AD,  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect AdmobGms InterstitialAd loadAd in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
					}
				}
			});
			
			XposedBridge.hookAllMethods(admobInter, "show",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect AdmobGms InterstitialAd show in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
					}
				}
			});
			
			Util.log(packageName, packageName + " uses AdmobGms");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
	@Override
	public String getBannerPrefix() {
		return BANNER_PREFIX;
	}

	@Override
	public String getBanner() {
		return BANNER;
	}
}
