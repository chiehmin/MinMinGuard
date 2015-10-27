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

public class MoPub extends Blocker {
    
    public final static String banner = "com.mopub.mobileads.MoPubView";
    public final static String bannerPrefix = "com.mopub.mobileads";
    public final static String inter = "com.mopub.mobileads.MoPubInterstitial";
    public final static String nativeAd = "com.mopub.nativeads.MoPubAdAdapter";

    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }

    @Override
    public String getBanner() {
        return banner;
    }
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
					
			Class<?> mopubBanner = findClass(banner, lpparam.classLoader);
            Class<?> mopubInter = findClass(inter, lpparam.classLoader);
            Class<?> mopubNativeAd = findClass(nativeAd, lpparam.classLoader);
			
			XposedBridge.hookAllMethods(mopubBanner, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect MoPub loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			;

            XposedBridge.hookAllMethods(mopubInter, "load", new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect mopubInter load in " + packageName);

                    if(!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }

            });

            XposedBridge.hookAllMethods(mopubNativeAd, "loadAds",  new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    Util.log(packageName, "Detect mopubNativeAd loadAds in " + packageName);

                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });

			Util.log(packageName, packageName + " uses MoPub");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
