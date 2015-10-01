package tw.fatminmin.xposed.minminguard.ui.fragments;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.ui.adapter.AppsAdapter;

public class MainFragment extends Fragment {

    private Context mContext;

    private TextView mTxtXposedEnabled;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PackageInfo> mAppList;


    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        mTxtXposedEnabled = (TextView) view.findViewById(R.id.txt_xposed_enable);
        if (!Util.xposedEnabled()) {
            mTxtXposedEnabled.setVisibility(View.VISIBLE);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAppList = new ArrayList<>();
        mAdapter = new AppsAdapter(getActivity(), mAppList);
        mRecyclerView.setAdapter(mAdapter);

        refresh();
        return view;
    }

    private void refresh() {
        updateAppList();
        mAdapter.notifyDataSetChanged();
    }

    private void updateAppList() {
        final PackageManager pm = getActivity().getPackageManager();
        mAppList.clear();

        List<PackageInfo> list = pm.getInstalledPackages(0);
        for (PackageInfo info : list) {
            if ((info.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
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
    }

}
