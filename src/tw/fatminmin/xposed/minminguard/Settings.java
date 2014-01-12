package tw.fatminmin.xposed.minminguard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

public class Settings extends SherlockFragmentActivity {
	
	private Fragment fragment;
	
	private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
    @SuppressWarnings("unused")
    private SharedPreferences pref;
    
	@SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// this is important because although the handler classes that read these settings
        // are in the same package, they are executed in the context of the hooked package
		pref = getSharedPreferences(getPackageName() + "_preferences", MODE_WORLD_READABLE);
		
		setContentView(R.layout.activity_main);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);        
		
        
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
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
		
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // Display the fragment as the main content.
        
        fragment = new PrefsFragment();
        
        if (savedInstanceState == null) {
            
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
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
		
	    if (item.getItemId() == android.R.id.home) {

	        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	            mDrawerLayout.closeDrawer(mDrawerList);
	        } else {
	            mDrawerLayout.openDrawer(mDrawerList);
	        }
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
	
	public static class PrefsFragment extends Fragment {
		
	    private ListView listView;
	    private List<Map<String, Object>> itemList;
	    
        @Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			
			setupAppList();
			
//			CheckBoxPreference selectAll = (CheckBoxPreference) findPreference("select_all");
//			selectAll.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
//				@Override
//				public boolean onPreferenceChange(Preference preference, Object newValue) {
//					setChecked((Boolean) newValue);
//					return (Boolean) newValue;
//				}
//				
//			});
		}
        
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            
            View root = inflater.inflate(R.layout.fragment_main, container);
            
            listView = (ListView) root.findViewById(R.id.listview);
            
            setupAppList();
            listView.setAdapter(new CheckBoxAdapter(getActivity(), itemList));
            
            return super.onCreateView(inflater, container, savedInstanceState);
        }
		
		private void setupAppList() {
			
			Context activity = getActivity();
			
			PackageManager pm = activity.getPackageManager();
			List<ApplicationInfo> list = pm.getInstalledApplications(0);
			
			itemList = new ArrayList<Map<String, Object>>();
			for(ApplicationInfo info : list) {
				
				if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					
				    Map<String, Object> map = new HashMap<String, Object>();
				    
				    map.put("title", pm.getApplicationLabel(info));
				    map.put("key", info.packageName);
				    map.put("icon", pm.getApplicationIcon(info));
				    
					itemList.add(map);
				}
			}
			
			Collections.sort(itemList, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
                    String s1 = (String) lhs.get("title");
                    String s2 = (String) rhs.get("title");
                    return s1.compareTo(s2);
                }
			});
		}
		
//		private void setChecked(boolean value) {
//			for(int i = 0; i < pc.getPreferenceCount(); i++) {
//				Preference pref = pc.getPreference(i);
//				if(pref instanceof CheckBoxPreference) {
//					CheckBoxPreference check = (CheckBoxPreference) pref;
//					check.setChecked(value);
//				}
//			}
//		}
	}
}