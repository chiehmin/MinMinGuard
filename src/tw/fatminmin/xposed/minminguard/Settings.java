package tw.fatminmin.xposed.minminguard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Settings extends Activity {
	
	private Fragment fragment;
	
	private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);        
		
        
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setAdapter(new ArrayAdapter<String>(Settings.this,
				android.R.layout.simple_list_item_1, 
				getResources().getStringArray(R.array.drawer_list)));
		
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				switch(position) {
				case 0:
					optionAbout();
					break;
				}
			}
		});
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
		
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // Display the fragment as the main content.
        
        fragment = new PrefsFragment();
        
        if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().replace(R.id.content_frame,
	                fragment).commit();
        }
        
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		
		mDrawerToggle.syncState();
		
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	private void optionAbout() {
		
		Dialog dlgAbout = new Dialog(this);
		dlgAbout.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dlgAbout.setTitle(getString(R.string.menu_about));
		dlgAbout.setContentView(R.layout.about);
		dlgAbout.setCancelable(true);
		dlgAbout.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
		
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			TextView tv = (TextView) dlgAbout.findViewById(R.id.tvVersion);
			tv.setText(String.format(getString(R.string.app_version), pInfo.versionName));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		dlgAbout.show();
		
	}
	
	public static class PrefsFragment extends PreferenceFragment {
		
		
		private PreferenceCategory pc;
		
		@SuppressWarnings("deprecation")
        @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// this is important because although the handler classes that read these settings
			// are in the same package, they are executed in the context of the hooked package
			getPreferenceManager().setSharedPreferencesMode(MODE_WORLD_READABLE);
			addPreferencesFromResource(R.xml.preferences);
			
			
			pc = (PreferenceCategory) findPreference("block_ad_cat");
			
			setupAppList();
			
			
			CheckBoxPreference selectAll = (CheckBoxPreference) findPreference("select_all");
			selectAll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					setChecked((Boolean) newValue);
					return (Boolean) newValue;
				}
				
			});
		}
		
		private void setupAppList() {
			
			Context activity = getActivity();
			
			PackageManager pm = activity.getPackageManager();
			List<ApplicationInfo> list = pm.getInstalledApplications(0);
			
			List<Preference> prefList = new ArrayList<Preference>();
			for(ApplicationInfo info : list) {
				
				if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					CheckBoxPreference cp = new CheckBoxPreference(activity);
					cp.setTitle(pm.getApplicationLabel(info));
					cp.setKey(info.packageName);
					cp.setIcon(pm.getApplicationIcon(info));

					prefList.add(cp);
				}
			}
			
			Collections.sort(prefList, new Comparator<Preference>() {
				@Override
				public int compare(Preference lhs, Preference rhs) {
					return lhs.getTitle().toString().compareTo(rhs.getTitle().toString());
				}
				
			});
			
			for(Preference pref : prefList) {
				pc.addPreference(pref);
			}
		}
		
		private void setChecked(boolean value) {
			for(int i = 0; i < pc.getPreferenceCount(); i++) {
				Preference pref = pc.getPreference(i);
				if(pref instanceof CheckBoxPreference) {
					CheckBoxPreference check = (CheckBoxPreference) pref;
					check.setChecked(value);
				}
			}
		}
	}
}