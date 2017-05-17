package develop.cl.com.crsp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.FenleiClassListActivity;
import develop.cl.com.crsp.activity.Select_ClassActivity;
import develop.cl.com.crsp.util.GridViewData;


public class FenleiFragment extends Fragment {
    private View view;
    private static final String Tag = "FenleiFragment";
    private GridView gv_fenlei;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fenlei, container, false);
        initView();
        return view;
    }

    protected void initView() {
        gv_fenlei = (GridView) view.findViewById(R.id.gv_fenlei);
        datalist = new ArrayList<Map<String, Object>>();
        String[] iconName = {"二手交易", "物品出租", "学习资料", "校内快捷服务", "校内资讯互动"
                , "兼职/全职"};
        int[] pic = {R.mipmap.jiaoyi2, R.mipmap.chuzu, R.mipmap.ziliao, R.mipmap.fuwu
                , R.mipmap.hudong, R.mipmap.gongzuo};
        GridViewData gridViewData = new GridViewData(datalist);
        //getActivity()
        sadapter = new SimpleAdapter(getActivity(), gridViewData.getData2(pic, iconName)
                , R.layout.gv_fenlei_item, new String[]{"pic", "name"}
                , new int[]{R.id.iv_fenlei_item, R.id.tv_fenlei_item});
        gv_fenlei.setAdapter(sadapter);
        //设置item点击监听事件
        gv_fenlei.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 跳转到另一个界面查看商品详细信息
                        Map<String, Object> locMap = datalist.get(position);
                        String typeName = locMap.get("name").toString();
                        Log.d(Tag, "点击:" + position);
                        switch (position) {
                            case 0:
                                intent = new Intent(getActivity(), FenleiClassListActivity.class);
                                intent.putExtra("typeName", typeName);
                                intent.putExtra("position", position);
                                startActivity(intent);
                                break;
                            case 1:
                                intent = new Intent(getActivity(), FenleiClassListActivity.class);
                                intent.putExtra("typeName", typeName);
                                intent.putExtra("position", position);
                                startActivity(intent);
                                break;
                            case 2:
                                intent = new Intent(getActivity(), FenleiClassListActivity.class);
                                intent.putExtra("typeName", typeName);
                                intent.putExtra("position", position);
                                startActivity(intent);
                                break;
                            case 3:
                                intent = new Intent(getActivity(), FenleiClassListActivity.class);
                                intent.putExtra("typeName", typeName);
                                intent.putExtra("position", position);
                                startActivity(intent);
                                break;
                            case 4:
                                intent = new Intent(getActivity(), FenleiClassListActivity.class);
                                intent.putExtra("typeName", typeName);
                                intent.putExtra("position", position);
                                startActivity(intent);
                                break;
                            case 5:
                                intent = new Intent(getActivity(), Select_ClassActivity.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
//                    Intent intent = new Intent(getActivity(),
//                            LoginActivity.class);
//                    intent.putExtra("productId", position);//携带参数
//                    startActivity(intent);
                        }
                    }
                }
        );
    }
}
