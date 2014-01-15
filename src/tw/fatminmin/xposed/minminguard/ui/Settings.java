package tw.fatminmin.xposed.minminguard.ui;

import tw.fatminmin.xposed.minminguard.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Settings extends SherlockFragmentActivity {
	
	private ListFragment fragment;
	private Fragment logFragment;
	
	private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    
    static public SharedPreferences pref;
    
	@SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// this is important because although the handler classes that read these settings
        // are in the same package, they are executed in the context of the hooked package
		pref = getSharedPreferences(getPackageName() + "_preferences", MODE_WORLD_READABLE);
		
		
		LogFragment.mHandler = new Handler();
		setContentView(R.layout.activity_main);
		
		fragment = new PrefsFragment();
		logFragment = new LogFragment();
		
		
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
				    mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                        @Override
                        public void onDrawerClosed(View drawerView) {
                            super.onDrawerClosed(drawerView);
                            getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame,fragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
                        }
				    });
				    mDrawerLayout.closeDrawer(mDrawerList);
				    break;
				case 1:
				    
				    mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
				        @Override
				        public void onDrawerClosed(View drawerView) {
				            super.onDrawerClosed(drawerView);
				            getSupportFragmentManager().beginTransaction()
                                .replace(R.id.content_frame, logFragment)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .addToBackStack(null)
                                .commit();
				        }
                    });
				    mDrawerLayout.closeDrawer(mDrawerList);
				    break;
				case 2:
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
	public boolean onCreateOptionsMenu(Menu menu) {
	    getSupportMenuInflater().inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch(item.getItemId()) {
	    case android.R.id.home:
	        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
	        break;
	    case R.id.select_all:
	        
	        String key = "select_all";
	        boolean value = !pref.getBoolean(key, false);
	        pref.edit()
	            .putBoolean(key, value)
	            .commit();
	        
	        SelectAllAsyncTask.setup(Settings.this, new Handler());
	        new SelectAllAsyncTask(value).execute(new Object());
	        break;
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
}