package develop.cl.com.crsp.activity.fenleiactivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Courier;
import develop.cl.com.crsp.JavaBean.Goods;
import develop.cl.com.crsp.JavaBean.Information;
import develop.cl.com.crsp.JavaBean.SecondGoods;
import develop.cl.com.crsp.JavaBean.TrainTicket;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class Select_ListActivity extends BaseActivity implements View.OnClickListener
        , AdapterView.OnItemSelectedListener {
    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private static final String Tag = "Select_ListActivity";
    private Intent mIntent;
    private String json;
    private String type;
    private int classposition;

    private TextView tvIcon;
    private TextView tvSeacrch;
    private TextView tvFabu;
    private Spinner spSelectList1;
    private Spinner spSelectList2;
    private Spinner spSelectList3;
    private Spinner spSelectList4;

    private ListView lv_select_list;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalistp;

    private String getspspSelectList1;
    private String getspspSelectList2;
    private String getspspSelectList3;
    private String getspspSelectList4;
    private int listsize;
    private int count = 0;
    private String preUrl;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_list);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvIcon = (TextView) this.findViewById(R.id.tv_select_list_search_icon);
        tvSeacrch = (TextView) this.findViewById(R.id.tv_select_list_search);
        tvFabu = (TextView) this.findViewById(R.id.tv_select_list_fabu);

        lv_select_list = (ListView) this.findViewById(R.id.lv_select_list);
        spSelectList1 = (Spinner) findViewById(R.id.sp_select_list1);
        spSelectList2 = (Spinner) findViewById(R.id.sp_select_list2);
        spSelectList3 = (Spinner) findViewById(R.id.sp_select_list3);
        spSelectList4 = (Spinner) findViewById(R.id.sp_select_list4);
    }

    @Override
    protected void initView() {
        getspspSelectList1 = spSelectList1.getSelectedItem().toString();
        getspspSelectList2 = spSelectList2.getSelectedItem().toString();
        getspspSelectList3 = spSelectList3.getSelectedItem().toString();
        getspspSelectList4 = spSelectList4.getSelectedItem().toString();

        tvIcon.setOnClickListener(this);
        tvSeacrch.setOnClickListener(this);
        tvFabu.setOnClickListener(this);
        spSelectList1.setOnItemSelectedListener(this);
        spSelectList2.setOnItemSelectedListener(this);
        spSelectList3.setOnItemSelectedListener(this);
        spSelectList4.setOnItemSelectedListener(this);

        mIntent = this.getIntent();
        Log.d(Tag, mIntent.getStringExtra("typeName"));
        //从Intent获得额外信息，设置为TextView的文本
        type = mIntent.getStringExtra("typeName");
        json = mIntent.getStringExtra("result");
        classposition = mIntent.getIntExtra("classposition", -1);
        mQueue = Volley.newRequestQueue(Select_ListActivity.this);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
        spSelectList1.setVisibility(View.GONE);
        spSelectList2.setVisibility(View.GONE);
        spSelectList3.setVisibility(View.GONE);
        spSelectList4.setVisibility(View.GONE);
        switch (type) {
            case "二手交易":
                preUrl = ServerInformation.URL + "secondgoods";
                List<SecondGoods> sglist = JSON.parseArray(jsonMap.get("resultBean").toString(), SecondGoods.class);
                datalistp = new ArrayList<Map<String, Object>>();
                listBeanToMapPic(sglist);
                break;
            case "物品出租":
                preUrl = ServerInformation.URL + "goods";
                List<Goods> glist = JSON.parseArray(jsonMap.get("resultBean").toString(), Goods.class);
                datalistp = new ArrayList<Map<String, Object>>();
                listBeanToMapPic(glist);
                break;
            case "学习资料":
                preUrl = ServerInformation.URL + "learningdata";
                break;
            case "校内快捷服务":
                if (classposition == 0) {
                    preUrl = ServerInformation.URL + "courier";
                    List<Courier> clist = JSON.parseArray(jsonMap.get("resultBean").toString(), Courier.class);
                    datalistp = new ArrayList<Map<String, Object>>();
                    listBeanToMapPic(clist, datalistp);
//                    spSelectList1.setVisibility(View.GONE);
//                    spSelectList2.setVisibility(View.GONE);
//                    spSelectList3.setVisibility(View.GONE);
//                    spSelectList4.setVisibility(View.GONE);
                    setDataByFuwu(classposition);
                } else if (classposition == 1) {
                    preUrl = ServerInformation.URL + "trainticket";
                    List<TrainTicket> tlist = JSON.parseArray(jsonMap.get("resultBean").toString(), TrainTicket.class);
                    datalistp = new ArrayList<Map<String, Object>>();
                    listBeanToMapPic(tlist, datalistp);
//                    spSelectList1.setVisibility(View.GONE);
//                    spSelectList2.setVisibility(View.GONE);
//                    spSelectList3.setVisibility(View.GONE);
//                    spSelectList4.setVisibility(View.GONE);
                    setDataByFuwu(classposition);
                }
                break;
            case "校内资讯互动":
                preUrl = ServerInformation.URL + "information";
                List<Information> ilist = JSON.parseArray(jsonMap.get("resultBean").toString(), Information.class);
                datalistp = new ArrayList<Map<String, Object>>();
                listBeanToMapPic(ilist, datalistp);
//                spSelectList1.setVisibility(View.GONE);
//                spSelectList2.setVisibility(View.GONE);
//                spSelectList3.setVisibility(View.GONE);
//                spSelectList4.setVisibility(View.GONE);
                setDataByInfo();
                break;
            default:
                break;
        }

        /**
         * listview 点击监听
         */
        lv_select_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Map<String, Object> nextMap;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (type) {
                    case "二手交易":
                        nextMap = datalistp.get(position);
                        mIntent = new Intent(Select_ListActivity.this, ShowDetailGoodsActivity.class);
                        //无法直接传map，需要序列化
                        mIntent.putExtra("map", (Serializable) nextMap);
                        startActivity(mIntent);
                        break;
                    case "物品出租":
                        nextMap = datalistp.get(position);
                        mIntent = new Intent(Select_ListActivity.this, ShowDetailGoodsActivity.class);
                        //无法直接传map，需要序列化
                        mIntent.putExtra("map", (Serializable) nextMap);
                        startActivity(mIntent);
                        break;
                    case "学习资料":
                        break;
                    case "校内快捷服务":
                        if (classposition == 0) {
                            nextMap = datalistp.get(position);
                            mIntent = new Intent(Select_ListActivity.this, ShowDetailFuwuActivity.class);
                            //无法直接传map，需要序列化
                            mIntent.putExtra("map", (Serializable) nextMap);
                            mIntent.putExtra("classposition", classposition);
                            startActivity(mIntent);
                        } else if (classposition == 1) {
                            nextMap = datalistp.get(position);
                            mIntent = new Intent(Select_ListActivity.this, ShowDetailFuwuActivity.class);
                            //无法直接传map，需要序列化
                            mIntent.putExtra("map", (Serializable) nextMap);
                            mIntent.putExtra("classposition", classposition);
                            startActivity(mIntent);
                        }
                        break;
                    case "校内资讯互动":
                        nextMap = datalistp.get(position);
                        mIntent = new Intent(Select_ListActivity.this, ShowDetailInfoActivity.class);
                        //无法直接传map，需要序列化
                        mIntent.putExtra("map", (Serializable) nextMap);
                        startActivity(mIntent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 校内快捷服务数据绑定
     *
     * @param fuwu 服务类型
     */
    protected void setDataByFuwu(int fuwu) {
        String[] mapName;
        if (fuwu == 0) {
            String[] mapName1 = new String[]{"detail", "showtime", "userid"
                    , "merchant", "price", "degree", "receive_time"};
            mapName = mapName1;
        } else {
            String[] mapName1 = new String[]{"detail", "showtime", "userid"
                    , "station", "price", "degree", "receive_time"};
            mapName = mapName1;
        }
        int[] controlId = new int[]{R.id.iv_fuwu_detail, R.id.iv_fuwu_timeitem
                , R.id.iv_fuwu_user, R.id.iv_fuwu_station, R.id.iv_fuwu_price
                , R.id.iv_fuwu_degree, R.id.iv_fuwu_time};
        sadapter = new SimpleAdapter(this, datalistp, R.layout.lv_fuwu_item, mapName, controlId);
        lv_select_list.setAdapter(sadapter);
    }

    /**
     * 校内快捷服务数据绑定
     */
    protected void setDataByInfo() {
        String[] mapName = {"title", "theme", "showtime"};
        int[] controlId = new int[]{R.id.lv_info_list_title, R.id.lv_info_list_theme
                , R.id.lv_info_list_time};
        sadapter = new SimpleAdapter(this, datalistp, R.layout.lv_info_list_item, mapName, controlId);
        lv_select_list.setAdapter(sadapter);
    }

    /**
     * 将list中的bean转换成map(无图片)
     *
     * @param olist
     * @param loclist
     */
    protected void listBeanToMapPic(List<?> olist, List<Map<String, Object>> loclist) {
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            map.put("showtime", StringUtils.substring(map.get("release_time").toString(), 5, 16));
            loclist.add(map);
        }
    }

    /**
     * 将list中的bean转换成map
     *
     * @param olist list
     * @return
     */
    protected void listBeanToMapPic(List<?> olist) {
        listsize = olist.size();
        int sort = 0;
        Log.d(Tag, "listsize:" + listsize);
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            sort++;
            map.put("sort", sort);
            map.put("showtime", StringUtils.substring(map.get("release_time").toString(), 5, 16));
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
                            datalistp.add(map);
                            if (count == listsize) {
                                Collections.sort(datalistp, new Comparator<Map<String, Object>>() {
                                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                        return (int) o1.get("sort") - (int) o2.get("sort");
                                    }
                                });
                                /**
                                 * 构造数据填充listview
                                 */
                                String[] mapName = new String[]{"showpic", "userid", "scource", "title", "showtime"
                                        , "price", "area"};
                                int[] controlId = new int[]{R.id.iv_select_list_pic, R.id.iv_select_list_user
                                        , R.id.iv_select_list_source
                                        , R.id.iv_select_list_title, R.id.iv_select_list_timeitem
                                        , R.id.iv_select_list_price, R.id.iv_select_list_address};
                                sadapter = new SimpleAdapter(Select_ListActivity.this, datalistp
                                        , R.layout.lv_select_list_item, mapName, controlId);
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
                                lv_select_list.setAdapter(sadapter);
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

    /**
     * 条件查询，未实现
     */
    protected void LocQueryServer() {

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
                        mIntent = new Intent(Select_ListActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, preUrl, volleyCallback);
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 加载进度条
     */
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        Drawable drawable = getResources().getDrawable(R.drawable.loading_animation);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("请稍候，正在努力加载...");
        progressDialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.sp_select_list1:
                getspspSelectList1 = Select_ListActivity.this.getResources().getStringArray(
                        R.array.list_str_quyu)[position];
                LocQueryServer();
                break;
            case R.id.sp_select_list2:
                getspspSelectList2 = Select_ListActivity.this.getResources().getStringArray(
                        R.array.list_str_leibie)[position];
                break;
            case R.id.sp_select_list3:
                getspspSelectList3 = Select_ListActivity.this.getResources().getStringArray(
                        R.array.list_str_sal)[position];
                break;
            case R.id.sp_select_list4:
                getspspSelectList4 = Select_ListActivity.this.getResources().getStringArray(
                        R.array.list_str_qita)[position];
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
