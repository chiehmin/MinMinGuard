package tw.fatminmin.xposed.minminguard.ui.fragments;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import tw.fatminmin.xposed.minminguard.ui.MainActivity;
import tw.fatminmin.xposed.minminguard.ui.adapter.AppsAdapter;

public class MainFragment extends Fragment {

    static public enum FragmentMode {
        AUTO,
        BLACKLIST,
        WHITELIST;
    };

    public boolean isAlive = false; // changing to true after onCreateView is called.

    private FragmentMode mMode;

    private TextView mTxtXposedEnabled;
    private Button mBtnMode;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private AppsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PackageInfo> mAppList;
    private SharedPreferences mModPref;
    private SharedPreferences mUiPref;

    private MainActivity mActivity;
    private Handler mHandler = new Handler();

    public MainFragment() {
    }

    private final View.OnClickListener btnModeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mModPref.edit()
                    .putString(Common.KEY_MODE, Common.getModeString(mMode))
                    .commit();
            mBtnMode.setVisibility(View.GONE);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();

        // waiting for the UI to load first
       refreshPost();
    }

    public void refreshUI() {
        if(mModPref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST).equals(Common.getModeString(mMode))) {
            mBtnMode.setVisibility(View.GONE);
        } else {
            mBtnMode.setVisibility(View.VISIBLE);
        }
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

        mActivity = (MainActivity) getActivity();
        mModPref = mActivity.modPref;
        mUiPref = mActivity.uiPref;
        mMode = FragmentMode.values()[getArguments().getInt("mode")];

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTxtXposedEnabled = (TextView) view.findViewById(R.id.txt_xposed_enable);
        mBtnMode = (Button) view.findViewById(R.id.btn_mode);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        if (!Util.xposedEnabled()) {
            mTxtXposedEnabled.setVisibility(View.VISIBLE);
        }

        /* setup apply button */
        mBtnMode.setOnClickListener(btnModeClick);
        if(mModPref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST).equals(Common.getModeString(mMode))) {
            mBtnMode.setVisibility(View.GONE);
        }


        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAppList = new ArrayList<>();
        mAdapter = new AppsAdapter(getActivity(), mAppList, mMode);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPost();
            }
        });

        isAlive = true;
        return view;
    }


    public void refreshPost() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                refresh();
            }
        });
    }
    private void refresh() {
        new AsyncTask<Void, Void, Void>() {

            PackageManager pm;
            List<PackageInfo> list;

            @Override
            protected void onPreExecute() {
                mSwipeRefreshLayout.setRefreshing(true);
                pm = getActivity().getPackageManager();
                list = pm.getInstalledPackages(0);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                updateAppList(pm, list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }.execute();
    }

    private void updateAppList(final PackageManager pm, final List<PackageInfo> list) {

        mAppList.clear();

        boolean showSystemApps = mUiPref.getBoolean(Common.KEY_SHOW_SYSTEM_APPS, false);

        for (PackageInfo info : list) {
            if (showSystemApps || (info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                mAppList.add(info);
            }
            // setting initial value for system apps
            if((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1 && !mModPref.contains(info.packageName)) {
                mModPref.edit()
                        .putBoolean(info.packageName, false)
                        .putBoolean(Common.getWhiteListKey(info.packageName), true)
                        .commit();
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
