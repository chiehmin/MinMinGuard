package tw.fatminmin.xposed.minminguard;

import android.util.Log;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;

public class Util {
    
    public static XSharedPreferences pref;
    
    
    final static public String tag = "MinMinGuard_v1.5.3";
    static public void log(String packageName, String msg) {
        if(pref.getBoolean(packageName + "_log", false)) {
            XposedBridge.log(msg);
            Log.d(tag, msg);
        }
    }
}
