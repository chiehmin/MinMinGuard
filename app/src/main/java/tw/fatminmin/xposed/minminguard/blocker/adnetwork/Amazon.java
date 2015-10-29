package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Amazon extends Blocker {
    
    public final static String banner = "com.amazon.device.ads.AdLayout";
    public final static String bannerPrefix = "com.amazon.device.ads";
    
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.amazon.device.ads.AdLayout", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "setListener", new XC_MethodHook() {
			   
			    @Override
			    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
			        
			        Util.log(packageName, "Prevent amazon setlistener");
			        
			        if(removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
			    }
			});
			
			XposedBridge.hookAllMethods(adView, "loadAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect Amazon loadAd in " + packageName);
					
					if(removeAd) {
						param.setResult(Boolean.valueOf(true));
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}
			});
			
			Util.log(packageName, packageName + " uses Amazon");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
	@Override
	public String getBannerPrefix() {
		return bannerPrefix;
	}

	@Override
	public String getBanner() {
		return banner;
	}
}
