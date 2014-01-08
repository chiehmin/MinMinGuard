package tw.fatminmin.xposed.minminguard;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;

public class BlockUrl {
	public static Context mContext;
	
	@SuppressLint("SdCardPath")
	public static void saveUrlsAsPreference(Context context) {
		
		mContext = context;
		
		if(!(new File("/sdcard/minminguard.txt")).exists()) {
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
		
		@SuppressLint("SdCardPath")
		@Override
		protected Void doInBackground(Object... params) {
			
			InputStream in;
			try {
				in = mContext.getAssets().open("host/output_file");
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				PrintWriter pw = new PrintWriter(new File("/sdcard/minminguard.txt"));
				String url;
				while((url = br.readLine()) != null) {
					pw.println(url);
				}
				pw.close();
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	
}
