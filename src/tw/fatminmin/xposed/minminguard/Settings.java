package tw.fatminmin.xposed.minminguard;

import java.util.List;

import tw.fatminmin.xposed.minminguard.R;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;

public class Settings extends PreferenceActivity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle(R.string.app_name);
		
//		pref.reload();
		
        // Display the fragment as the main content.
        if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().replace(android.R.id.content,
	                new PrefsFragment()).commit();
        }
	}
	
	
	
	
	public static class PrefsFragment extends PreferenceFragment {
		@SuppressWarnings("deprecation")
        @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// this is important because although the handler classes that read these settings
			// are in the same package, they are executed in the context of the hooked package
			getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
			addPreferencesFromResource(R.xml.preferences);
			
			setupAppList();
		}
		
		
		private PreferenceCategory pc;
		private void setupAppList() {
			
			pc = (PreferenceCategory) findPreference("block_ad_cat");
			
			
			Context activity = getActivity();
			
			PackageManager pm = activity.getPackageManager();
			List<ApplicationInfo> list = pm.getInstalledApplications(0);
			
			
			for(ApplicationInfo info : list) {
				
				if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					CheckBoxPreference cp = new CheckBoxPreference(activity);
					cp.setTitle(pm.getApplicationLabel(info));
					cp.setKey(info.packageName);
					cp.setIcon(pm.getApplicationIcon(info));
					
					pc.addPreference(cp);
				}
			}
			
		}
	}
}
