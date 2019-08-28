package tw.fatminmin.xposed.minminguard.ui.image;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

public class DrawableModelLoaderFactory implements ModelLoaderFactory<ApplicationInfo, Drawable> {

    private final Context mContext;

    DrawableModelLoaderFactory(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ModelLoader<ApplicationInfo, Drawable> build(@NonNull MultiModelLoaderFactory multiFactory) {
        return new DrawableModelLoader(mContext);
    }

    @Override
    public void teardown() {
        // Empty Implementation.
    }
}