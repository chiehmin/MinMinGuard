package tw.fatminmin.xposed.minminguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Domob {
    
    public final static String banner = "cn.domob.android.ads.DomobAdView";
    public final static String bannerPrefix = "cn.domob.android.ads";
    
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            
            Class<?> adBanner = findClass("cn.domob.android.ads.DomobAdView", lpparam.classLoader);
            Class<?> adInter = findClass("cn.domob.android.ads.DomobInterstitialAd", lpparam.classLoader);
            
            XposedBridge.hookAllMethods(adBanner, "setAdEventListener", new XC_MethodHook() {
                
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect DomobAdView setAdEventListener in " + packageName);
                            
                            if(!test) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    
                    });
            XposedBridge.hookAllMethods(adInter, "setInterstitialAdListener", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect DomobInterstitialAd setInterstitialAdListener in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
            
            });
            
            XposedBridge.hookAllMethods(adInter, "setInterstitialAdListener", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect DomobInterstitialAd setInterstitialAdListener in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
            
            });
            
            Util.log(packageName, packageName + " uses Domob");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
