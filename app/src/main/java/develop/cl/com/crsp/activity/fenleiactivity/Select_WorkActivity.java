package develop.cl.com.crsp.activity.fenleiactivity;

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

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Company;
import develop.cl.com.crsp.JavaBean.Work;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.VolleyCallback;


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
        if (MySharedPreferences.getResumeCount(Select_WorkActivity.this) != 0) {
            tvJianli.setVisibility(View.INVISIBLE);
        }

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
        listBeanToMapWork(wlist, datalist);
        listBeanToMapPic(clist, datalistc);
        String[] mapName = new String[]{"title", "salary", "welfare"
                , "source", "showtime", "work_area"};
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

    protected void listBeanToMapWork(List<?> olist, List<Map<String, Object>> loclist) {
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            map.put("showtime", StringUtils.substring(map.get("release_time").toString(), 5, 16));
//            2017-05-01 03:45:37
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
