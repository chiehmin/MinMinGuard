package tw.fatminmin.xposed.minminguard.ui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.ui.MainActivity;
import tw.fatminmin.xposed.minminguard.ui.adapter.AppsAdapter;
import tw.fatminmin.xposed.minminguard.ui.models.AppDetails;

import java.util.ArrayList;

public class MainFragment extends Fragment
{

    public boolean isAlive = false;
    private MainActivity mainActivity;
    private FragmentMode mMode;
    private Button mBtnMode;
    private AppsAdapter adapter;
    private SharedPreferences mModPref;
    private final View.OnClickListener btnModeClick = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            mModPref.edit().putString(Common.KEY_MODE, Common.getModeString(mMode)).apply();
            mBtnMode.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    };

    public static MainFragment newInstance(FragmentMode mode)
    {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt("mode", mode.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void refreshUI()
    {
        if (mModPref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST).equals(Common.getModeString(mMode)))
        {
            mBtnMode.setVisibility(View.GONE);
        }
        else
        {
            mBtnMode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        mainActivity = (MainActivity) getActivity();
        mModPref = mainActivity.modPref;
        mMode = FragmentMode.values()[getArguments().getInt("mode")];

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        TextView mTxtXposedEnabled = view.findViewById(R.id.txt_xposed_enable);
        mBtnMode = view.findViewById(R.id.btn_mode);

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);

        if (!Util.xposedEnabled())
        {
            mTxtXposedEnabled.setVisibility(View.VISIBLE);
        }

        /* setup apply button */
        mBtnMode.setOnClickListener(btnModeClick);
        if (mModPref.getString(Common.KEY_MODE, Common.VALUE_MODE_BLACKLIST).equals(Common.getModeString(mMode)))
        {
            mBtnMode.setVisibility(View.GONE);
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        adapter = new AppsAdapter(getActivity(), mainActivity.masterAppList, mMode);
        mRecyclerView.setAdapter(adapter);

        isAlive = true;
        return view;
    }

    public FragmentMode getMode()
    {
        return mMode;
    }

    public void updateAndNotify(ArrayList<AppDetails> list)
    {
        adapter.setAppList(list);
        adapter.notifyDataSetChanged();
    }

    public enum FragmentMode
    {
        AUTO, BLACKLIST, WHITELIST
    }
}
