package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import android.view.ViewGroup;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Madvertise extends Blocker {
    
    public static final String BANNER = "de.madvertise.android.sdk.MadvertiseMraidView";
    public static final String BANNER_PREFIX = "de.madvertise.android.sdk";

	@Override
	public String getBannerPrefix() {
		return BANNER_PREFIX;
	}

	@Override
	public String getBanner() {
		return BANNER;
	}
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		boolean result = false;
		result |= ApiBlocking.removeBanner(packageName, BANNER, "loadAd", lpparam, removeAd);

		return result;
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
