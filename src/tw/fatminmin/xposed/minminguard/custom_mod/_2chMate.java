package tw.fatminmin.xposed.minminguard.custom_mod;

import static de.robv.android.xposed.XposedHelpers.findClass;
import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class _2chMate {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		
		if(!packageName.equals("jp.co.airfront.android.a2chMate")) {
			return false;
		}
		
		try {
					
			Class<?> adview = findClass("jp.syoboi.a2chMate.view.MyAdView", lpparam.classLoader);

			XposedHelpers.findAndHookMethod(adview, "a", int.class, String.class, new XC_MethodHook() {
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

				    Util.log(packageName, "Detect 2chmate MyAdView in " + packageName);

					if (!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}

			});
			Util.log(packageName, packageName + " is 2chmate");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
        if(!resparam.packageName.equals("jp.co.airfront.android.a2chMate")) {
            return;
        }
        
        resparam.res.hookLayout("jp.co.airfront.android.a2chMate", "layout", "a2ch_ad", new XC_LayoutInflated() {
                
                @Override
            public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
                liparam.view.setVisibility(View.GONE);
                liparam.view.getLayoutParams().height = 0;
            }
        });
    }
}
