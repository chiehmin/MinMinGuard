package tw.fatminmin.xposed.minminguard.adnetwork;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Main;

public class Nend {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> adView = XposedHelpers.findClass("net.nend.android.NendAdView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "loadAd" ,new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							XposedBridge.log("Detect NendAdView loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, true);
							}
						}
					});
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use Nend");
			return false;
		}
		return true;
	}
}
