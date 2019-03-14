package tw.fatminmin.xposed.minminguard.blocker;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import tw.fatminmin.xposed.minminguard.Main;

public class ViewBlocking
{
    public static void removeAdView(String packageName, View view)
    {
        Util.notifyRemoveAdView(view.getContext(), packageName, 1);
        removeAdView(packageName, view, true, 51);
    }

    public static void removeAdView(final String packageName, final View view, final boolean first, final float heightLimit)
    {
        float adHeight = convertPixelsToDp(view.getHeight());

        if (first || (adHeight > 0 && adHeight <= heightLimit))
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();

            if (params == null)
                params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            else
                params.height = 0;

            view.setLayoutParams(params);
        }

        // preventing view not ready situation
        view.post(new Runnable()
        {
            @Override
            public void run()
            {
                float adHeight = convertPixelsToDp(view.getHeight());

                if (first || (adHeight > 0 && adHeight <= heightLimit))
                {
                    ViewGroup.LayoutParams params = view.getLayoutParams();

                    if (params == null)
                        params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
                    else
                        params.height = 0;

                    view.setLayoutParams(params);
                }
            }
        });

        if (view.getParent() != null && view.getParent() instanceof ViewGroup)
        {
            ViewGroup parent = (ViewGroup) view.getParent();

            removeAdView(packageName, parent, false, heightLimit);
        }
    }

    private static float convertPixelsToDp(float px)
    {
        DisplayMetrics metrics = Main.resources.getDisplayMetrics();

        return px / (metrics.densityDpi / 160f);
    }
}
