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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.fabuactivity.FabuClassActivity;
import develop.cl.com.crsp.activity.fabuactivity.FabuClassListActivity;
import develop.cl.com.crsp.activity.LoginActivity;
import develop.cl.com.crsp.myutil.GridViewData;
import develop.cl.com.crsp.myutil.MySharedPreferences;


public class FabuFragment extends Fragment {
    private static final String Tag = "FabuFragment";
    private View view;
    private GridView gv_fabu;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_fabu, container, false);
        initView();
        return view;
    }

    protected void initView() {
        gv_fabu = (GridView) view.findViewById(R.id.gv_fabu);
        datalist = new ArrayList<Map<String, Object>>();
        String[] typeName = {"二手交易", "物品出租", "学习资料", "校内快捷服务", "校内资讯互动"
                , "兼职/全职"};
        String[] typeShow = {"手机/自行车/笔记本", "自行车/书籍/户外用品", "知识/学习内容"
                , "快递代领/火车票代领", "消息互动", "兼职/全职工作"};
        int[] pic = {R.mipmap.jiaoyi2, R.mipmap.chuzu, R.mipmap.ziliao, R.mipmap.fuwu
                , R.mipmap.hudong, R.mipmap.gongzuo};
        GridViewData gridViewData = new GridViewData(datalist);
        //getActivity()
        sadapter = new SimpleAdapter(getActivity(), gridViewData.getData3(pic, typeName, typeShow)
                , R.layout.gv_fabu_item, new String[]{"pic", "name1", "name2"}
                , new int[]{R.id.iv_fabu_item, R.id.tv_fabu_items, R.id.tv_fabu_itemx});
        gv_fabu.setAdapter(sadapter);
        gv_fabu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkLogin()) {
                    Map<String, Object> locMap = datalist.get(position);
                    String typeName = locMap.get("name1").toString();
                    Log.d(Tag, "点击:" + position);
                    switch (position) {
                        case 0:
                            intent = new Intent(getActivity(), FabuClassListActivity.class);
                            intent.putExtra("typeName", typeName);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        case 1:
                            intent = new Intent(getActivity(), FabuClassListActivity.class);
                            intent.putExtra("typeName", typeName);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        case 2:
                            intent = new Intent(getActivity(), FabuClassListActivity.class);
                            intent.putExtra("typeName", typeName);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        case 3:
                            intent = new Intent(getActivity(), FabuClassListActivity.class);
                            intent.putExtra("typeName", typeName);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        case 4:
                            intent = new Intent(getActivity(), FabuClassListActivity.class);
                            intent.putExtra("typeName", typeName);
                            intent.putExtra("position", position);
                            startActivity(intent);
                            break;
                        case 5:
                            intent = new Intent(getActivity(), FabuClassActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            break;
                    }
                } else {
                    // 跳转到另一个界面查看商品详细信息
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(),
                            LoginActivity.class);
//                    intent.putExtra("productId", position);//携带参数
                    startActivity(intent);
                }
            }
        });
    }

    protected boolean checkLogin() {
        boolean isLogin = false;
        if (MySharedPreferences.getLogin(getActivity()).equals("yes")) {
            isLogin = true;
        }
        return isLogin;
    }
}
