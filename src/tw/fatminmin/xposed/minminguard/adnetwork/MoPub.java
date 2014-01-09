package tw.fatminmin.xposed.minminguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MoPub {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
					
			Class<?> adview = findClass("com.mopub.mobileads.MoPubView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adview, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							XposedBridge.log("Detect MoPub loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject);
							}
						}
					
					});
			XposedBridge.log(packageName + " uses MoPub");
		}
		catch(ClassNotFoundError e) {
			XposedBridge.log(packageName + " does not use MoPub");
			return false;
		}
		return true;
	}
}
