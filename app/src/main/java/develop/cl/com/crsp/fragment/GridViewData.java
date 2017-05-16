package develop.cl.com.crsp.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;


public class GridViewData {
    private List<Map<String, Object>> datalist;

    public GridViewData(List<Map<String, Object>> datalist) {
        this.datalist = datalist;
    }

    public List<Map<String, Object>> getData1(String[] iconName) {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("typeName", iconName[i]);
            datalist.add(map);
        }
        return datalist;
    }

    public List<Map<String, Object>> getDataP1(String[] iconName) {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", R.mipmap.next);
            map.put("typeName", iconName[i]);
            datalist.add(map);
        }
        return datalist;
    }

    public List<Map<String, Object>> getData2(int[] pic, String[] iconName) {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", pic[i]);
            map.put("name", iconName[i]);
            datalist.add(map);
        }
        return datalist;
    }

    public List<Map<String, Object>> getData3(int[] pic, String[] iconName1, String[] iconName2) {
        for (int i = 0; i < iconName1.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", pic[i]);
            map.put("name1", iconName1[i]);
            map.put("name2", iconName2[i]);
            datalist.add(map);
        }
        return datalist;
    }

    public List<Map<String, Object>> getData4(String[] iconName1, String[] iconName2, String[] time) {
        for (int i = 0; i < iconName1.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", R.drawable.ic_launcher);
            map.put("name1", iconName1[i]);
            map.put("name2", iconName2[i]);
            map.put("time", time[i]);
            datalist.add(map);
        }
        return datalist;
    }

//        for (int i = 0; i < selectwork.getWork_title().length; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", selectwork.getWork_title()[i]);
//            map.put("work_sal", selectwork.getWork_sal()[i]);
//            map.put("work_welfare", selectwork.getWork_welfare()[i]);
//            map.put("work_company", selectwork.getWork_company()[i]);
//            map.put("work_time", selectwork.getWork_time()[i]);
//            map.put("work_land", selectwork.getWork_land()[i]);
//            map.put("work_submit", selectwork.getWork_submit());
//            datalist.add(map);
//        }
//        return datalist;
//    }
}
