package tw.fatminmin.xposed.minminguard.ui;

import java.util.Map;

import tw.fatminmin.xposed.minminguard.R;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class SelectAllAsyncTask extends AsyncTask<Object, Integer, Void> {
    
    static private Context mContext;
    static private Handler mHandler;
    private Dialog dlg;
    private Boolean mValue;
    
    public SelectAllAsyncTask(Boolean value) {
        mValue = value;
    }
    
    static public void setup(Context context, Handler handler) {
        mContext = context;
        mHandler = handler;
    }
    
    @Override
    protected void onPreExecute() {
        dlg = new Dialog(mContext);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.selecting_all);
        dlg.setCancelable(false);
        
        TextView tv = (TextView) dlg.findViewById(R.id.selecting);
        if(mValue) {
            tv.setText(mContext.getString(R.string.msg_selecting_all));
        }
        else {
            tv.setText(mContext.getString(R.string.msg_deselecting_all));
        }
        
        dlg.show();
    }
    
    @Override
    protected void onPostExecute(Void result) {
        dlg.dismiss();
    }
    
    @Override
    protected Void doInBackground(Object... args) {
        
        final ListView listView = PrefsFragment.listView;
        final SharedPreferences pref = Settings.pref;
        
        for(int i = 0; i < listView.getAdapter().getCount(); i++) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) listView.getAdapter().getItem(i);
            
            String key = (String) map.get("key");
            pref.edit() 
                .putBoolean(key, mValue)
                .commit();
        }
        for(int i = 0; i < listView.getChildCount(); i++) {
            
            final int index = i;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    View v = listView.getChildAt(index);
                    CheckBox cb = (CheckBox) v.findViewById(R.id.chkCheckBox);
                    ImageButton imgEdit = (ImageButton) v.findViewById(R.id.edit);
                    
                    cb.setChecked(mValue);
                    if(mValue) {
                        imgEdit.setVisibility(View.VISIBLE);
                    }
                    else {
                        imgEdit.setVisibility(View.GONE);
                    }
                }
            });
        }
        return null;
    }

}
