package tw.fatminmin.xposed.minminguard.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.Util;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class LogFragment extends SherlockFragment {
    
    static public Handler mHandler;
    static public boolean mRunning;
    static public TextView tvLog;
    private ViewGroup root;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        
        root.removeAllViews();
        
    }
    
    public void refresh() {
        tvLog.setText("");
        tvLog.append("Log\n");
        tvLog.append("===========\n");
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                
                List<String> progs = new ArrayList<String>();
                Context context = getActivity();
                final String pname = "tw.fatminmin.xposed.minminguard";
                if (context.getPackageManager().checkPermission(android.Manifest.permission.READ_LOGS, pname) != 0) {
                    if (android.os.Build.VERSION.SDK_INT >= 16) {
                        progs.add("su");
                        progs.add("-c");
                        progs.add("pm grant tw.fatminmin.xposed.minminguard android.permission.READ_LOGS");
                        try {
                            Process proc = Runtime.getRuntime().exec(progs.toArray(new String[progs.size()]));
                            proc.waitFor();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
                
                progs.clear();
                progs.add("logcat");
                progs.add("-d");
                progs.add("-s");
                progs.add(Util.tag);
                
                try {
                    Process proc = Runtime.getRuntime().exec(progs.toArray(new String[progs.size()]));
                    
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(proc.getInputStream()), 1024);   
                    String line; 
                    while ((line = bufferedReader.readLine())!= null) {   
                        
                        if(line.contains("beginning of /dev/log")) {
                            continue;
                        }
                        
                        final String msg = line;
                        
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvLog.append(msg);   
                                tvLog.append("\n");
                            }
                        });
                    }
                    
                    bufferedReader.close();
                    proc.destroy();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            tvLog.append("===========\nEnd of log");
                        }
                    });
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.log_fragment, container);
        tvLog = (TextView) root.findViewById(R.id.tvLog);
        
        Settings.usingPrefFragment = false;
        getSherlockActivity().supportInvalidateOptionsMenu();
        
        refresh();
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
