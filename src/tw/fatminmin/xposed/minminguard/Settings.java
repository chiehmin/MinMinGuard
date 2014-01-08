package tw.fatminmin.xposed.minminguard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.google.analytics.tracking.android.EasyTracker;

public class Settings extends Activity {
	
	private Fragment fragment;
	
	private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
	
    private IInAppBillingService mService;
    static private SharedPreferences pref;

    private ServiceConnection mServiceConn = new ServiceConnection() {
       @Override
       public void onServiceDisconnected(ComponentName name) {
           mService = null;
       }

       @Override
       public void onServiceConnected(ComponentName name, 
          IBinder service) {
           mService = IInAppBillingService.Stub.asInterface(service);
       }
    };
    
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
				
				TextView tv = (TextView) view;
				if(tv.getText().equals("About")) {
					optionAbout();
				}
				else if(tv.getText().equals("Donate")) {
					optionDonate();
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
        
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        fragment = new PrefsFragment();
        
        if (savedInstanceState == null) {
			getFragmentManager().beginTransaction().replace(R.id.content_frame,
	                fragment).commit();
        }
        
        bindService(new Intent("com.android.vending.billing.InAppBillingService.BIND"),
                        mServiceConn, Context.BIND_AUTO_CREATE);
        
        
		BlockUrl.saveUrlsAsPreference(Settings.this);
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
		dlgAbout.show();
		dlgAbout.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
	}
	
	private void donateOk() {
		Dialog dlgAbout = new Dialog(this);
		dlgAbout.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dlgAbout.setTitle(getString(R.string.str_thank_you));
		dlgAbout.setContentView(R.layout.thank_you);
		dlgAbout.setCancelable(true);
		dlgAbout.show();
		dlgAbout.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
	}
	
	private void optionDonate() {
		final Dialog dlgDonate = new Dialog(this);
		dlgDonate.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dlgDonate.setTitle(getString(R.string.str_donation));
		dlgDonate.setContentView(R.layout.donate);
		dlgDonate.setCancelable(true);
		
		final Spinner spinner = (Spinner) dlgDonate.findViewById(R.id.spi_donate);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, 
				new String[]{"1", "5", "10", "100"});
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		Button btnOk = (Button) dlgDonate.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doDonate(spinner.getSelectedItemPosition());
				dlgDonate.dismiss();
			}
		});
		
		dlgDonate.show();
		dlgDonate.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
		
	}
	
	private void doDonate(int selection) {
		
		final String[] sku = {"minminguard_donate", "minminguard_donate_5", 
				"minminguard_donate_10", "minminguard_donate_100"};
		
		try {
			
			consumeDonate();
			
			Bundle buyIntentBundle;
			buyIntentBundle = mService.getBuyIntent(3, getPackageName(),
					sku[selection], "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");
			
			PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
			
			startIntentSenderForResult(pendingIntent.getIntentSender(),
					   1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0),
					   Integer.valueOf(0));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void consumeDonate() {
		try {
			Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
			int response = ownedItems.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList<String> purchaseDataList = ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
			   
				for (int i = 0; i < purchaseDataList.size(); ++i) {
					String purchaseData = purchaseDataList.get(i);
				    Log.i("fatminmin", purchaseData);
				    JSONObject jo = new JSONObject(purchaseData);
				    String token = jo.getString("purchaseToken");
				    Log.i("fatminmin", token);
				    
				    mService.consumePurchase(3, getPackageName(), token);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1001) {           
	      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");
	      
	      Log.v("fatminmin", "Response code:" + responseCode);
	      Log.v("fatminmin", "Purchase data:" + purchaseData);
	      Log.v("fatminmin", "Data signature:" + dataSignature);
	      
	      if (resultCode == RESULT_OK) {
	         try {
	            JSONObject jo = new JSONObject(purchaseData);
	            String sku = jo.getString("productId");
	            Log.v("fatminmin", "You have bought the " + sku + ". Excellent choice, adventurer!");
	            
	            donateOk();
	            
	          }
	          catch (JSONException e) {
	        	 Log.v("fatminmin", "Failed to parse purchase data.");
	             e.printStackTrace();
	          }
	      }
	   }
	}
	
	@Override
	protected void onStart() {
		EasyTracker.getInstance(this).activityStart(this);
		super.onStart();
	}
	
	@Override
	protected void onStop() {
		EasyTracker.getInstance(this).activityStop(this);
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		if (mServiceConn != null) {
	        unbindService(mServiceConn);
	    }   
		super.onDestroy();
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
					
					
					boolean admob, vpon, kuad, others;
					admob = pref.getBoolean(info.packageName + "admob", false);
					vpon = pref.getBoolean(info.packageName + "vpon", false);
					kuad = pref.getBoolean(info.packageName + "kuad", false);
					others = pref.getBoolean(info.packageName + "others", false);
					
					if(admob) {
						cp.setSummary("Admob detected.");
					}
					if(vpon) {
						cp.setSummary("Vpon detected.");
					}
					if(kuad) {
						cp.setSummary("KuAd detected.");
					}
					if(others) {
						cp.setSummary("others detected.");
					}
					
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