package tw.fatminmin.xposed.minminguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class KuAd {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> wsBanner = findClass("com.waystorm.ads.WSAdBanner", lpparam.classLoader);
			Class<?> wsListener = findClass("com.waystorm.ads.WSAdListener", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(wsBanner, "setWSAdListener", new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect WSAdBanner setWSAdListener " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					});
			
			XposedBridge.hookAllMethods(wsBanner, "setApplicationId", new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect WSAdBanner setApplicationId " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					});
			XposedBridge.hookAllMethods(wsListener, "onReceived", new XC_MethodHook() {
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						
						Util.log(packageName, "Detect WSDlistener onreceived " + packageName);
						
						if(!test) {
							param.setResult(new Object());
						}
					}
				});
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
