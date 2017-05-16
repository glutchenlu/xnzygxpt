package develop.cl.com.crsp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.Company;
import develop.cl.com.crsp.JavaBean.Courier;
import develop.cl.com.crsp.JavaBean.Goods;
import develop.cl.com.crsp.JavaBean.Information;
import develop.cl.com.crsp.JavaBean.LearningData;
import develop.cl.com.crsp.JavaBean.SecondGoods;
import develop.cl.com.crsp.JavaBean.TrainTicket;
import develop.cl.com.crsp.JavaBean.Work;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.util.MyList;

public class UserFabuActivity extends BaseActivity {
    private static final String Tag = "UserFabuActivity";
    private Intent mIntent;
    private String resultBean;

    private ListView lvUserFabu;
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
        lvUserFabu = (ListView) this.findViewById(R.id.lv_userfabu);
    }

    @Override
    protected void initView() {
        mIntent = this.getIntent();
        resultBean = mIntent.getStringExtra("returnBean");
        JSONObject jsonMap = JSON.parseObject(resultBean);
        List<Courier> clist = JSON.parseArray(jsonMap.get("courier").toString(), Courier.class);
        List<Goods> glist = JSON.parseArray(jsonMap.get("goods").toString(), Goods.class);
        List<Information> ilist = JSON.parseArray(jsonMap.get("information").toString(), Information.class);
        List<LearningData> ldlist = JSON.parseArray(jsonMap.get("learningData").toString(), LearningData.class);
        List<SecondGoods> sglist = JSON.parseArray(jsonMap.get("secondGoods").toString(), SecondGoods.class);
        List<TrainTicket> tlist = JSON.parseArray(jsonMap.get("trainTicket").toString(), TrainTicket.class);
        List<Work> wlist = JSON.parseArray(jsonMap.get("work").toString(), Work.class);
        List<Company> cplist = JSON.parseArray(jsonMap.get("company").toString(), Company.class);

        datalist = new ArrayList<Map<String, Object>>();
        datalistc = new ArrayList<Map<String, Object>>();
        listBeanToMapPic(cplist);
        listBeanToMap(clist);
        listBeanToMap(glist);
        listBeanToMap(ilist);
        listBeanToMap(ldlist);
        listBeanToMap(sglist);
        listBeanToMap(tlist);
        listBeanToMap(wlist);

        String[] mapName = new String[]{"title", "release_time", "typeName"};
        int[] controlId = new int[]{R.id.lv_fabu_item_title, R.id.lv_fabu_item_time, R.id.lv_fabu_item_class};
        sadapter = new SimpleAdapter(this, datalist, R.layout.lv_user_fabu, mapName, controlId);
        lvUserFabu.setAdapter(sadapter);
        lvUserFabu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> nextMap = datalist.get(position);
                //1、快递代领2、物品出租3、学习资料4、二手物品5、火车票代领6、兼职/全职7、资讯互动
                switch (nextMap.get("type").toString()) {
                    case "1":
                        mIntent = new Intent(UserFabuActivity.this, MainActivity.class);
                        break;
                    case "2":
                        mIntent = new Intent(UserFabuActivity.this, ShowDetailGoodsActivity.class);
                        //无法直接传map，需要序列化
                        mIntent.putExtra("map", (Serializable) nextMap);
                        break;
                    case "3":
                        mIntent = new Intent(UserFabuActivity.this, MainActivity.class);
                        break;
                    case "4":
                        mIntent = new Intent(UserFabuActivity.this, ShowDetailGoodsActivity.class);
                        //无法直接传map，需要序列化
                        mIntent.putExtra("map", (Serializable) nextMap);
                        break;
                    case "5":
                        mIntent = new Intent(UserFabuActivity.this, MainActivity.class);
                        break;
                    case "6":
                        mIntent = new Intent(UserFabuActivity.this, ShowDetailWorkActivity.class);
                        Map<String, Object> nextMapc = datalistc
                                .get(Integer.parseInt(nextMap.get("workPosition").toString()));
                        //无法直接传map，需要序列化
                        mIntent.putExtra("mapw", (Serializable) nextMap);
                        mIntent.putExtra("mapc", (Serializable) nextMapc);
                        break;
                    case "7":
                        mIntent = new Intent(UserFabuActivity.this, MainActivity.class);
                        break;
                    default:
                        break;
                }
                startActivity(mIntent);
            }
        });
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
            switch (map.get("type").toString()) {
                case "1":
                    map.put("typeName", "快递代领");
                    break;
                case "2":
                    map.put("typeName", "物品出租");
                    break;
                case "3":
                    map.put("typeName", "学习资料");
                    break;
                case "4":
                    map.put("typeName", "二手物品");
                    break;
                case "5":
                    map.put("typeName", "火车票代领");
                    break;
                case "6":
                    workPosition++;
                    map.put("typeName", "兼职/全职");
                    map.put("workPosition", workPosition);
                    break;
                case "7":
                    map.put("typeName", "资讯互动");
                    break;
                default:
                    break;
            }
            datalist.add(map);
        }
    }
}
