package tw.fatminmin.xposed.minminguard;

import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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
		
		final TextView title = (TextView) convertView.findViewById(R.id.title);
		final TextView summary = (TextView) convertView.findViewById(R.id.summary);
		final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkCheckBox);
		final ImageButton imgEdit = (ImageButton) convertView.findViewById(R.id.edit);
		
		final String sTitle = (String) mItemList.get(position).get("title");
		final String sSummary = (String) mItemList.get(position).get("summary");
		final String key = (String) mItemList.get(position).get("key");
		final Drawable dIcon = (Drawable) mItemList.get(position).get("icon");
		
		title.setText(sTitle);
		summary.setText(sSummary);
		icon.setImageDrawable(dIcon);
		
		if(pref.getBoolean(key, false)) {
		    checkBox.setChecked(true);
		    imgEdit.setVisibility(View.VISIBLE);
		}
		else {
		    checkBox.setChecked(false);
		}
		
		imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View checkBoxView = View.inflate(mContext, R.layout.settings, null);
                CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
                
                
                checkBox.setChecked(pref.getBoolean(key + "_url", true));
                
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        pref.edit()
                            .putBoolean(key + "_url", isChecked)
                            .commit();
                    }
                });
                checkBox.setText(mContext.getString(R.string.enable_url_filtering_data));

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("Setting")
                           .setIcon(R.drawable.ic_launcher)
                           .setView(checkBoxView)
                           .setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dlg, int id) {
                                    dlg.dismiss();
                                }
                           })
                           .show();
            }
        });
		
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				pref.edit()
					.putBoolean(key, isChecked)
					.commit();
				if(isChecked) {
				    imgEdit.setVisibility(View.VISIBLE);
				}
				else {
				    imgEdit.setVisibility(View.GONE);
				}
			}
		});
		
		return convertView;
	}

}
