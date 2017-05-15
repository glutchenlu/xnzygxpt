package develop.cl.com.crsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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


public class Select_ClassActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private Intent mIntent;
    private static final String Tag = "Select_ClassActivity";
    private TextView tvIcon;
    private TextView searchTv;
    private TextView tvJianli;
    private TextView tvRemind;

    private GridView gv_fenlei1;
    private SimpleAdapter sadapter1;
    private List<Map<String, Object>> datalist1;

    private GridView gv_fenlei2;
    private SimpleAdapter sadapter2;
    private List<Map<String, Object>> datalist2;

    private GridView gv_fenlei3;
    private SimpleAdapter sadapter3;
    private List<Map<String, Object>> datalist3;

    private GridView gv_fenlei4;
    private SimpleAdapter sadapter4;
    private List<Map<String, Object>> datalist4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_class);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {

        tvRemind = (TextView) this.findViewById(R.id.tv_select_remind);
        tvJianli = (TextView) this.findViewById(R.id.tv_select_class_jianli);
        tvIcon = (TextView) this.findViewById(R.id.tv_select_class_search_icon);
        searchTv = (TextView) this.findViewById(R.id.tv_select_class_search);

        gv_fenlei1 = (GridView) this.findViewById(R.id.gv_select_work_fenlei1);
        gv_fenlei2 = (GridView) this.findViewById(R.id.gv_select_work_fenlei2);
        gv_fenlei3 = (GridView) this.findViewById(R.id.gv_select_work_fenlei3);
        gv_fenlei4 = (GridView) this.findViewById(R.id.gv_select_work_fenlei4);
    }

    @Override
    protected void initView() {
        tvRemind.setOnClickListener(this);
        tvJianli.setOnClickListener(this);
        tvIcon.setOnClickListener(this);
        searchTv.setOnClickListener(this);
        //1
        String[] typeName1 = {"销售", "司机", "行政/后勤", "人力资源", "客服", "财务审计"
                , "房产/开发", "淘宝职位", "保险", "翻译", "物业管理", "金融/证券", "市场/公关"
                , "编辑/出版", "广告/会展", "法律", "高级管理", "信托/担保"};
        mySetAdapter(gv_fenlei1, datalist1, sadapter1, typeName1);
        //2
        String[] typeName2 = {"零售/百货", "餐饮/酒店", "家政/安保", "美容/美发", "医院/医疗"
                , "保健按摩", "旅游", "运动健康"};
        mySetAdapter(gv_fenlei2, datalist2, sadapter2, typeName2);
        //3
        String[] typeName3 = {"技工/工人", "贸易/物流", "汽车制造", "机械设计", "生产/制造"
                , "通信/网络", "建筑/装修", "电气/能源", "农/林/牧/渔", "生物工程", "环保"
                , "化工", "产品/运行", "软件开发", "运维测试", "服装生产", "电子/电器"};
        mySetAdapter(gv_fenlei3, datalist3, sadapter3, typeName3);
        //4
        String[] typeName4 = {"咨询/顾问", "设计/创意", "教育/培训", "媒体/影视"};
        mySetAdapter(gv_fenlei4, datalist4, sadapter4, typeName4);
    }

    protected void mySetAdapter(GridView gv, List<Map<String, Object>> list, SimpleAdapter adapter, String[] str) {
        list = new ArrayList<Map<String, Object>>();
        GridViewData gridViewData = new GridViewData(list);
        final List<Map<String, Object>> finalList = gridViewData.getData1(str);
        adapter = new SimpleAdapter(Select_ClassActivity.this, finalList
                , R.layout.gv_fabu_class_item, new String[]{"typeName"}
                , new int[]{R.id.tv_fabu_class_item});
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                          Log.d(Tag, "点击:" + position);
                                          Log.d(Tag, "ID:" + id);
                                          Map<String, Object> locMap = finalList.get(position);
                                          String typeName = locMap.get("typeName").toString();
                                          String sendUrl = ServerInformation.URL +
                                                  "work/querybyindustry?industry=" + typeName;
                                          LocQueryServer(sendUrl);
                                      }
                                  }
        );
    }

    /**
     * 根据选择的分类请求数据
     *
     * @param url 服务器地址
     */
    protected void LocQueryServer(String url) {
        mQueue = Volley.newRequestQueue(Select_ClassActivity.this);
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
                        mIntent = new Intent(Select_ClassActivity.this, Select_WorkActivity.class);
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
            case R.id.tv_select_remind:
                mIntent = new Intent(Select_ClassActivity.this,
                        JianliActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_select_class_jianli:
                mIntent = new Intent(Select_ClassActivity.this,
                        JianliActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_select_class_search_icon:
                mIntent = new Intent(Select_ClassActivity.this, SearchActivity.class);
                startActivity(mIntent);
                break;
            case R.id.tv_select_class_search:
                mIntent = new Intent(Select_ClassActivity.this, SearchActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}