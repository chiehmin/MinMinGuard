package tw.fatminmin.xposed.minminguard.ui.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import tw.fatminmin.xposed.minminguard.ui.MainActivity;

public class SettingsDialogFragment extends DialogFragment
{

    private SharedPreferences mUiPref;
    private CheckBox mCbShowSystemApps;
    private CheckBox mCbShowDebugInfo;
    private CheckBox mCbShowLauncherIcon;

    public static SettingsDialogFragment newInstance()
    {
        return new SettingsDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        mUiPref = getActivity().getSharedPreferences(Common.UI_PREFS, Common.getPrefMode());

        getDialog().setTitle(R.string.action_settings);

        View v = inflater.inflate(R.layout.dialog_settings, container, false);

        mCbShowSystemApps = v.findViewById(R.id.cb_show_system_apps);
        mCbShowSystemApps.setChecked(mUiPref.getBoolean(Common.KEY_SHOW_SYSTEM_APPS, false));

        mCbShowDebugInfo = v.findViewById(R.id.cb_show_debug_info);
        mCbShowDebugInfo.setChecked(Util.DEBUG);

        Button mBtnOk = v.findViewById(R.id.btn_ok);

        mBtnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Util.DEBUG = mCbShowDebugInfo.isChecked();

                mUiPref.edit().putBoolean(Common.KEY_SHOW_SYSTEM_APPS, mCbShowSystemApps.isChecked()).apply();

                ((MainActivity) getActivity()).refresh(true);
                dismiss();
            }
        });

        return v;
    }
}
