package tw.fatminmin.xposed.minminguard.ui.dialog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.orm.AppData;

/**
 * Created by fatminmin on 2015/10/25.
 */
public class AppDetailDialogFragment extends DialogFragment
{

    private ImageView imgAppIcon;
    private TextView txtAppName;
    private TextView txtPkgName;
    private TextView txtAdNetworks;
    private TextView txtAdsBlocked;
    private Switch swtUrlFilter;

    private String appName;
    private String pkgName;
    private String adNetworks;
    private Integer blockNum;

    private SharedPreferences mPref;

    public static AppDetailDialogFragment newInstance(String appName, String pkgName, AppData appData)
    {
        Bundle args = new Bundle();
        args.putString("appName", appName);
        args.putString("pkgName", pkgName);
        if (appData != null)
        {
            args.putString("adNetworks", appData.getAdNetworks());
            args.putInt("blockNum", appData.getBlockNum());
        }

        AppDetailDialogFragment fragment = new AppDetailDialogFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        appName = args.getString("appName");
        pkgName = args.getString("pkgName");
        adNetworks = args.getString("adNetworks");
        blockNum = args.getInt("blockNum");

        Context ctx = ContextCompat.createDeviceProtectedStorageContext(getActivity());
        if (ctx == null) ctx = getActivity();

        mPref = ctx.getSharedPreferences(Common.MOD_PREFS, Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View v = inflater.inflate(R.layout.dialog_app_detail, container, false);
        imgAppIcon = v.findViewById(R.id.detail_app_icon);
        txtAppName = v.findViewById(R.id.detail_app_name);
        txtPkgName = v.findViewById(R.id.detail_pkg_name);
        txtAdNetworks = v.findViewById(R.id.txt_ad_networks);
        txtAdsBlocked = v.findViewById(R.id.txt_ads_blocked);
        swtUrlFilter = v.findViewById(R.id.switch_url_filter);

        txtAppName.setText(appName);
        txtPkgName.setText(pkgName);
        if (adNetworks != null && adNetworks.length() > 0)
        {
            txtAdNetworks.setText(adNetworks);
        }
        if (blockNum != null)
        {
            txtAdsBlocked.setText(blockNum.toString());
        }

        /* set appicon */
        final PackageManager pm = v.getContext().getPackageManager();
        try
        {
            Drawable appIcon = pm.getApplicationIcon(pkgName);
            if (appIcon != null)
            {
                imgAppIcon.setImageDrawable(appIcon);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        swtUrlFilter.setChecked(mPref.getBoolean(pkgName + "_url", false));
        swtUrlFilter.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPref.edit().putBoolean(pkgName + "_url", swtUrlFilter.isChecked()).commit();
            }
        });

        return v;
    }
}
