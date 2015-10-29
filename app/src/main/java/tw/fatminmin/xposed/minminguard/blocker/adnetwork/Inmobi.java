package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Inmobi extends Blocker {
    
    public final static String banner = "com.inmobi.monetization.IMBanner";
    public final static String bannerPrefix = "com.inmobi.monetization";

	@Override
	public String getBannerPrefix() {
		return bannerPrefix;
	}

	@Override
	public String getBanner() {
		return banner;
	}
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.inmobi.monetization.IMBanner", lpparam.classLoader);
			Class<?> adInter = XposedHelpers.findClass("com.inmobi.monetization.IMInterstitial", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "loadBanner", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect inmobi loadBanner in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
					
				}
			});
			
			XposedBridge.hookAllMethods(adInter, "loadInterstitial", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect IMInterstitial loadInterstitial in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
					}
					
				}
			});
			
			XposedBridge.hookAllMethods(adInter, "show", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect IMInterstitial show in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
					}
					
				}
			});
			
			Util.log(packageName, packageName + " uses inmobi");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
