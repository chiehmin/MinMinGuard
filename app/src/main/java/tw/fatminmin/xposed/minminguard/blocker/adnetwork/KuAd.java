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

public class KuAd extends Blocker {
    
    public static final String BANNER = "com.waystorm.ads.WSAdBanner";
    public static final String BANNER_PREFIX = "com.waystorm.ads";

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
			
			Class<?> wsBanner = findClass("com.waystorm.ads.WSAdBanner", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(wsBanner, "setWSAdListener", new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect WSAdBanner setWSAdListener " + packageName);
							
							if(removeAd) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
							new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    
                                }
                            });
						}
					});
			
			XposedBridge.hookAllMethods(wsBanner, "setApplicationId", new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect WSAdBanner setApplicationId " + packageName);
							
							if(removeAd) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					});
			
			XposedBridge.hookAllMethods(wsBanner, "mediationLoadAd", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect WSAdBanner mediationLoadAd " + packageName);
                            
                            if(removeAd) {
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
