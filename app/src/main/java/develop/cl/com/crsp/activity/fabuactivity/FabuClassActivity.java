package develop.cl.com.crsp.activity.fabuactivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.SearchActivity;
import develop.cl.com.crsp.myutil.GridViewData;

public class FabuClassActivity extends BaseActivity {
    private Intent mIntent;
    private static final String Tag = "FabuClassActivity";
    private TextView searchTv;

    private GridView gv_fabu1;
    private SimpleAdapter sadapter1;
    private List<Map<String, Object>> datalist1;

    private GridView gv_fabu2;
    private SimpleAdapter sadapter2;
    private List<Map<String, Object>> datalist2;

    private GridView gv_fabu3;
    private SimpleAdapter sadapter3;
    private List<Map<String, Object>> datalist3;

    private GridView gv_fabu4;
    private SimpleAdapter sadapter4;
    private List<Map<String, Object>> datalist4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_class_fabu);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        searchTv = (TextView) this.findViewById(R.id.tv_fabu_search);
        gv_fabu1 = (GridView) this.findViewById(R.id.gv_select_class_fabu1);
        gv_fabu2 = (GridView) this.findViewById(R.id.gv_select_class_fabu2);
        gv_fabu3 = (GridView) this.findViewById(R.id.gv_select_class_fabu3);
        gv_fabu4 = (GridView) this.findViewById(R.id.gv_select_class_fabu4);
    }

    @Override
    protected void initView() {

        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(FabuClassActivity.this, SearchActivity.class);
                startActivity(mIntent);
            }
        });
        //1
        String[] typeName1 = {"销售", "司机", "行政/后勤", "人力资源", "客服", "财务审计"
                , "房产/开发", "淘宝职位", "保险", "翻译", "物业管理", "金融/证券", "市场/公关"
                , "编辑/出版", "广告/会展", "法律", "高级管理", "信托/担保"};
        mySetAdapter(gv_fabu1, datalist1, sadapter1, typeName1);
        //2
        String[] typeName2 = {"零售/百货", "餐饮/酒店", "家政/安保", "美容/美发", "医院/医疗"
                , "保健按摩", "旅游", "运动健康"};
        mySetAdapter(gv_fabu2, datalist2, sadapter2, typeName2);
        //3
        String[] typeName3 = {"技工/工人", "贸易/物流", "汽车制造", "机械设计", "生产/制造"
                , "通信/网络", "建筑/装修", "电气/能源", "农/林/牧/渔", "生物工程", "环保"
                , "化工", "产品/运行", "软件开发", "运维测试", "服装生产", "电子/电器"};
        mySetAdapter(gv_fabu3, datalist3, sadapter3, typeName3);
        //4
        String[] typeName4 = {"咨询/顾问", "设计/创意", "教育/培训", "媒体/影视"};
        mySetAdapter(gv_fabu4, datalist4, sadapter4, typeName4);
    }

    protected void mySetAdapter(GridView gv, List<Map<String, Object>> list, SimpleAdapter adapter, String[] str) {
        list = new ArrayList<Map<String, Object>>();
        GridViewData gridViewData = new GridViewData(list);
        final List<Map<String, Object>> finalList = gridViewData.getData1(str);
        adapter = new SimpleAdapter(FabuClassActivity.this, finalList
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
                                          mIntent = new Intent(FabuClassActivity.this,
                                                  FabudetailActivity.class);
                                          mIntent.putExtra("typeName", typeName);
                                          startActivity(mIntent);
                                      }
                                  }
        );
    }
}
