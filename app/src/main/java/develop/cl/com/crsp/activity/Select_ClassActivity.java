package develop.cl.com.crsp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.fragment.GridViewData;


public class Select_ClassActivity extends AppCompatActivity {

    private GridView gv_select_class_s;
    private SimpleAdapter sadapter_s;
    private List<Map<String, Object>> datalist_s;

    private GridView gv_select_class_x;
    private SimpleAdapter sadapter_x;
    private List<Map<String, Object>> datalist_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_class);

        gv_select_class_s = (GridView) findViewById(R.id.gv_select_class_s);
        gv_select_class_x = (GridView) findViewById(R.id.gv_select_class_x);
        datalist_s = new ArrayList<Map<String, Object>>();
        datalist_x = new ArrayList<Map<String, Object>>();

        //数据
        String[] iconName = {"附近工作", "包吃包住", "放心企业", "五险一金", "周末双休"
                , "今日高新", "地铁沿线", "我的求职"};
        String[] typeName = {"技工", "销售", "普工", "司机", "餐饮", "服务员", "导购", "家政"
                , "快递员", "保安", "客服", "淘宝客服", "教育培训", "人力资源"};
        int[] pic1 = {R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2
                , R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2};
        int[] pic2 = {R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2
                , R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2
                , R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2, R.mipmap.jiaoyi2
                , R.mipmap.jiaoyi2, R.mipmap.jiaoyi2};

        //构造自定义的获取数据的类
        GridViewData gridViewData2 = new GridViewData(datalist_s);
        GridViewData gridViewData1 = new GridViewData(datalist_x);

        sadapter_s = new SimpleAdapter(this, gridViewData2.getData2(pic1, iconName)
                , R.layout.gv_fenlei_item, new String[]{"pic", "name"}
                , new int[]{R.id.iv_fenlei_item, R.id.tv_fenlei_item});
        sadapter_x = new SimpleAdapter(this, gridViewData1.getData2(pic2, typeName)
                , R.layout.gv_btn_item, new String[]{"name"}, new int[]{R.id.btn_select_class_gv});

        gv_select_class_s.setAdapter(sadapter_s);
        gv_select_class_x.setAdapter(sadapter_x);
    }
}