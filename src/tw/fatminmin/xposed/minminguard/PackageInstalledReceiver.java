package tw.fatminmin.xposed.minminguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class PackageInstalledReceiver extends BroadcastReceiver {
	public static SharedPreferences pref, uiPref;

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(final Context context, final Intent intent) {

		String action = intent.getAction();
		if (!Intent.ACTION_PACKAGE_ADDED.equals(action)) {
			return;
		}
		uiPref = context.getSharedPreferences("ui_preference",
				Context.MODE_PRIVATE);
		boolean autoEnableNewApps = uiPref.getBoolean("auto_enable_new_apps",
				false);
		if (!autoEnableNewApps) {
			return;
		}
		Uri uri = intent.getData();
		if (uri == null) {
			return;
		}
		String packageName = uri.getSchemeSpecificPart();
		pref = context.getSharedPreferences(context.getPackageName()
				+ "_preferences", Context.MODE_WORLD_READABLE);
		pref.edit().putBoolean(packageName, true).commit();
}
}
