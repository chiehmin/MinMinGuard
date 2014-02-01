package tw.fatminmin.xposed.minminguard;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class MinMinProvider extends ContentProvider {
    
    SharedPreferences adPref;
    
    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
    public boolean onCreate() {
        adPref = getContext().getSharedPreferences("ad_pref", Context.MODE_WORLD_READABLE);
        return false;
    }

    @Override
    public Cursor query(Uri arg0, String[] arg1, String arg2, String[] arg3,
            String arg4) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        
        String packageName = uri.getPath().substring(1);
        Log.d("fatminmin", packageName);
        
        String network = values.getAsString("networks");
        
        adPref.edit()
            .putString(packageName, network)
            .commit();
        
        return 0;
    }

}
