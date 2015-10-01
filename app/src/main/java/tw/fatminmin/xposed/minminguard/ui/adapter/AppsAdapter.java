package tw.fatminmin.xposed.minminguard.ui.adapter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import tw.fatminmin.xposed.minminguard.R;

/**
 * Created by fatminmin on 2015/10/1.
 */
public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private Context mContext;
    private List<PackageInfo> mAppList;

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
        mAppList = list;
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
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = mAppList.get(position);

        Drawable appIcon = info.applicationInfo.loadIcon(pm);
        String appName = (String) info.applicationInfo.loadLabel(pm);

        holder.imgAppIcon.setImageDrawable(appIcon);
        holder.txtAppName.setText(appName);
    }

    @Override
    public int getItemCount() {
        return mAppList.size();
    }


}
