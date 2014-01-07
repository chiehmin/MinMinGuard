package tw.fatminmin.xposed.minminguard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.Window;

public class BlockUrl {
	public static Context mContext;
	private static SharedPreferences pref; 
	
	public static void saveUrlsAsPreference(Context context) {
		
		mContext = context;
		pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		
		if(pref.getStringSet("urls", null) == null) {
			new InitTask().execute(new Object[]{});
		}
	}
	
	
	private static class InitTask extends AsyncTask<Object, Void, Void> {
		
		Dialog dlgInit;
		
		@Override
		protected void onPreExecute() {
			dlgInit = new Dialog(mContext);
			dlgInit.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dlgInit.setContentView(R.layout.init);
			dlgInit.setCancelable(false);
			dlgInit.show();
		}
		
		@Override
		protected void onPostExecute(Void result) {
			dlgInit.dismiss();
		}
		
		@Override
		protected Void doInBackground(Object... params) {
			
			InputStream in;
			try {
				in = mContext.getAssets().open("host/output_file");
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				Set<String> set = new HashSet<String>();
				String url;
				while((url = br.readLine()) != null) {
					set.add(url);
				}
				
				pref.edit()
					.putStringSet("urls", set)
					.commit();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
}
