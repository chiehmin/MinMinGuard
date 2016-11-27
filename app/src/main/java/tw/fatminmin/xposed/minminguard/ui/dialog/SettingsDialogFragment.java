package tw.fatminmin.xposed.minminguard.ui.dialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.ui.MainActivity;

public class SettingsDialogFragment extends DialogFragment{

    private SharedPreferences mUiPref;
    private CheckBox mCbShowSystemApps;
    private CheckBox mCbShowLauncherIcon;

    public static SettingsDialogFragment newInstance() {
        return new SettingsDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mUiPref = getActivity().getSharedPreferences(Common.UI_PREFS, Context.MODE_PRIVATE);

        getDialog().setTitle(R.string.action_settings);

        View v = inflater.inflate(R.layout.dialog_settings, container, false);

        mCbShowSystemApps = (CheckBox) v.findViewById(R.id.cb_show_system_apps);
        mCbShowSystemApps.setChecked(mUiPref.getBoolean(Common.KEY_SHOW_SYSTEM_APPS, false));

        mCbShowLauncherIcon = (CheckBox) v.findViewById(R.id.cb_enable_launcher_icon);
        mCbShowLauncherIcon.setChecked(mUiPref.getBoolean(Common.KEY_SHOW_LAUNCHER_ICON, true));

        Button mBtnOk = (Button) v.findViewById(R.id.btn_ok);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUiPref.edit()
                        .putBoolean(Common.KEY_SHOW_SYSTEM_APPS, mCbShowSystemApps.isChecked())
                        .apply();

                mUiPref.edit()
                        .putBoolean(Common.KEY_SHOW_LAUNCHER_ICON, mCbShowLauncherIcon.isChecked())
                        .apply();

                ComponentName alias = new ComponentName(getContext(), Common.PACKAGE_NAME + ".ui.MainActivity-Alias");
                if(mCbShowLauncherIcon.isChecked()) {
                    getContext().getApplicationContext().getPackageManager().setComponentEnabledSetting(alias,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                } else {
                    getContext().getApplicationContext().getPackageManager().setComponentEnabledSetting(alias,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                }

                ((MainActivity) getActivity()).refresh(true);
                dismiss();
            }
        });

        return v;
    }
}
