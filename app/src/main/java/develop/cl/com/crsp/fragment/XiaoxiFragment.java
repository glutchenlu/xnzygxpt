package develop.cl.com.crsp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.GridViewData;


public class XiaoxiFragment extends Fragment {

    private Button btn_xiaoxi1;
    private Button btn_xiaoxi2;

    private ListView lv_xiaoxi;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_xiaoxi, container, false);
        btn_xiaoxi1 = (Button) view.findViewById(R.id.btn_xiaoxi1);
        btn_xiaoxi2 = (Button) view.findViewById(R.id.btn_xiaoxi2);
        lv_xiaoxi = (ListView) view.findViewById(R.id.lv_xiaoxi);
        datalist = new ArrayList<Map<String, Object>>();
        String[] typeName = {"二手物品", "房屋出租", "学习资料", "找工作", "校内资讯互动", "兼职/全职"
                , "找人才", "本地生活", "二手车"};
        String[] typeShow = {"二手物品测试", "房屋出租测试", "学习资料测试", "找工作测试"
                , "校内资讯互动测", "兼职/全职测试", "找人才测试", "本地生活测试", "二手车测试"};
        String[] time = {"18.18", "18.18", "18.18", "18.18", "18.18", "18.18", "18.18", "18.18", "18.18"};

        GridViewData gridViewData = new GridViewData(datalist);
        sadapter = new SimpleAdapter(getActivity(), gridViewData.getData4(typeName, typeShow, time)
                , R.layout.lv_xiaoxi_item, new String[]{"pic", "name1", "name2", "time"}
                , new int[]{R.id.iv_xiaoxi_item, R.id.tv_xiaoxi_item_name, R.id.tv_xiaoxi_item_miaoshu
                , R.id.tv_fenlei_item_time});
        lv_xiaoxi.setAdapter(sadapter);
        return view;
    }
}
