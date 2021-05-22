package tw.fatminmin.xposed.minminguard.ui.image;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.module.AppGlideModule;

@com.bumptech.glide.annotation.GlideModule
public class GlideModule extends AppGlideModule {

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.prepend(ApplicationInfo.class, Drawable.class, new DrawableModelLoaderFactory(context));
    }

    public static void loadApplicationIcon(Context context, ApplicationInfo applicationInfo, ImageView view) {
        GlideApp.with(context)
                .load(applicationInfo)
                .into(view);
    }
}