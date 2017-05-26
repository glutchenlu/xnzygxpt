package develop.cl.com.crsp.myutil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;

/**
 * 将listview或GridView中item固定的参数绑定
 */
public class GridViewData {

    private List<Map<String, Object>> datalist;

    public GridViewData(List<Map<String, Object>> datalist) {
        this.datalist = datalist;
    }

    /**
     * 控件绑定的item只有1个显示文字的控件，将参数封装到list内
     *
     * @param iconName 文字参数
     * @return 已经绑定好参数的list
     */
    public List<Map<String, Object>> getData1(String[] iconName) {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("typeName", iconName[i]);
            datalist.add(map);
        }
        return datalist;
    }

    /**
     * 控件绑定的item有2个控件，一个文字，一个图片，图片固定，将参数封装至list
     *
     * @param iconName 文字参数
     * @return 已经绑定好参数的list
     */
    public List<Map<String, Object>> getDataP1(String[] iconName) {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            //listview右键头
            map.put("pic", R.mipmap.next);
            map.put("typeName", iconName[i]);
            datalist.add(map);
        }
        return datalist;
    }

    /**
     * 控件绑定的item有2个控件，一个文字，一个图片，图片不固定，将参数封装至list
     *
     * @param pic      图片参数
     * @param iconName 文字参数
     * @return 已经绑定好参数的list
     */
    public List<Map<String, Object>> getData2(int[] pic, String[] iconName) {
        for (int i = 0; i < iconName.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("pic", pic[i]);
            map.put("name", iconName[i]);
            datalist.add(map);
        }
        return datalist;
    }

    /**
     * 控件绑定的item有3个控件，2个文字，1个图片，图片不固定，将参数封装至list
     *
     * @param pic       图片参数
     * @param iconName1 文字参数1
     * @param iconName2 文字参数2
     * @return 已经绑定好参数的list
     */
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
}
