package tw.fatminmin.xposed.minminguard.ui;

import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import tw.fatminmin.xposed.minminguard.R;

/**
 * Created by fatminmin on 2015/10/3.
 */
public class MinMinGuardIntro extends AppIntro {
    private static final String GREEN_COLOR = "#4CAE50";
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance(
                getResources().getString(R.string.intro_auto_title),
                getResources().getString(R.string.intro_auto_content),
                R.drawable.intro1, Color.parseColor(GREEN_COLOR)));
        addSlide(AppIntroFragment.newInstance(
                getResources().getString(R.string.intro_manual_title),
                getResources().getString(R.string.intro_manual_content),
                R.drawable.intro2, Color.parseColor(GREEN_COLOR)));
        addSlide(AppIntroFragment.newInstance(
                getResources().getString(R.string.intro_tap_icon_title),
                getResources().getString(R.string.intro_tap_icon_content),
                R.drawable.intro3, Color.parseColor(GREEN_COLOR)));
        addSlide(AppIntroFragment.newInstance(
                getResources().getString(R.string.intro_app_detail_title),
                getResources().getString(R.string.intro_app_detail_content),
                R.drawable.intro4, Color.parseColor(GREEN_COLOR)));
        addSlide(AppIntroFragment.newInstance(
                getResources().getString(R.string.intro_url_filtering_title),
                getResources().getString(R.string.intro_url_filtering_content),
                R.drawable.intro5, Color.parseColor(GREEN_COLOR)));
    }

    @Override
    public void onSkipPressed() {
        finish();
    }

    @Override
    public void onDonePressed() {
        finish();
    }
}
