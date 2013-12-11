package tw.fatminmin.xposed;

import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

abstract public class ModApp  {
	
	protected static String pkg = "";
	
	public void modMethod(LoadPackageParam lpparam) {}
	public void modLayout(InitPackageResourcesParam resparam) {}
	
}
