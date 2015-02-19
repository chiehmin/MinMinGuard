package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Flurry {
    
    public final static String banner = "com.flurry.android.FlurryAds";
    public final static String bannerPrefix = "com.flurry.android";
    
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.flurry.android.FlurryAds", lpparam.classLoader);
			XposedBridge.hookAllMethods(adView, "fetchAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Util.log(packageName, "Detect FlurryAds fetchAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
				}
				
			});
			
			XposedBridge.hookAllMethods(adView, "displayAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Util.log(packageName, "Detect FlurryAds displayAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}
				
			});
			Util.log(packageName, packageName + " uses FlurryAds");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
