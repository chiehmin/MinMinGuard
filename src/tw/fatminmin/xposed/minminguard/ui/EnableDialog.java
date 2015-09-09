package tw.fatminmin.xposed.minminguard.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import tw.fatminmin.xposed.minminguard.R;

public class EnableDialog extends Activity {

    private SharedPreferences pref;
    private String requestPkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getSharedPreferences(getPackageName() + "_preferences", MODE_WORLD_READABLE);
        requestPkg = getIntent().getStringExtra("pkgName");

        pref.edit()
                .putBoolean(requestPkg + "_first", false)
                .commit();

        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_title_enable_mmg)
                .setMessage(R.string.dialog_msg_enable_mmg)
                .setPositiveButton(R.string.dialog_btn_enable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        pref.edit()
                            .putBoolean(requestPkg, true)
                            .commit();
                        dialog.dismiss();
                        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
                        am.killBackgroundProcesses(requestPkg);
                        Intent it = getPackageManager().getLaunchIntentForPackage(requestPkg);
                        if (it != null) {
                            it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(it);
                        }
                        finish();
                    }
                })
                .setNegativeButton(R.string.dialog_btn_no_enable, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .show();

    }
}
