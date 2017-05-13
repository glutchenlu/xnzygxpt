package develop.cl.com.crsp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.SelectWorkBean;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.fragment.GridViewData;


public class Select_WorkActivity extends AppCompatActivity {

    private ListView lv_select_work;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;

    private Spinner sp_select_work1;
    private Spinner sp_select_work2;
    private Spinner sp_select_work3;
    private Spinner sp_select_work4;

    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    ArrayAdapter<String> adapter3;
    ArrayAdapter<String> adapter4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_work);

        //下拉列表数据
        String[] str_sp_select_work1 = new String[]{"区域1", "区域2", "区域3", "区域4", "区域5"};
        String[] str_sp_select_work2 = new String[]{"类别1", "类别2", "类别3", "类别4", "类别5"};
        String[] str_sp_select_work3 = new String[]{"薪资1", "薪资2", "薪资3", "薪资4", "薪资5"};
        String[] str_sp_select_work4 = new String[]{"福利1", "福利2", "福利3", "福利4", "福利5"};


        sp_select_work1 = (Spinner) findViewById(R.id.sp_select_work1);
        sp_select_work2 = (Spinner) findViewById(R.id.sp_select_work2);
        sp_select_work3 = (Spinner) findViewById(R.id.sp_select_work3);
        sp_select_work4 = (Spinner) findViewById(R.id.sp_select_work4);

        adapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work1);
        adapter2 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work2);
        adapter3 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work3);
        adapter4 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, str_sp_select_work4);

        sp_select_work1.setAdapter(adapter1);
        sp_select_work2.setAdapter(adapter2);
        sp_select_work3.setAdapter(adapter3);
        sp_select_work4.setAdapter(adapter4);
        //初始化
        lv_select_work = (ListView) findViewById(R.id.lv_select_work);
        datalist = new ArrayList<Map<String, Object>>();
        SelectWorkBean selectWork = new SelectWorkBean();
        GridViewData gridViewData_select_work = new GridViewData(datalist);
        //配置数据
        selectWork.setWork_company(new String[]{"广西", "广东", "北京", "上海", "武汉", "重庆", "四川", "天津"});
        selectWork.setWork_land(new String[]{"广西a", "广东a", "北京a", "上海a", "武汉a", "重庆a", "四川a", "天津a"});
        selectWork.setWork_sal(new String[]{"1000", "2000", "3000", "4000", "5000", "6000", "7000", "8000"});
        selectWork.setWork_submit("申请");
        selectWork.setWork_time(new String[]{"11", "22", "33", "44", "55", "66", "77", "88"});
        selectWork.setWork_title(new String[]{"广西t", "广东t", "北京t", "上海t", "武汉t", "重庆t", "四川t", "天津t"});
        selectWork.setWork_welfare(new String[]{"福利好1", "福利好2", "福利好3", "福利好4", "福利好5", "福利好6", "福利好7", "福利好8"});
        //绑定数据
        String[] mapName = new String[]{"title", "work_sal", "work_welfare", "work_company"
                , "work_time", "work_land", "work_submit"};
        int[] controlId = new int[]{R.id.tv_select_work_item_title, R.id.tv_select_work_item_sal
                , R.id.tv_select_work_item_welfare, R.id.tv_select_work_item_company
                , R.id.tv_select_work_item_time, R.id.tv_select_work_item_land, R.id.btn_select_work_item};
        datalist = gridViewData_select_work.getData_SeleceWork(selectWork);
        sadapter = new SimpleAdapter(this, datalist, R.layout.lv_select_work_item, mapName, controlId);

        lv_select_work.setAdapter(sadapter);
    }
}
