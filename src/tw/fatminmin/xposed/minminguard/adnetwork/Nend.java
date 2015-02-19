package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Nend {
    
    public final static String banner = "net.nend.android.NendAdView";
    public final static String bannerPrefix = "net.nend.android";
    
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> adView = XposedHelpers.findClass("net.nend.android.NendAdView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "loadAd" ,new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect NendAdView loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
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
