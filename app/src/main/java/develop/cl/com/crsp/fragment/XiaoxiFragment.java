package develop.cl.com.crsp.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Message;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;


public class XiaoxiFragment extends Fragment {

    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private static final String Tag = "XiaoxiFragment";
    private Button btn_xiaoxi1;
    private Button btn_xiaoxi2;
    private int listsize;
    private int count = 0;
    private ProgressDialog progressDialog;

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
        btn_xiaoxi1 = (Button) view.findViewById(R.id.btn_xiaoxi1);
        btn_xiaoxi2 = (Button) view.findViewById(R.id.btn_xiaoxi2);
        lv_xiaoxi = (ListView) view.findViewById(R.id.lv_xiaoxi);
    }

    protected void init() {
        datalist = new ArrayList<Map<String, Object>>();
        LocQueryServer();
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
        Log.d(Tag, "listsize:" + listsize);
        for (int i = 0; i < listsize; i++) {
            final Map<String, Object> map = MyList.transBean2Map(olist.get(i));
            final Map<String, Object> mapb = MyList.transBean2Map(blist.get(i));
            sort++;
            map.put("sort", sort);
            map.put("pic", mapb.get("picture"));
            map.put("bname", mapb.get("name"));
            map.put("typeName", typeTotypename((Integer) (map.get("type"))));
            String str = StringUtils.substringBefore(map.get("pic").toString(), ",");
            /**
             * 根据地址请求服务器图片
             */
            ImageRequest imageRequest = new ImageRequest(str,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            count++;
                            Log.d(Tag, "count:" + count);
                            map.put("showpic", response);
                            datalist.add(map);
                            if (count == listsize) {
                                Collections.sort(datalist, new Comparator<Map<String, Object>>() {
                                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                        return (int) o1.get("sort") - (int) o2.get("sort");
                                    }
                                });
                                /**
                                 * 构造数据填充listview
                                 */
                                String str[] = {"showpic", "bname", "time", "typeName", "count"};
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
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                    progressDialog = null;
                                }
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
     * 根据选择的分类请求数据
     */
    protected void LocQueryServer() {
        mQueue = Volley.newRequestQueue(getActivity());
        //创建回调接口并实例化方法
        showProgressDialog();
        volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    DisPlay("服务器异常！");
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //显示提示消息
                    DisPlay(jsonMap.get("returnString").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
//                        JSONObject jsonBean = JSON.parseObject(jsonObject.get("resultBean").toString());
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

    /**
     * 加载进度条
     */
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        Drawable drawable = getResources().getDrawable(R.drawable.loading_animation);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("请稍候，正在努力加载...");
        progressDialog.show();
    }

    protected void DisPlay(String content) {
        Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT).show();
    }
}
