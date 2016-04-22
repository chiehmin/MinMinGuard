package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class GmsDoubleClick extends Blocker {
    
    public static final String BANNER = "com.google.android.gms.ads.doubleclick.PublisherAdView";
    public static final String BANNER_PREFIX = "com.google.android.gms.ads.doubleclick";

	@Override
	public String getBannerPrefix() {
		return BANNER_PREFIX;
	}

	@Override
	public String getBanner() {
		return BANNER;
	}
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {
			
			Class<?> admobBanner = findClass("com.google.android.gms.ads.doubleclick.PublisherAdView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(admobBanner, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect GmsDoubleClick PublisherAdView loadAd in " + packageName);
							
							if(removeAd) {
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
