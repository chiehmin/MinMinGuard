package tw.fatminmin.xposed.minminguard.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;

/**
 * Created by fatminmin on 2015/10/1.
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private Context mContext;
    private List<PackageInfo> mAppList;
    private List<PackageInfo> mFilteredList;
    private SharedPreferences mPref;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgAppIcon;
        public TextView txtAppName;
        public Switch switchEnable;

        public ViewHolder(View v) {
            super(v);
            imgAppIcon = (ImageView) v.findViewById(R.id.img_app_icon);
            txtAppName = (TextView) v.findViewById(R.id.txt_app_name);
            switchEnable = (Switch) v.findViewById(R.id.switch_enable);
        }
    }

    public AppsAdapter(Context context, List<PackageInfo> list) {
        super();
        mContext = context;
        mFilteredList = mAppList = list;
        mPref = PreferenceManager.getDefaultSharedPreferences(mContext);
    }

    public void setAppList(List<PackageInfo> list) {
        mFilteredList = mAppList = list;
    }

    public void filterApp(String keyword) {
        PackageManager pm = mContext.getPackageManager();
        mFilteredList = new ArrayList<>();
        for (PackageInfo info : mAppList) {
            String appName = (String) info.applicationInfo.loadLabel(pm);
            // use toLowerCase to ignore case
            if (appName.toLowerCase().contains(keyword.toLowerCase())) {
                mFilteredList.add(info);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.card_app, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PackageManager pm = mContext.getPackageManager();
        final PackageInfo info = mFilteredList.get(position);

        final Drawable appIcon = info.applicationInfo.loadIcon(pm);
        final String appName = (String) info.applicationInfo.loadLabel(pm);
        final String pkgName = info.packageName;

        holder.imgAppIcon.setImageDrawable(appIcon);
        holder.txtAppName.setText(appName);

        if (mPref.getBoolean(Common.KEY_AUTO_MODE_ENABLED, false)) {
            holder.switchEnable.setVisibility(View.GONE);
        } else {
            holder.switchEnable.setVisibility(View.VISIBLE);
        }

        holder.switchEnable.setChecked(mPref.getBoolean(pkgName, false));
        holder.switchEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch sw = (Switch) v;
                mPref.edit()
                        .putBoolean(pkgName, sw.isChecked())
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


}
