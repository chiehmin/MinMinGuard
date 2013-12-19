package tw.fatminmin.xposed;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.webkit.WebView;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class ModGpsStatus {
	static String pkg = "com.eclipsim.gpsstatus2";
	
	
	void modMethod(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals(pkg))
			return;
		
		XposedBridge.log("Inside GpsStatus");
		
		findAndHookMethod("android.view.ViewGroup", lpparam.classLoader, "addView",
				"android.view.View", new XC_MethodHook() {
			
					@Override
					protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
						if(param.args[0] instanceof WebView) {
							
							XposedBridge.log("Hacking GpsStatus remove WebView");
							
							param.setResult(new Object());
						}
					}
			
				});
	}
	
}
