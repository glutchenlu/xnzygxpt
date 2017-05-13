package develop.cl.com.crsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.fragment.GridViewData;

public class FabuClassListActivity extends BaseActivity implements View.OnClickListener {
    private static final String Tag = "FabuClassListActivity";
    private Intent mIntent;

    private TextView teTop;
    private ListView lv_class;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private int servicePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_list_class);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        teTop = (TextView) this.findViewById(R.id.tv_select_list_fabu_top);
        lv_class = (ListView) this.findViewById(R.id.lv_fabu_list);
    }

    @Override
    protected void initView() {
        lv_class.setDivider(null);
        mIntent = this.getIntent();
        Log.d(Tag, mIntent.getStringExtra("typeName"));
        //从Intent获得额外信息，设置为TextView的文本
        servicePosition = mIntent.getIntExtra("position", -1);
        teTop.setText(mIntent.getStringExtra("typeName"));
        switch (servicePosition) {
            case 0:
                String[] typeName0 = {"手机/手机配件", "二手笔记本", "二手电脑", "数码产品", "家用电器"
                        , "家具", "摩托车", "电动车", "自行车", "三轮车", "家居用品", "设备/办公用品"
                        , "服饰/箱包", "美容护肤/化妆品", "图书/乐器运动"
                        , "收藏品/工艺品", "闲置礼品", "虚拟物品", "其他物品"};
                mySetAdapter(lv_class, datalist, sadapter, typeName0, FabuGoodsdetailActivity.class);
                break;
            case 1:
                String[] typeName1 = {"手机/手机配件", "二手笔记本", "二手电脑", "数码产品", "家用电器"
                        , "家具", "摩托车", "电动车", "自行车", "三轮车", "家居用品", "设备/办公用品"
                        , "服饰/箱包", "美容护肤/化妆品", "图书/乐器运动"
                        , "收藏品/工艺品", "闲置礼品", "虚拟物品", "其他物品"};
                mySetAdapter(lv_class, datalist, sadapter, typeName1, FabuGoodsdetailActivity.class);
                break;
            case 2:
                String[] typeName2 = {"英语四/六级", "计算机一/二等级考试", "考研相关", "文史类书籍"
                        , "理学类", "信息类", "课外读物", "考试相关", "知识分享", "其他"};
                mySetAdapter(lv_class, datalist, sadapter, typeName2, FabuGoodsdetailActivity.class);
                break;
            case 3:
                String[] typeName3 = {"快递代领", "火车票代领"};
                mySetAdapter(lv_class, datalist, sadapter, typeName3, FabuGoodsdetailActivity.class);
                break;
            case 4:
                String[] typeName4 = {"校内新闻", "校内活动", "校内赛事", "校内八卦", "校内通知消息", "其他"};
                mySetAdapter(lv_class, datalist, sadapter, typeName4, FabuGoodsdetailActivity.class);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }

    protected void mySetAdapter(ListView lv, List<Map<String, Object>> list, SimpleAdapter adapter, String[] str, final Class<?> cls) {
        list = new ArrayList<Map<String, Object>>();
        GridViewData gridViewData = new GridViewData(list);
        final List<Map<String, Object>> finalList = gridViewData.getDataP1(str);
        adapter = new SimpleAdapter(FabuClassListActivity.this, finalList
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
                                          mIntent = new Intent(FabuClassListActivity.this,
                                                  cls);
                                          mIntent.putExtra("typeName", typeName);
                                          mIntent.putExtra("servicePosition", servicePosition);
                                          startActivity(mIntent);
                                      }
                                  }
        );
    }
}
