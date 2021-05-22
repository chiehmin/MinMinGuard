package tw.fatminmin.xposed.minminguard.ui.image;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;

public class DrawableDataFetcher implements DataFetcher<Drawable> {

    private final ApplicationInfo mModel;
    private final Context mContext;


    DrawableDataFetcher(Context context, ApplicationInfo model) {
        mModel = model;
        mContext = context;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super Drawable> callback) {
        final Drawable icon = mContext.getPackageManager().getApplicationIcon(mModel);

        callback.onDataReady(icon);
    }

    @Override
    public void cleanup() {
        // Empty Implementation
    }

    @Override
    public void cancel() {
        // Empty Implementation
    }

    @NonNull
    @Override
    public Class<Drawable> getDataClass() {
        return Drawable.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}