package tw.fatminmin.xposed.minminguard.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.ui.adapter.AppsAdapter;

public class MainFragment extends Fragment {

    static public enum FragmentMode {
        AUTO,
        BLACKLIST,
        WHITELIST;
    };

    private FragmentMode mMode;
    private Context mContext;

    private TextView mTxtXposedEnabled;
    private Button mBtnMode;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AppsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PackageInfo> mAppList;
    private SharedPreferences mPref;


    private final View.OnClickListener btnModeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Boolean autoMode = !mPref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false);
            mBtnMode.setText(autoMode ? R.string.msg_mode_auto : R.string.msg_mode_manual);
            mPref.edit()
                    .putBoolean(Common.KEY_AUTO_MODE_ENABLED, autoMode)
                    .commit();
            mAdapter.notifyDataSetChanged();
        }
    };

    public static MainFragment newInstance(FragmentMode mode) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
        mPref = mContext.getSharedPreferences(Common.MOD_PREFS, Context.MODE_WORLD_READABLE);

        mMode = FragmentMode.values()[getArguments().getInt("mode")];
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.filterApp(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTxtXposedEnabled = (TextView) view.findViewById(R.id.txt_xposed_enable);
        mBtnMode = (Button) view.findViewById(R.id.btn_mode);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if (!Util.xposedEnabled()) {
            mTxtXposedEnabled.setVisibility(View.VISIBLE);
        }

        Boolean autoMode = mPref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false);
        mBtnMode.setText(autoMode ? R.string.msg_mode_auto : R.string.msg_mode_manual);
        mBtnMode.setOnClickListener(btnModeClick);


        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAppList = new ArrayList<>();
        mAdapter = new AppsAdapter(getActivity(), mAppList, mMode);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        refresh();
        return view;
    }

    public void refresh() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        updateAppList();
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void updateAppList() {
        final PackageManager pm = getActivity().getPackageManager();
        final SharedPreferences mUiPref = getActivity().getSharedPreferences(Common.UI_PREFS,
                Context.MODE_PRIVATE);

        mAppList.clear();

        boolean showSystemApps = mUiPref.getBoolean(Common.KEY_SHOW_SYSTEM_APPS, false);

        List<PackageInfo> list = pm.getInstalledPackages(0);
        for (PackageInfo info : list) {
            if (showSystemApps || (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                mAppList.add(info);
            }
        }

        Collections.sort(mAppList, new Comparator<PackageInfo>() {
            @Override
            public int compare(PackageInfo lhs, PackageInfo rhs) {
                String s1 = (String) lhs.applicationInfo.loadLabel(pm);
                String s2 = (String) rhs.applicationInfo.loadLabel(pm);
                return s1.compareToIgnoreCase(s2);
            }
        });
        mAdapter.setAppList(mAppList);
    }

}
