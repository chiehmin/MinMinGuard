package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Flurry {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.flurry.android.FlurryAds", lpparam.classLoader);
			XposedBridge.hookAllMethods(adView, "fetchAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					XposedBridge.log("Detect FlurryAds fetchAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
				}
				
			});
			
			XposedBridge.hookAllMethods(adView, "displayAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					XposedBridge.log("Detect FlurryAds displayAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, true);
					}
				}
				
			});
			XposedBridge.log(packageName + " uses FlurryAds");
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use FlurryAds");
			return false;
		}
		return true;
	}
}
