package develop.cl.com.crsp.myutil;


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

/**
 * 　使用工具，将集合的形式转变
 */
public class MyList {
    /**
     * 使用BeanUtils工具，将bean中指定数据遍历转换为字符串数组形式，所指定的数据由所传递的字符串数组中的字符串内容决定；
     * 将两个字符串数组数据放置在同一个字符串数组中，添加至list，为请求服务器的参数拼凑做准备
     *
     * @param str  　bean中所需要遍历转换为字符串数组形式的参数数组列表
     * @param bean 封装好数据的bean对象
     * @return list内封装字符串数组，字符串数组内的数据为一一对应键值的两个字符串数组数据
     */
    public static List<String[]> strList(String[] str, Object bean) {
        List<String[]> mLlist = new ArrayList<String[]>();
        if (str.length != 0) {
            String[] str2 = new String[str.length];
            for (int i = 0; i < str.length; i++) {
                try {
                    //将bean对象遍历转换为数组
                    str2[i] = BeanUtils.getProperty(bean, str[i]);
                    //创建大小为2字符串数组，分别用来存储键值相对应的两个字符串数组
                    String[] mstr = new String[2];
                    //键
                    mstr[0] = str[i];
                    //值
                    mstr[1] = str2[i];
                    //添加至list
                    mLlist.add(mstr);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("mLlist.size", mLlist.size() + "");
        return mLlist;
    }

    /**
     * Bean --> Map ；利用Introspector和PropertyDescriptor 将Bean --> Map
     * 将bean对象遍历，转换为map对象
     *
     * @param obj 所需要转为的bean对象
     * @return 由bean所转换的map对象
     */
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
