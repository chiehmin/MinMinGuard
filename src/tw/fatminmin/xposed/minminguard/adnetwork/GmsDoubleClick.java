package tw.fatminmin.xposed.minminguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class GmsDoubleClick {
    
    public final static String banner = "com.google.android.gms.ads.doubleclick.PublisherAdView";
    public final static String bannerPrefix = "com.google.android.gms.ads.doubleclick";
    
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> admobBanner = findClass("com.google.android.gms.ads.doubleclick.PublisherAdView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(admobBanner, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect GmsDoubleClick PublisherAdView loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			
			
			Util.log(packageName, packageName + " uses GmsDoubleClick");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
