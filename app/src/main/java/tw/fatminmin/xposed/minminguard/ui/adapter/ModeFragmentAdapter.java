package tw.fatminmin.xposed.minminguard.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tw.fatminmin.xposed.minminguard.ui.fragments.MainFragment;

/**
 * Created by fatminmin on 4/21/16.
 */
public final class ModeFragmentAdapter extends FragmentPagerAdapter {

    static final int PAGE_COUNT = 3;
    private String[] mTabTitles = new String[] { "AUTO", "Blacklist", "Whitelist" };
    private MainFragment[] mFragments = new MainFragment[] {
            MainFragment.newInstance(MainFragment.FragmentMode.AUTO),
            MainFragment.newInstance(MainFragment.FragmentMode.BLACKLIST),
            MainFragment.newInstance(MainFragment.FragmentMode.WHITELIST)};
    private Context mContext;


    public ModeFragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }
}
