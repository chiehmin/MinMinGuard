package tw.fatminmin.xposed.minminguard.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.orm.AppData;
import tw.fatminmin.xposed.minminguard.orm.AppDataDao;
import tw.fatminmin.xposed.minminguard.orm.DaoMaster;
import tw.fatminmin.xposed.minminguard.orm.DaoSession;
import tw.fatminmin.xposed.minminguard.ui.UIUtils;
import tw.fatminmin.xposed.minminguard.ui.dialog.AppDetailDialogFragment;
import tw.fatminmin.xposed.minminguard.ui.fragments.MainFragment;
import tw.fatminmin.xposed.minminguard.ui.models.AppDetails;

/**
 * Created by fatminmin on 2015/10/1.
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private final Context mContext;
    private final SharedPreferences mPref;

    private ArrayList<AppDetails> appList;

    private Map<String, AppData> mAppDataMap; // retrieved AppData from sqlite
    private MainFragment.FragmentMode mMode;

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

    public AppsAdapter(Context context, ArrayList<AppDetails> list, MainFragment.FragmentMode mode) {
        super();
        mContext = context;
        appList = list;
        mPref = mContext.getSharedPreferences(Common.MOD_PREFS, Context.MODE_WORLD_READABLE);

        helper = new DaoMaster.DevOpenHelper(context, "mmg", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        appDataDao = daoSession.getAppDataDao();

        mMode = mode;

        mAppDataMap = new HashMap<>();
    }

    public void setAppList(ArrayList<AppDetails> list) {
        appList = list;

        /* update appdata map */
        mAppDataMap.clear();
        for (AppDetails info : appList) {
            final String pkgName = info.getPackageName();
            AppData appData = null;

            List<AppData> results = appDataDao.queryBuilder()
                    .where(AppDataDao.Properties.PkgName.eq(pkgName))
                    .list();

            if (results.size() == 1) {
                appData = results.get(0);
                appDataDao.refresh(appData);
            }

            mAppDataMap.put(pkgName, appData);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_app, parent, false);
        return new ViewHolder(v);
    }

    private void setState(String pkgName, boolean checked) {
        /* auto mode */
        if (mMode == MainFragment.FragmentMode.AUTO) return;

        if (mMode == MainFragment.FragmentMode.BLACKLIST) {
            /* blacklist mode */
            mPref.edit().putBoolean(pkgName, checked).apply();
        } else {
            /* whitelist mode */
            mPref.edit().putBoolean(Common.getWhiteListKey(pkgName), checked).apply();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final AppDetails currentAppDetails = appList.get(position);

        final AppData appData = mAppDataMap.get(currentAppDetails.getPackageName());
        if (appData != null && appData.getBlockNum() != 0)
            holder.txtBlockNum.setText(mContext.getString(R.string.msg_block_num, appData.getBlockNum()));
        else
            holder.txtBlockNum.setText("");

        holder.imgAppIcon.setImageDrawable(currentAppDetails.getIcon());
        holder.txtAppName.setText(currentAppDetails.getName());

        if (mMode == MainFragment.FragmentMode.AUTO)
            holder.switchEnable.setVisibility(View.GONE);
        else
            holder.switchEnable.setVisibility(View.VISIBLE);

        holder.switchEnable.setChecked(currentAppDetails.isEnabled());
        holder.switchEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch sw = (Switch) v;
                setState(currentAppDetails.getPackageName(), sw.isChecked());
            }
        });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialog = AppDetailDialogFragment.newInstance(currentAppDetails.getName(), currentAppDetails.getPackageName(), appData);
                AppCompatActivity activity = (AppCompatActivity) mContext;
                dialog.show(activity.getSupportFragmentManager(), "dialog");
            }
        });

        holder.imgAppIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.restartApp(mContext, currentAppDetails.getPackageName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }
}
