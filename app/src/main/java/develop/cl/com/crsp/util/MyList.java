package develop.cl.com.crsp.util;


import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javadz.beans.BeanInfo;
import javadz.beans.Introspector;
import javadz.beans.PropertyDescriptor;
import javadz.beanutils.BeanUtils;

public class MyList {
    /**
     * @param str  数组
     * @param bean bean
     * @return
     */
    public static List<String[]> strList(String[] str, Object bean) {
        List<String[]> mLlist = new ArrayList<String[]>();
        if (str.length != 0) {
            String[] str2 = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                try {
                    str2[i] = BeanUtils.getProperty(bean, str[i]);
                    String[] mstr = new String[2];
                    mstr[0] = str[i];
                    mstr[1] = str2[i];
                    mLlist.add(mstr);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("mLlist.size", mLlist.size() + "");
        return mLlist;
    }

    // Bean --> Map 1: 利用Introspector和PropertyDescriptor 将Bean --> Map
    public static Map<String, Object> transBean2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(obj);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            System.out.println("transBean2Map Error " + e);
        }
        return map;
    }
}
