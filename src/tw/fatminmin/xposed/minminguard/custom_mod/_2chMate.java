package tw.fatminmin.xposed.minminguard.custom_mod;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class _2chMate {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		
		if(!packageName.equals("jp.co.airfront.android.a2chMate")) {
			return false;
		}
		
		try {
					
			Class<?> adview = findClass("jp.syoboi.a2chMate.view.MyAdView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adview, "c", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							XposedBridge.log("Detect 2chmate MyAdView in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, true);
							}
						}
					
					});
			XposedBridge.log(packageName + " is 2chmate");
		}
		catch(ClassNotFoundError e) {
			
			return false;
		}
		return true;
	}
}
