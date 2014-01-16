package tw.fatminmin.xposed.minminguard.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListView;

public class PrefsFragment extends ListFragment {
    static public ListView listView;
    private List<Map<String, Object>> itemList;
    
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        listView = getListView();
        setupAppList();
        setListAdapter(new CheckBoxAdapter(getActivity(), itemList));
    }
    
    private void setupAppList() {
        
        Context activity = getActivity();
        
        PackageManager pm = activity.getPackageManager();
        List<ApplicationInfo> list = pm.getInstalledApplications(0);
        
        itemList = new ArrayList<Map<String, Object>>();
        for(ApplicationInfo info : list) {
            
            if((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                
                Map<String, Object> map = new HashMap<String, Object>();
                
                map.put("title", pm.getApplicationLabel(info));
                map.put("key", info.packageName);
                map.put("icon", pm.getApplicationIcon(info));
                
                itemList.add(map);
            }
        }
        
        Collections.sort(itemList, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
                String s1 = (String) lhs.get("title");
                String s2 = (String) rhs.get("title");
                return s1.compareTo(s2);
            }
        });
    }
}
