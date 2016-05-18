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

public class Admob extends Blocker {
    
    public static final String BANNER = "com.google.ads.AdView";
    public static final String BANNER_PREFIX = "com.google.ads";

	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {
			
			Class<?> admobBanner = findClass("com.google.ads.AdView", lpparam.classLoader);
			Class<?> admobInter = findClass("com.google.ads.InterstitialAd", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(admobBanner, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect AdmobBanner loadAd in " + packageName);
							
							if(removeAd) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			
			XposedBridge.hookAllMethods(admobInter, "loadAd",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect Admob InterstitialAd loadAd in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
					}
				}
			});
			
			XposedBridge.hookAllMethods(admobInter, "show",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect Admob InterstitialAd show in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
					}
				}
			});
			
			Util.log(packageName, packageName + " uses Admob");
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
