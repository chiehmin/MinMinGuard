package tw.fatminmin.xposed.minminguard.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.ui.adapter.ModeFragmentAdapter;
import tw.fatminmin.xposed.minminguard.ui.dialog.SettingsDialogFragment;
import tw.fatminmin.xposed.minminguard.ui.fragments.MainFragment;
import tw.fatminmin.xposed.minminguard.ui.models.AppDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity
{

    public volatile ArrayList<AppDetails> masterAppList;

    public ArrayList<AppDetails> filteredAppList;
    public SharedPreferences uiPref;
    public SharedPreferences modPref;
    private ViewPager mViewPager;
    private ModeFragmentAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private final NavigationView.OnNavigationItemSelectedListener mNavListener = new NavigationView.OnNavigationItemSelectedListener()
    {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem)
        {
            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId())
            {
                case R.id.action_settings:
                    SettingsDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
                    break;
                case R.id.action_tutorial:
                    showIntro();
                    break;
                case R.id.action_donate:
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=47GXTVHUB4LYS"));
                    startActivity(it);
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://fatminmin.com/pages/minminguard.html")));
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    private ActionBarDrawerToggle mToggle;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        uiPref = getSharedPreferences(Common.UI_PREFS, MODE_PRIVATE);
        modPref = getSharedPreferences(Common.MOD_PREFS, MODE_PRIVATE);

        progressDialog = new ProgressDialog(this, R.style.MainSpinnerDialogStyle);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getString(R.string.msg_wait));
        progressDialog.setCancelable(false);

        if (uiPref.getBoolean(Common.KEY_FIRST_TIME, true))
        {
            uiPref.edit().putBoolean(Common.KEY_FIRST_TIME, false).apply();
            showIntro();
        }

        setContentView(R.layout.activity_main);

        // actionbar setup
        if (getSupportActionBar() != null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        // drawer view and navigation view setup
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(mNavListener);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        // setup tab view
        mAdapter = new ModeFragmentAdapter(getSupportFragmentManager(), MainActivity.this);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                MainFragment fragment = (MainFragment) mAdapter.getItem(position);
                if (fragment.isAlive)
                {
                    fragment.refreshUI();
                }
                refresh(true);
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
                if (state == 1)
                {
                    swipeRefreshLayout.setEnabled(false);
                }
                else if (state == 0)
                {
                    swipeRefreshLayout.setEnabled(true);
                }
            }
        });

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        switch (modPref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST))
        {
            case Common.VALUE_MODE_AUTO:
                mViewPager.setCurrentItem(0);
                break;
            case Common.VALUE_MODE_BLACKLIST:
                mViewPager.setCurrentItem(1);
                break;
            case Common.VALUE_MODE_WHITELIST:
                mViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }

        masterAppList = new ArrayList<>();
        filteredAppList = new ArrayList<>();

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refresh(false);
            }
        });

        refresh(true);
    }

    @Override
    public void onResume()
    {
        refresh(false);
        super.onResume();
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        else if (mDrawerLayout.isDrawerOpen(GravityCompat.END))
        {
            mDrawerLayout.closeDrawer(GravityCompat.END);
            return;
        }

        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                filterAppList(query);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return mToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    public MainFragment getCurrentFragment()
    {
        return (MainFragment) mAdapter.getItem(mViewPager.getCurrentItem());
    }

    private void showIntro()
    {
        Intent intent = new Intent(this, MinMinGuardIntro.class);
        startActivity(intent);
    }

    public void refresh(final boolean displayProgressDialog)
    {
        new AsyncTask<Void, Void, Void>()
        {

            @Override
            protected void onPreExecute()
            {
                if (displayProgressDialog)
                {
                    progressDialog.show();
                }
            }

            @Override
            protected Void doInBackground(Void... voids)
            {
                updateAppList(getPackageManager());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                getCurrentFragment().updateAndNotify(masterAppList);
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    private void updateAppList(final PackageManager pm)
    {

        boolean showSystemApps = uiPref.getBoolean(Common.KEY_SHOW_SYSTEM_APPS, false);
        ArrayList<AppDetails> temp = new ArrayList<>();

        for (PackageInfo info : pm.getInstalledPackages(0))
        {
            if (showSystemApps || (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                String name = (String) info.applicationInfo.loadLabel(pm);
                String packageName = info.packageName;
                Drawable icon = info.applicationInfo.loadIcon(pm);
                boolean isEnabled = isPackageEnabled(packageName);

                temp.add(new AppDetails(name, packageName, icon, isEnabled));
            }

            // setting initial value for system apps
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1 && !modPref.contains(info.packageName))
            {
                modPref.edit().putBoolean(info.packageName, false).putBoolean(Common.getWhiteListKey(info.packageName), true).apply();
            }
        }

        Collections.sort(temp, new Comparator<AppDetails>()
        {
            @Override
            public int compare(AppDetails l, AppDetails r)
            {
                return l.getName().compareToIgnoreCase(r.getName());
            }
        });

        masterAppList = temp;
    }

    private boolean isPackageEnabled(String pkgName)
    {
        if (getCurrentFragment().getMode() == MainFragment.FragmentMode.AUTO)
        {
            return true;
        }

        if (getCurrentFragment().getMode() == MainFragment.FragmentMode.BLACKLIST)
        {
            return modPref.getBoolean(pkgName, false);
        }

        return modPref.getBoolean(Common.getWhiteListKey(pkgName), false);
    }

    public void filterAppList(String keyword)
    {
        filteredAppList.clear();

        for (AppDetails info : masterAppList)
        {
            if (info.getName().toLowerCase().contains(keyword.toLowerCase()))
            {
                filteredAppList.add(info);
            }
        }

        getCurrentFragment().updateAndNotify(filteredAppList);
    }
}
