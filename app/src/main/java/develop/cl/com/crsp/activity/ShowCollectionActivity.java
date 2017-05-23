package develop.cl.com.crsp.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Company;
import develop.cl.com.crsp.JavaBean.MyCollection;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.fenleiactivity.ShowDetailFuwuActivity;
import develop.cl.com.crsp.activity.fenleiactivity.ShowDetailGoodsActivity;
import develop.cl.com.crsp.activity.fenleiactivity.ShowDetailInfoActivity;
import develop.cl.com.crsp.activity.fenleiactivity.ShowDetailWorkActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class ShowCollectionActivity extends BaseActivity {
    private static final String Tag = "ShowCollectionActivity";
    private Intent mIntent;
    private String returnBean;

    private RequestQueue mQueue;
    private VolleyCallback volleyCallback;
    private TextView tvAdd;

    private Button btnTop;
    private ListView lvUserShoucang;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    /**
     * 公司list，为匹配工作
     */
    private List<Map<String, Object>> datalistc;
    private int workPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fabu);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        lvUserShoucang = (ListView) this.findViewById(R.id.lv_userfabu);
        btnTop = (Button) this.findViewById(R.id.btn_userfabu_top);
        tvAdd = (TextView) this.findViewById(R.id.tv_userfabu_add);
    }

    @Override
    protected void initView() {

        tvAdd.setVisibility(View.GONE);
        btnTop.setText("我的收藏");
        mIntent = this.getIntent();
        returnBean = mIntent.getStringExtra("returnBean");
        JSONObject jsonMap = JSON.parseObject(returnBean);
        List<Company> cplist = JSON.parseArray(jsonMap.get("company").toString(), Company.class);
        List<MyCollection> mlist = JSON.parseArray(jsonMap.get("mycollection").toString(), MyCollection.class);

        datalistc = new ArrayList<Map<String, Object>>();
        datalist = new ArrayList<Map<String, Object>>();
        listBeanToMapPic(cplist);
        listBeanToMap(mlist);

        String[] mapName = new String[]{"serviceTitle", "collection_time", "typeName"};
        int[] controlId = new int[]{R.id.lv_fabu_item_title, R.id.lv_fabu_item_time, R.id.lv_fabu_item_class};
        sadapter = new SimpleAdapter(this, datalist, R.layout.lv_user_fabu, mapName, controlId);
        lvUserShoucang.setAdapter(sadapter);
        lvUserShoucang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> nextMap = datalist.get(position);
                String serviceUrl = "";
                Map<String, String> hmap = new HashMap<String, String>();
                //1、快递代领2、物品出租3、学习资料4、二手物品5、火车票代领6、兼职/全职7、资讯互动
                switch (nextMap.get("type").toString()) {
                    //1、快递代领
                    case "1":
                        serviceUrl = ServerInformation.URL + "courier/queryByID";
                        hmap.put("courierid", datalist.get(position).get("serviceid").toString());
                        sendQueryByServiceID(serviceUrl, ShowDetailFuwuActivity.class, -1, hmap);
                        break;
                    //2、物品出租
                    case "2":
                        serviceUrl = ServerInformation.URL + "goods/queryByID";
                        hmap.put("goodsid", datalist.get(position).get("serviceid").toString());
                        sendQueryByServiceID(serviceUrl, ShowDetailGoodsActivity.class, -1, hmap);
                        break;
                    //3、学习资料
                    case "3":
                        mIntent = new Intent(ShowCollectionActivity.this, MainActivity.class);
                        break;
                    //4、二手物品
                    case "4":
                        serviceUrl = ServerInformation.URL + "secondgoods/queryByID";
                        hmap.put("second_goodsid", datalist.get(position).get("serviceid").toString());
                        sendQueryByServiceID(serviceUrl, ShowDetailGoodsActivity.class, -1, hmap);
                        break;
                    //5、火车票代领
                    case "5":
                        serviceUrl = ServerInformation.URL + "trainticket/queryByID";
                        hmap.put("train_ticketid", datalist.get(position).get("serviceid").toString());
                        sendQueryByServiceID(serviceUrl, ShowDetailFuwuActivity.class, -1, hmap);
                        break;
                    //6、兼职/全职
                    case "6":
                        serviceUrl = ServerInformation.URL + "work/queryByID";
                        hmap.put("workid", datalist.get(position).get("serviceid").toString());
                        sendQueryByServiceID(serviceUrl, ShowDetailWorkActivity.class
                                , Integer.parseInt(nextMap.get("workPosition").toString()), hmap);
                        break;
                    //7、资讯互动
                    case "7":
                        serviceUrl = ServerInformation.URL + "information/queryByID";
                        hmap.put("informationid", datalist.get(position).get("serviceid").toString());
                        mIntent = new Intent(ShowCollectionActivity.this, ShowDetailInfoActivity.class);
                        sendQueryByServiceID(serviceUrl, ShowDetailFuwuActivity.class, -1, hmap);
                        break;
                    default:
                        break;
                }
            }
        });

        lvUserShoucang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                new AlertDialog.Builder(ShowCollectionActivity.this);
        normalDialog.setTitle("确定要删除这条收藏记录吗？");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int locmessageid = (Integer) delMap.get("collectionid");
                        delShoucang(locmessageid, locposition);
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

    protected void delShoucang(int id, final int locposition) {
        mQueue = Volley.newRequestQueue(ShowCollectionActivity.this);
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
        String url = ServerInformation.URL + "mycollection/del";
        //调用自定义的Volley函数
        Map<String, String> hmap = new HashMap<String, String>();
        hmap.put("collectionid", id + "");
//        DFVolley.NoMReq(mQueue, url, delVolleyCallback);
        DFVolley.NoMPots(mQueue, url, delVolleyCallback, hmap);
    }


    /**
     * 根据收藏的id获取到内容
     *
     * @param serviceUrl url
     * @param cls        跳转查看的界面class
     */
    protected void sendQueryByServiceID(String serviceUrl, final Class<?> cls, final int workposition, Map<String, String> hmap) {
        mQueue = Volley.newRequestQueue(ShowCollectionActivity.this);
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
//                        mIntent.putExtra("map", (Serializable) nextMap);
                        Map<String, Object> nextMap = JSON.parseObject(jsonMap.get("bean").toString());
                        mIntent = new Intent(ShowCollectionActivity.this, cls);
                        /**
                         * 判断是否为工作类型服务
                         */
                        if (cls.isAssignableFrom(ShowDetailWorkActivity.class)) {
                            Map<String, Object> nextMapc = datalistc
                                    .get(workposition);
                            mIntent.putExtra("mapw", (Serializable) nextMap);
                            mIntent.putExtra("mapc", (Serializable) nextMapc);
                        } else {
                            mIntent.putExtra("map", (Serializable) nextMap);
                        }
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //调用自定义的Volley函数
//        DFVolley.NoMReq(mQueue, serviceUrl, volleyCallback);
        DFVolley.NoMPots(mQueue, serviceUrl, volleyCallback, hmap);
    }

    protected void listBeanToMapPic(List<?> olist) {
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            datalistc.add(map);
        }
    }

    /**
     * 将list中的bean转换成map
     *
     * @param olist
     */
    protected void listBeanToMap(List<?> olist) {
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            if ("6".equals(map.get("type").toString())) {
//                map.put("typeName", "兼职/全职");
                map.put("workPosition", workPosition);
                workPosition++;
            }
            datalist.add(map);
        }
    }
}
