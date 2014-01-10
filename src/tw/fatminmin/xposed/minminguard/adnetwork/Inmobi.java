package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Inmobi {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.inmobi.monetization.IMBanner", lpparam.classLoader);
			Class<?> adInter = XposedHelpers.findClass("com.inmobi.monetization.IMInterstitial", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "loadBanner", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					XposedBridge.log("Detect inmobi loadBanner in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, true);
					}
					
				}
			});
			
			XposedBridge.hookAllMethods(adInter, "loadInterstitial", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					XposedBridge.log("Detect IMInterstitial loadInterstitial in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
					
				}
			});
			
			XposedBridge.hookAllMethods(adInter, "show", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					XposedBridge.log("Detect IMInterstitial show in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
					
				}
			});
			
			XposedBridge.log(packageName + " uses inmobi");
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use inmobi");
			return false;
		}
		return true;
	}
}
