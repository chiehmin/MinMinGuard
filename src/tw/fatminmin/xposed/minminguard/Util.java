package tw.fatminmin.xposed.minminguard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import tw.fatminmin.xposed.minminguard.ui.LogFragment;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class Util {
    
    public static XSharedPreferences pref;
    
    
    final static public String tag = "MinMinGuard_v1.7.1";
    static public void log(String packageName, String msg) {
        if(pref.getBoolean(packageName + "_log", false)) {
            XposedBridge.log(msg);
            Log.d(tag, msg);
        }
    }
    
    static public Application getCurrentApplication() {
        try {
            Application app = (Application)Class.forName("android.app.ActivityThread").
                    getMethod("currentApplication", new Class[0]).invoke(null, new Object[]{ null });
            return app;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static public void saveLog(final File dest, final Context context, final Handler handler) {
        
        handler.post(new Runnable() {
            
            @Override
            public void run() {
                try {
                    
                    Toast.makeText(context, context.getString(R.string.msg_saving_log), Toast.LENGTH_SHORT)
                        .show();
                    
                    dest.delete();
                    dest.createNewFile();
                    
                    FileOutputStream fout = new FileOutputStream(dest);
                    OutputStreamWriter writer = new OutputStreamWriter(fout);
                    writer.write(LogFragment.tvLog.getText().toString());
                    
                    writer.close();
                    fout.close();
                    
                    String str = String.format(context.getString(R.string.msg_saving_log_complete), dest.getPath());
                    Toast.makeText(context, str, Toast.LENGTH_SHORT)
                        .show();
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
