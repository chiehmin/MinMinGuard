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
    
    public final static String banner = "com.waystorm.ads.WSAdBanner";
    public final static String bannerPrefix = "com.waystorm.ads";
    
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> wsBanner = findClass("com.waystorm.ads.WSAdBanner", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(wsBanner, "setWSAdListener", new XC_MethodHook() {
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect WSAdBanner setWSAdListener " + packageName);
							
							if(!test) {
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
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					});
			
			XposedBridge.hookAllMethods(wsBanner, "mediationLoadAd", new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect WSAdBanner mediationLoadAd " + packageName);
                            
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
