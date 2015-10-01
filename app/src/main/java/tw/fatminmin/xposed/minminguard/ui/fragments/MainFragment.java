package tw.fatminmin.xposed.minminguard.ui.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.blocker.Util;

public class MainFragment extends Fragment {

    private TextView txtXposedEnabled;

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
//        MainFragmentMainFragmentBundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        txtXposedEnabled = (TextView) view.findViewById(R.id.txt_xposed_enable);
        if (Util.xposedEnabled()) {
            txtXposedEnabled.setText("MinMinGuard is working and enabled");
        }
        else {
            txtXposedEnabled.setText("Xposed framework is not working.");
        }

        return view;
    }

}
