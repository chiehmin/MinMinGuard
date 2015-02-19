package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.Util;
import android.view.View;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Madvertise {
    
    public final static String banner = "de.madvertise.android.sdk.MadvertiseMraidView";
    public final static String bannerPrefix = "de.madvertise.android.sdk";
    
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			Class<?> adView = XposedHelpers.findClass("de.madvertise.android.sdk.MadvertiseMraidView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adView, "loadAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect Madvertise loadAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
					
				}
			});
			
			Util.log(packageName, packageName + " uses Madvertise");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
	public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
		
		final String packageName = "net.tsapps.appsales"; 
		
		if(!resparam.packageName.equals(packageName)) {
			return;
		}
		
		String[] files = {"fragment_watchdetail", "fragment_watchdetail_tablet", 
				"fragment_list", "fragment_saledetail", "fragment_saledetail_tablet"};
		
		for(String file : files) {
			resparam.res.hookLayout(packageName, "layout", file, new XC_LayoutInflated() {
				@Override
				public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
					
					Util.log(packageName, "Set view gone");
					final ViewGroup root = (ViewGroup) liparam.view.findViewById(
							liparam.res.getIdentifier("ll_adcontainer", "id", packageName));
					
					root.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
						
						@Override
						public void onChildViewRemoved(View parent, View child) {
							root.removeAllViews();
						}
						
						@Override
						public void onChildViewAdded(View parent, View child) {
							
						}
					});
					
					ss(root);
				}
			});
		}
		
	}
	private static void ss(View vv) {
		
		vv.setVisibility(View.GONE);
		
		if(vv instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) vv;
			vg.setVisibility(View.GONE);
			for(int i = 0; i < vg.getChildCount(); i++) {
				View v = vg.getChildAt(i);
				ss(v);
			}
		}
	}
}
