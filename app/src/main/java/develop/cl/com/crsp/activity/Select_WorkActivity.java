package develop.cl.com.crsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.Company;
import develop.cl.com.crsp.JavaBean.Work;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.util.MyList;
import develop.cl.com.crsp.util.VolleyCallback;


public class Select_WorkActivity extends BaseActivity implements View.OnClickListener
        , AdapterView.OnItemSelectedListener {
    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private static final String Tag = "Select_WorkActivity";
    private Intent mIntent;
    private String json;

    private ListView lvSelectWork;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private List<Map<String, Object>> datalistc;

    private TextView tvIcon;
    private TextView tvSeacrch;
    private TextView tvJianli;
    private Spinner spWork1;
    private Spinner spWork2;
    private Spinner spWork3;
    private Spinner spWork4;
    private String getspspSelectWork1;
    private String getspspSelectWork2;
    private String getspspSelectWork3;
    private String getspspSelectWork4;


//    ArrayAdapter<String> adapter1;
//    ArrayAdapter<String> adapter2;
//    ArrayAdapter<String> adapter3;
//    ArrayAdapter<String> adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_work);
        findViewById();
        initView();
        //下拉列表数据
//        String[] str_sp_select_work1 = new String[]{"区域1", "区域2", "区域3", "区域4", "区域5"};
//        String[] str_sp_select_work2 = new String[]{"类别1", "类别2", "类别3", "类别4", "类别5"};
//        String[] str_sp_select_work3 = new String[]{"薪资1", "薪资2", "薪资3", "薪资4", "薪资5"};
//        String[] str_sp_select_work4 = new String[]{"福利1", "福利2", "福利3", "福利4", "福利5"};

//        adapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work1);
//        adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work2);
//        adapter3 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work3);
//        adapter4 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work4);

//        sp_select_work1.setAdapter(adapter1);
//        sp_select_work2.setAdapter(adapter2);
//        sp_select_work3.setAdapter(adapter3);
//        sp_select_work4.setAdapter(adapter4);

//        SelectWorkBean selectWork = new SelectWorkBean();
//        GridViewData gridViewData_select_work = new GridViewData(datalist);
        //配置数据
//        selectWork.setWork_company(new String[]{"广西", "广东", "北京", "上海", "武汉", "重庆", "四川", "天津"});
//        selectWork.setWork_land(new String[]{"广西a", "广东a", "北京a", "上海a", "武汉a", "重庆a", "四川a", "天津a"});
//        selectWork.setWork_sal(new String[]{"1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000"});
//        selectWork.setWork_submit("申请");
//        selectWork.setWork_time(new String[]{"11", "22", "33", "44", "55", "66", "77", "88"});
//        selectWork.setWork_title(new String[]{"广西t", "广东t", "北京t", "上海t", "武汉t", "重庆t", "四川t", "天津t"});
//        selectWork.setWork_welfare(new String[]{"福利好1", "福利好2", "福利好3", "福利好4", "福利好5", "福利好6", "福利好7", "福利好8"});
//        //绑定数据
//        String[] mapName = new String[]{"title", "work_sal", "work_welfare", "work_company"
//                , "work_time", "work_land", "work_submit"};
//        int[] controlId = new int[]{R.id.tv_select_work_item_title, R.id.tv_select_work_item_sal
//                , R.id.tv_select_work_item_welfare, R.id.tv_select_work_item_company
//                , R.id.tv_select_work_item_time, R.id.tv_select_work_item_land, R.id.btn_select_work_item};
//        datalist = gridViewData_select_work.getData_SeleceWork(selectWork);
//        sadapter = new SimpleAdapter(this, datalist, R.layout.lv_select_work_item, mapName, controlId);
//
//        lvSelectWork.setAdapter(sadapter);
    }

    @Override
    protected void findViewById() {
        tvIcon = (TextView) this.findViewById(R.id.tv_select_work_search_icon);
        tvSeacrch = (TextView) this.findViewById(R.id.tv_select_work_search);
        tvJianli = (TextView) this.findViewById(R.id.tv_select_work_jianli);

        spWork1 = (Spinner) findViewById(R.id.sp_select_work1);
        spWork2 = (Spinner) findViewById(R.id.sp_select_work2);
        spWork3 = (Spinner) findViewById(R.id.sp_select_work3);
        spWork4 = (Spinner) findViewById(R.id.sp_select_work4);
        lvSelectWork = (ListView) findViewById(R.id.lv_select_work);
    }

    @Override
    protected void initView() {
        getspspSelectWork1 = spWork1.getSelectedItem().toString();
        getspspSelectWork2 = spWork2.getSelectedItem().toString();
        getspspSelectWork3 = spWork3.getSelectedItem().toString();
        getspspSelectWork4 = spWork4.getSelectedItem().toString();

        tvIcon.setOnClickListener(this);
        tvSeacrch.setOnClickListener(this);
        tvJianli.setOnClickListener(this);
        spWork1.setOnItemSelectedListener(this);
        spWork2.setOnItemSelectedListener(this);
        spWork3.setOnItemSelectedListener(this);
        spWork4.setOnItemSelectedListener(this);

        mIntent = this.getIntent();
        //从Intent获得额外信息，设置为TextView的文本
        json = mIntent.getStringExtra("result");
        mQueue = Volley.newRequestQueue(Select_WorkActivity.this);
        JSONObject jsonObject = JSON.parseObject(json);
        JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
        List<Work> wlist = JSON.parseArray(jsonMap.get("resultwork").toString(), Work.class);
        List<Company> clist = JSON.parseArray(jsonMap.get("resultcompany").toString(), Company.class);
        datalist = new ArrayList<Map<String, Object>>();
        datalistc = new ArrayList<Map<String, Object>>();
        listBeanToMapPic(wlist, datalist);
        listBeanToMapPic(clist, datalistc);
        String[] mapName = new String[]{"title", "salary", "welfare"
                , "source", "release_time", "work_area"};
        int[] controlId = new int[]{R.id.tv_select_work_item_title
                , R.id.tv_select_work_item_sal, R.id.tv_select_work_item_welfare
                , R.id.tv_select_work_item_company, R.id.tv_select_work_item_time
                , R.id.tv_select_work_item_land};
        sadapter = new SimpleAdapter(this, datalist, R.layout.lv_select_work_item, mapName, controlId);
        lvSelectWork.setAdapter(sadapter);
        lvSelectWork.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> nextMapw = datalist.get(position);
                Map<String, Object> nextMapc = datalistc.get(position);
                mIntent = new Intent(Select_WorkActivity.this, ShowDetailWorkActivity.class);
                //无法直接传map，需要序列化
                mIntent.putExtra("mapw", (Serializable) nextMapw);
                mIntent.putExtra("mapc", (Serializable) nextMapc);
                startActivity(mIntent);
            }
        });
    }

    /**
     * 将list中的bean转换成map
     *
     * @param olist
     * @param loclist
     */
    protected void listBeanToMapPic(List<?> olist, List<Map<String, Object>> loclist) {
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            loclist.add(map);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.sp_select_work1:
                getspspSelectWork1 = Select_WorkActivity.this.getResources().getStringArray(
                        R.array.list_str_quyu)[position];
                break;
            case R.id.sp_select_work2:
                getspspSelectWork2 = Select_WorkActivity.this.getResources().getStringArray(
                        R.array.list_str_leibie)[position];
                break;
            case R.id.sp_select_work3:
                getspspSelectWork3 = Select_WorkActivity.this.getResources().getStringArray(
                        R.array.list_str_sal)[position];
                break;
            case R.id.sp_select_work4:
                getspspSelectWork4 = Select_WorkActivity.this.getResources().getStringArray(
                        R.array.list_str_qita)[position];
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
