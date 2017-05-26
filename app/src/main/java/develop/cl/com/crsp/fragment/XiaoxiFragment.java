package develop.cl.com.crsp.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Message;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;


public class XiaoxiFragment extends Fragment {

    private boolean isLogin;
    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private static final String Tag = "XiaoxiFragment";
    private int listsize;
    //    private int count = 0;

    private ListView lv_xiaoxi;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_xiaoxi, container, false);
        findview();
        init();
        return view;
    }

    protected void findview() {
        lv_xiaoxi = (ListView) view.findViewById(R.id.lv_xiaoxi);
    }

    protected void init() {
        datalist = new ArrayList<Map<String, Object>>();
        /**
         * 登录状态并且网络状态可用
         */
        if (checkLogin()) {
            if (MyCheckNet.isNetworkAvailable(getActivity())) {
                LocQueryServer();
            }
        }
        lv_xiaoxi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showNormalDialog(datalist.get(position), position);
                return true;
            }
        });
    }

    private void showNormalDialog(final Map<String, Object> delMap, final int locposition) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(getActivity());
        normalDialog.setTitle("确定要删除这条消息记录吗？");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int locmessageid = (Integer) delMap.get("messageid");
                        delMessage(locmessageid, locposition);
                    }
                });
        normalDialog.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    protected void delMessage(int id, final int locposition) {
        //创建回调接口并实例化方法
        VolleyCallback delVolleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    DisPlay("服务器异常！");
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                        datalist.remove(locposition);
                        sadapter.notifyDataSetChanged();
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "message/del?messageid=" + id;
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, url, delVolleyCallback);
    }

    protected boolean checkLogin() {
        isLogin = false;
        if (MySharedPreferences.getLogin(getActivity()).equals("yes")) {
            isLogin = true;
        }
        return isLogin;
    }

    /**
     * 将list中的bean转换成map
     *
     * @param olist list
     * @return
     */
    protected void listBeanToMapPic(List<?> olist, List<?> blist) {
        listsize = olist.size();
        int sort = 0;
        int count = 0;
        final Map<String, Integer> countmap = new HashMap<String, Integer>();
        countmap.put("loccount", count);
        Log.d(Tag, "listsize:" + listsize);
        for (int i = 0; i < listsize; i++) {
            final Map<String, Object> map = MyList.transBean2Map(olist.get(i));
            final Map<String, Object> mapb = MyList.transBean2Map(blist.get(i));
            sort++;
            map.put("sort", sort);
            map.put("pic", mapb.get("picture"));
            map.put("bname", mapb.get("name"));
            map.put("showtime", StringUtils.substring(map.get("message_time").toString(), 5, 16));
            map.put("typeName", typeTotypename((Integer) (map.get("type"))));
            String str = StringUtils.substringBefore(map.get("pic").toString(), ",");
            /**
             * 根据地址请求服务器图片
             */
            ImageRequest imageRequest = new ImageRequest(str,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            int reCount = countmap.get("loccount");
                            reCount++;
                            countmap.put("loccount", reCount);
                            Log.d(Tag, "loccount:" + countmap.get("loccount"));
                            map.put("showpic", response);
                            datalist.add(map);
                            if (countmap.get("loccount") == listsize) {
                                Collections.sort(datalist, new Comparator<Map<String, Object>>() {
                                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                        return (int) o1.get("sort") - (int) o2.get("sort");
                                    }
                                });
                                /**
                                 * 构造数据填充listview
                                 */
                                String str[] = {"showpic", "bname", "showtime", "typeName", "count"};
                                int[] iconid = {R.id.iv_xiaoxi_pic, R.id.tv_xiaoxi_name, R.id.tv_xiaoxi_time
                                        , R.id.iv_xiaoxi_type, R.id.iv_xiaoxi_count};
                                sadapter = new SimpleAdapter(getActivity(), datalist, R.layout.lv_xiaoxi_item, str, iconid);
                                sadapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                    /**
                                     * 重写adapter设置图片
                                     * @param view
                                     * @param data
                                     * @param textRepresentation
                                     * @return
                                     */
                                    @Override
                                    public boolean setViewValue(View view, Object data,
                                                                String textRepresentation) {
                                        if (view instanceof ImageView && data instanceof Bitmap) {
                                            ImageView iv = (ImageView) view;
                                            iv.setImageBitmap((Bitmap) data);
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                lv_xiaoxi.setAdapter(sadapter);
                            }
                        }
                    }, 100, 100, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.getMessage());
                }
            });
            mQueue.add(imageRequest);
        }
    }

    protected String typeTotypename(int typeid) {
        String typeName = "";
        //1、快递代领2、物品出租3、学习资料4、二手物品5、火车票代领6、兼职/全职7、资讯互动
        switch (typeid) {
            case 1:
                typeName = "快递代领";
                break;
            case 2:
                typeName = "物品出租";
                break;
            case 3:
                typeName = "学习资料";
                break;
            case 4:
                typeName = "二手物品";
                break;
            case 5:
                typeName = "火车票代领";
                break;
            case 6:
                typeName = "兼职/全职";
                break;
            case 7:
                typeName = "资讯互动";
                break;
            default:
                break;
        }
        return typeName;
    }

    /**
     *
     */
    protected void LocQueryServer() {
        mQueue = Volley.newRequestQueue(getActivity());
        //创建回调接口并实例化方法
        volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    DisPlay("服务器异常！");
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //显示提示消息
                    DisPlay(jsonMap.get("returnString").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        List<Message> mglist = JSON.parseArray(jsonMap.get("message").toString(), Message.class);
                        List<Basic> bglist = JSON.parseArray(jsonMap.get("basic").toString(), Basic.class);
                        listBeanToMapPic(mglist, bglist);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "message/querybyuser?userid="
                + MySharedPreferences.getUserID(getActivity());
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, url, volleyCallback);
    }

    protected void DisPlay(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
}
