package tw.fatminmin.xposed.minminguard;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.findField;
import static de.robv.android.xposed.XposedHelpers.findMethodBestMatch;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.view.View;
import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Main implements IXposedHookZygoteInit,
							 IXposedHookLoadPackage, 
							 IXposedHookInitPackageResources {
	
	
	public static final String MY_PACKAGE_NAME = Main.class.getPackage().getName();
	private static XSharedPreferences pref;
	
	private static ModGoGoTalk modGoGoTalk = new ModGoGoTalk();
	private static ModGpsStatus modGpsStatus = new ModGpsStatus();
	private static ModJptt modJptt = new ModJptt();
	private static ModTrain modTrain = new ModTrain();
	
	
	@Override
	public void initZygote(StartupParam startupParam) throws Throwable {
		pref = new XSharedPreferences(MY_PACKAGE_NAME);
	}
	
	@Override
	public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
		
		pref.reload();
		
		if(pref.getBoolean("block_ecdict_ad", true)) {
			modEcdict(lpparam);
		}
		if(pref.getBoolean("anti_tos_cheat_detect", true)) {
			modTosRoot(lpparam);
		}
		if(pref.getBoolean("block_jptt_ad", true)) {
			modJptt.modMethod(lpparam);
		}
		if(pref.getBoolean("block_gogotalk_ad", true)) {
			modGoGoTalk.modMethod(lpparam);
		}
		if(pref.getBoolean("block_gpsstatus_ad", true)) {
			modGpsStatus.modMethod(lpparam);
		}
		if(pref.getBoolean("block_train_ad", true)) {
			modTrain.modMethod(lpparam);
		}
	}
	
	@Override 
	public void handleInitPackageResources(InitPackageResourcesParam resparam) throws Throwable {
		
		pref.reload();
		
		if(pref.getBoolean("block_backgroundsHD_ad", true)) {
			modBackgroundHDLayout(resparam);
		}
		if(pref.getBoolean("block_jptt_ad", true)) {
			modJptt.modLayout(resparam);
		}
		if(pref.getBoolean("block_train_ad", true)) {
			modTrain.modLayout(resparam);
		}
	}
	
	void modTosRoot(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals("com.madhead.tos.zh"))
			return;
		
		XposedBridge.log("Hacking tos");
		
		Class<?> acs = findClass("com.madhead.tos.plugins.acs.AntiCheatSystem", lpparam.classLoader);
		
		
		findAndHookMethod(acs, "isDeviceRooted", new XC_MethodHook() {
			
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Set isDeviceRooted result false");
				
				param.setResult(Boolean.valueOf(false));
			}
			
		});
		
		findAndHookMethod(acs, "isSecure", "java.lang.String", new XC_MethodHook() {
			
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Set isSecure result true");
				
				param.setResult(Boolean.valueOf(true));
			}
			
		});
		
		findAndHookMethod(acs, "getRunningProcess", new XC_MethodHook() {
			
			@Override
			protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("Set getRunningProcess result empty string");
				
				param.setResult(new String(""));
			}
			
		});
		
	}
	
	void modEcdict(LoadPackageParam lpparam) {
		if(!lpparam.packageName.equals("com.csst.ecdict"))
			return;
		
		
		XposedBridge.log("we are in ecdict!");
		
		
		final Class<?> c = findClass("com.csst.ecdict.DetailActivity", lpparam.classLoader);
		
		final Field f = findField(c, "i");
		
		final Class<?> adc = findClass("com.google.ads.AdView", lpparam.classLoader);
		final Method adm = findMethodBestMatch(adc, "removeAllViews", new Class[]{});
				
		
		findAndHookMethod("com.csst.ecdict.DetailActivity", lpparam.classLoader, "onCreate", "android.os.Bundle", new XC_MethodHook() {
			
			@Override
			protected void afterHookedMethod(MethodHookParam param) throws Throwable {
				
				XposedBridge.log("End of detail activity");
				
				f.setAccessible(true);
				Object ad = f.get(param.thisObject);
				if(ad != null) {
					adm.invoke(ad, new Object[0]);
				}
			}
		});
	}
	
	void modBackgroundHDLayout(InitPackageResourcesParam resparam) {
		if(!resparam.packageName.equals("com.ogqcorp.bgh")) {
			return;
		}
		
		XposedBridge.log("Inside background hd");
		
		resparam.res.hookLayout("com.ogqcorp.bgh", "layout", "inc_ads", new XC_LayoutInflated() {
			
			@Override
			public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
				
				XposedBridge.log("Handle background ad layout");
				
				View ad = (View) liparam.view.findViewById(
						liparam.res.getIdentifier("ads", "id", "com.ogqcorp.bgh"));
				
				ad.setVisibility(View.GONE);
						
			}
		});
		
	}
	

}
