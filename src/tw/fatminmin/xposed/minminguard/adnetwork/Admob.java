package tw.fatminmin.xposed.minminguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Admob {
    
    public final static String banner = "com.google.ads.AdView";
    public final static String bannerPrefix = "com.google.ads";

	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> admobBanner = findClass("com.google.ads.AdView", lpparam.classLoader);
			Class<?> admobInter = findClass("com.google.ads.InterstitialAd", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(admobBanner, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect AdmobBanner loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			
			XposedBridge.hookAllMethods(admobInter, "loadAd",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect Admob InterstitialAd loadAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
				}
			});
			
			XposedBridge.hookAllMethods(admobInter, "show",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect Admob InterstitialAd show in " + packageName);
					
					if(!test) {
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
}
