package tw.fatminmin.xposed.minminguard.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import tw.fatminmin.xposed.minminguard.orm.AppData;
import tw.fatminmin.xposed.minminguard.orm.AppDataDao;
import tw.fatminmin.xposed.minminguard.orm.DaoMaster;
import tw.fatminmin.xposed.minminguard.orm.DaoSession;
import tw.fatminmin.xposed.minminguard.ui.UIUtils;
import tw.fatminmin.xposed.minminguard.ui.dialog.AppDetailDialogFragment;

/**
 * Created by fatminmin on 2015/10/1.
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private final Context mContext;
    private List<PackageInfo> mAppList;
    private List<PackageInfo> mFilteredList;
    private final SharedPreferences mPref;

    private final DaoMaster.DevOpenHelper helper;
    private final SQLiteDatabase db;
    private final DaoMaster daoMaster;
    private final DaoSession daoSession;
    private final AppDataDao appDataDao;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View card;
        public ImageView imgAppIcon;
        public TextView txtAppName;
        public TextView txtBlockNum;
        public Switch switchEnable;

        public ViewHolder(View v) {
            super(v);
            card = v;
            imgAppIcon = (ImageView) v.findViewById(R.id.img_app_icon);
            txtAppName = (TextView) v.findViewById(R.id.txt_app_name);
            txtBlockNum = (TextView) v.findViewById(R.id.txt_block_num);
            switchEnable = (Switch) v.findViewById(R.id.switch_enable);
        }
    }

    public AppsAdapter(Context context, List<PackageInfo> list) {
        super();
        mContext = context;
        mFilteredList = mAppList = list;
        mPref = mContext.getSharedPreferences(Common.MOD_PREFS, Context.MODE_WORLD_READABLE);

        helper = new DaoMaster.DevOpenHelper(context, "mmg", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        appDataDao = daoSession.getAppDataDao();
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
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PackageManager pm = mContext.getPackageManager();
        final PackageInfo info = mFilteredList.get(position);

        final Drawable appIcon = info.applicationInfo.loadIcon(pm);
        final String appName = (String) info.applicationInfo.loadLabel(pm);
        final String pkgName = info.packageName;

        List<AppData> list = appDataDao.queryBuilder()
                               .where(AppDataDao.Properties.PkgName.eq(pkgName))
                               .list();

        final AppData appData;
        if (list.size() == 1) {
            appData = list.get(0);
            appDataDao.refresh(appData);
            int blockNum = appData.getBlockNum();
            String msg = mContext.getString(R.string.msg_block_num, blockNum);
            holder.txtBlockNum.setText(msg);
        }
        else {
            appData = null;
            holder.txtBlockNum.setText("");
        }

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


        holder.card.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DialogFragment dialog = AppDetailDialogFragment.newInstance(appName, pkgName, appData);
                AppCompatActivity activity = (AppCompatActivity) mContext;
                dialog.show(activity.getSupportFragmentManager(), "dialog");
            }
        });

        holder.imgAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.restartApp(mContext, pkgName);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }


}
