package tw.fatminmin.xposed.minminguard.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.fatminmin.xposed.minminguard.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

public class PrefsFragment extends SherlockFragment {
    
    static public ListView listView;
    
    private CheckBoxAdapter mAdapter;
    private EditText search;
    private List<Map<String, Object>> itemList;
    private ViewGroup root;
    private SharedPreferences adPref;
    
    public void refresh() {
        setupAppList();
        mAdapter = new CheckBoxAdapter(getActivity(), itemList);
        listView.setAdapter(mAdapter);
        setupSearch();
    }
    
    @Override
    public void onDestroyView() {
        
        root.removeAllViews();
        
        super.onDestroyView();
    }
    
    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        
        root = (ViewGroup) inflater.inflate(R.layout.pref_fragment, container);
        listView = (ListView) root.findViewById(R.id.listview);
        search = (EditText) root.findViewById(R.id.search);
        
        Settings.usingPrefFragment = true;
        getSherlockActivity().supportInvalidateOptionsMenu();
        
        adPref = getActivity().getSharedPreferences("ad_pref", Context.MODE_WORLD_READABLE);
        refresh();
        
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    private void setupSearch() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                
            }
            @Override
            public void afterTextChanged(Editable s) {
                
            }
        });
    }
    
    private void setupAppList() {
        
        Context activity = getActivity();
        
        PackageManager pm = activity.getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(0);
        
        boolean showSystemApp = Settings.uiPref.getBoolean("show_system_apps", false);
        boolean showAppWithAds = Settings.uiPref.getBoolean("show_apps_with_ads", false);
        
        itemList = new ArrayList<Map<String, Object>>();
        for(ApplicationInfo info : list) {
            
            if(showSystemApp || ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0)) {
                
                Map<String, Object> map = new HashMap<String, Object>();
                
                map.put("title", pm.getApplicationLabel(info));
                map.put("key", info.packageName);
                map.put("icon", pm.getApplicationIcon(info));
                String ads = adPref.getString(info.packageName, "");
                map.put("summary", ads);
                
                if (ads.length > 0) {
                    itemList.add(map);
                }
                else {
                    if (!showAppWithAds) {
                        itemList.add(map);
                    }
                }
            }
        }
        
        Collections.sort(itemList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
                String s1 = (String) lhs.get("title");
                String s2 = (String) rhs.get("title");
                return s1.compareToIgnoreCase(s2);
            }
        });
    }
}
