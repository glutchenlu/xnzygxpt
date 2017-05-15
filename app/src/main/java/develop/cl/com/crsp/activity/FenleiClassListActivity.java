package develop.cl.com.crsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.fragment.GridViewData;
import develop.cl.com.crsp.util.DFVolley;
import develop.cl.com.crsp.util.ServerInformation;
import develop.cl.com.crsp.util.VolleyCallback;

import static develop.cl.com.crsp.R.id.tv_fenlei_search_icon;

public class FenleiClassListActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private static final String Tag = "FenleiClassListActivity";
    private Intent mIntent;

    private TextView tvIcon;
    private TextView tvSeacrch;
    private TextView tvFabu;

    private ListView lv_class;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private int servicePosition;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fenlei_list_class);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvIcon = (TextView) this.findViewById(tv_fenlei_search_icon);
        tvSeacrch = (TextView) this.findViewById(R.id.tv_fenlei_search);
        tvFabu = (TextView) this.findViewById(R.id.tv_select_fabu);
        lv_class = (ListView) this.findViewById(R.id.lv_fenlei_list);
    }

    @Override
    protected void initView() {
        tvIcon.setOnClickListener(this);
        tvSeacrch.setOnClickListener(this);
        tvFabu.setOnClickListener(this);

        lv_class.setDivider(null);
        mIntent = this.getIntent();
        Log.d(Tag, mIntent.getStringExtra("typeName"));
        //从Intent获得额外信息，设置为TextView的文本
        servicePosition = mIntent.getIntExtra("position", -1);
        type = mIntent.getStringExtra("typeName");
        switch (servicePosition) {
            case 0:
                String[] typeName0 = {"手机/手机配件", "二手笔记本", "二手电脑", "数码产品", "家用电器"
                        , "家具", "摩托车", "电动车", "自行车", "三轮车", "家居用品", "设备/办公用品"
                        , "服饰/箱包", "美容护肤/化妆品", "图书/乐器运动"
                        , "收藏品/工艺品", "闲置礼品", "虚拟物品", "其他物品"};
                mySetAdapter(lv_class, datalist, sadapter, typeName0, Select_ListActivity.class, 0);
                break;
            case 1:
                String[] typeName1 = {"手机/手机配件", "二手笔记本", "二手电脑", "数码产品", "家用电器"
                        , "家具", "摩托车", "电动车", "自行车", "三轮车", "家居用品", "设备/办公用品"
                        , "服饰/箱包", "美容护肤/化妆品", "图书/乐器运动"
                        , "收藏品/工艺品", "闲置礼品", "虚拟物品", "其他物品"};
                mySetAdapter(lv_class, datalist, sadapter, typeName1, Select_ListActivity.class, 1);
                break;
            case 2:
                String[] typeName2 = {"英语四/六级", "计算机一/二等级考试", "考研相关", "文史类书籍"
                        , "理学类", "信息类", "课外读物", "考试相关", "知识分享", "其他"};
                mySetAdapter(lv_class, datalist, sadapter, typeName2, Select_ListActivity.class, 2);
                break;
            case 3:
                String[] typeName3 = {"快递代领", "火车票代领"};
                mySetAdapter(lv_class, datalist, sadapter, typeName3, Select_ListActivity.class, 3);
                break;
            case 4:
                String[] typeName4 = {"校内新闻", "校内活动", "校内赛事", "校内八卦", "校内通知消息", "其他"};
                mySetAdapter(lv_class, datalist, sadapter, typeName4, Select_ListActivity.class, 4);
                break;
            default:
                break;
        }
    }

    protected void mySetAdapter(ListView lv, List<Map<String, Object>> list, SimpleAdapter adapter
            , String[] str, final Class<?> cls, final int firstposition) {
        list = new ArrayList<Map<String, Object>>();
        GridViewData gridViewData = new GridViewData(list);
        final List<Map<String, Object>> finalList = gridViewData.getDataP1(str);
        adapter = new SimpleAdapter(FenleiClassListActivity.this, finalList
                , R.layout.lv_fabu_list, new String[]{"pic", "typeName"}
                , new int[]{R.id.iv_fabu_list_next, R.id.tv_fabu_list_item});
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          Log.d(Tag, "点击:" + position);
                                          Log.d(Tag, "ID:" + id);
                                          Map<String, Object> locMap = finalList.get(position);
                                          String typeName = locMap.get("typeName").toString();
                                          String sendUrl = "";
                                          switch (firstposition) {
                                              case 0:
                                                  sendUrl = ServerInformation.URL +
                                                          "secondgoods/queryclass?classify=" + typeName;
                                                  break;
                                              case 1:
                                                  sendUrl = ServerInformation.URL +
                                                          "goods/queryclass?classify=" + typeName;
                                                  break;
                                              case 2:
                                                  sendUrl = ServerInformation.URL +
                                                          "learningdata/queryclass?classify=" + typeName;
                                                  break;
                                              case 3:
                                                  if ("快递代领".equals(typeName)) {
                                                      sendUrl = ServerInformation.URL + "courier/queryclass";
                                                  } else if ("火车票代领".equals(typeName)) {
                                                      sendUrl = ServerInformation.URL + "trainticket/queryclass";
                                                  }
                                                  break;
                                              case 4:
                                                  sendUrl = ServerInformation.URL +
                                                          "information/queryclass?classify=" + typeName;
                                                  break;
                                              default:
                                                  break;
                                          }
                                          LocQueryServer(sendUrl, cls, position);
                                      }
                                  }
        );
    }

    /**
     * 根据选择的分类请求数据
     *
     * @param url           服务器地址
     * @param cls           将要跳转的页面
     * @param classposition list的序列
     */
    protected void LocQueryServer(String url, final Class<?> cls, final int classposition) {
        mQueue = Volley.newRequestQueue(FenleiClassListActivity.this);
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
                        mIntent = new Intent(FenleiClassListActivity.this, cls);
                        mIntent.putExtra("typeName", type);
                        mIntent.putExtra("classposition", classposition);
                        mIntent.putExtra("result", result);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, url, volleyCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_fenlei_search_icon:
                mIntent = new Intent(FenleiClassListActivity.this, SearchActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_fenlei_search:
                mIntent = new Intent(FenleiClassListActivity.this, SearchActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_select_fabu:
                mIntent = new Intent(FenleiClassListActivity.this,
                        FabuClassListActivity.class);
                mIntent.putExtra("typeName", type);
                mIntent.putExtra("position", servicePosition);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
