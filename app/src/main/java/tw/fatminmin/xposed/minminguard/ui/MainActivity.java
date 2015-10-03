package tw.fatminmin.xposed.minminguard.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.ui.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, MainFragment.newInstance())
                    .commit();
        }
    }
}
