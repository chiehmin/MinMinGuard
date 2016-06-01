package tw.fatminmin.xposed.minminguard.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.ui.adapter.ModeFragmentAdapter;
import tw.fatminmin.xposed.minminguard.ui.dialog.SettingsDialogFragment;
import tw.fatminmin.xposed.minminguard.ui.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ModeFragmentAdapter mAdapter;
    private TabLayout mTabLayout;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;

    private SharedPreferences mUiPref;
    private SharedPreferences mModPref;

    private final NavigationView.OnNavigationItemSelectedListener mNavListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            mDrawerLayout.closeDrawers();
            switch (menuItem.getItemId()) {
                case R.id.action_settings:
                    SettingsDialogFragment.newInstance().show(getSupportFragmentManager(), "dialog");
                    break;
                case R.id.action_tutorial:
                    showIntro();
                    break;
                case R.id.action_donate:
                    Intent it = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=47GXTVHUB4LYS"));
                    startActivity(it);
                    break;
                case R.id.action_about:
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://fatminmin.com/pages/minminguard.html")));
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private void showIntro() {
        Intent it = new Intent(this, MinMinGuardIntro.class);
        startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUiPref = getSharedPreferences(Common.UI_PREFS, MODE_PRIVATE);
        mModPref = getSharedPreferences(Common.MOD_PREFS, MODE_WORLD_READABLE);

        if (mUiPref.getBoolean(Common.KEY_FIRST_TIME, true)) {
            mUiPref.edit()
                    .putBoolean(Common.KEY_FIRST_TIME, false)
                    .commit();
            showIntro();
        }

        setContentView(R.layout.activity_main);


        // actionbar setup
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // drawerview and navigation view setup
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        mNavigationView.setNavigationItemSelectedListener(mNavListener);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name , R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();

        // setup tabview
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mAdapter = new ModeFragmentAdapter(getSupportFragmentManager(), MainActivity.this);
        mViewPager.setAdapter(mAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                MainFragment fragment = (MainFragment) mAdapter.getItem(position);
                if(fragment.isAlive) fragment.refreshUI();
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        switch (mModPref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST)) {
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
    }

    public MainFragment getCurrentFragment() {
        return (MainFragment) mAdapter.getItem(mViewPager.getCurrentItem());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
