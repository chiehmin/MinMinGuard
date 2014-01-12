package tw.fatminmin.xposed.minminguard;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckBoxAdapter extends BaseAdapter {
	
	
	private List<Map<String, Object>> mItemList;
	private Context mContext;
	private LayoutInflater mInflater;
	private SharedPreferences pref;
	
	public CheckBoxAdapter(Context context, List<Map<String, Object>> itemList) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mItemList = itemList;
		pref = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
	
	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mItemList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.preference_checkbox, null);
		}
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView summary = (TextView) convertView.findViewById(R.id.summary);
		ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkCheckBox);
		
		final String sTitle = (String) mItemList.get(position).get("title");
		final String sSummary = (String) mItemList.get(position).get("summary");
		final String key = (String) mItemList.get(position).get("key");
		final Drawable dIcon = (Drawable) mItemList.get(position).get("icon");
		
		title.setText(sTitle);
		summary.setText(sSummary);
		icon.setImageDrawable(dIcon);
		checkBox.setChecked(pref.getBoolean(key, false));
		
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				pref.edit()
					.putBoolean(key, isChecked)
					.commit();
			}
		});
		
		return convertView;
	}

}
