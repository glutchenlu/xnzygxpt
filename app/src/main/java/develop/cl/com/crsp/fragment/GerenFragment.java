package develop.cl.com.crsp.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.LoginActivity;
import develop.cl.com.crsp.activity.Person_DataActivity;
import develop.cl.com.crsp.activity.SettingActivity;
import develop.cl.com.crsp.activity.ShowCollectionActivity;
import develop.cl.com.crsp.activity.fabuactivity.FabudetailActivity;
import develop.cl.com.crsp.activity.fabuactivity.UserFabuActivity;
import develop.cl.com.crsp.image.CircleImageView;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.GridViewData;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;


public class GerenFragment extends Fragment implements View.OnClickListener {
    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    private boolean isLogin;
    private View view;
    private Intent mIntent;
    private static final String Tag = "GerenFragment";
    private GridView gv_geren_s;
    private SimpleAdapter sadapter_s;
    private List<Map<String, Object>> datalist_s;

    private GridView gv_geren_x;
    private SimpleAdapter sadapter_x;
    private List<Map<String, Object>> datalist_x;

    private LinearLayout ly_pthoto;
    private ImageView ivSet;
    private CircleImageView ivPhoto;
    private TextView tvName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_geren, container, false);
        initView();
        return view;
    }

    protected void initView() {

        ly_pthoto = (LinearLayout) view.findViewById(R.id.ly_photo);
        ivSet = (ImageView) view.findViewById(R.id.iv_geren_set);
        ivPhoto = (CircleImageView) view.findViewById(R.id.iv_geren_photo);
        tvName = (TextView) view.findViewById(R.id.tv_geren_name);

        ivSet.setOnClickListener(this);
        ly_pthoto.setOnClickListener(this);

        gv_geren_s = (GridView) view.findViewById(R.id.gv_geren_s);
        gv_geren_x = (GridView) view.findViewById(R.id.gv_geren_x);
        datalist_s = new ArrayList<Map<String, Object>>();
        datalist_x = new ArrayList<Map<String, Object>>();
        //数据
        String[] iconName = {"已发帖子", "收藏", "订阅管理",};
        String[] typeName = {"我的求职", "我的招聘", "我的店铺", "我的测试1"};
        String[] typeShow = {"我的求职描述", "我的招聘描述", "我的店铺描述", "我的测试描述1"};
        int[] pic = {R.mipmap.userfabuicon, R.mipmap.usershoucang, R.mipmap.userdingyue};
        int[] pic1 = {R.mipmap.jiaoyi2, R.mipmap.chuzu, R.mipmap.ziliao, R.mipmap.fuwu};

        mySetAdapter2(gv_geren_s, datalist_s, sadapter_s, iconName, pic);

        mySetAdapter3(gv_geren_x, datalist_x, sadapter_x, typeName, typeShow, pic1);
        /**
         * 设置头像
         */
        if (checkLogin()) {
            Log.i(Tag, "checkLogin");
            Basic basic = MySharedPreferences.getBasicData(getActivity());
            Bitmap bitmap = BitmapFactory.decodeFile(basic.getPicture());
            ivPhoto.setImageBitmap(bitmap);
            tvName.setText(basic.getName());
        }
        gv_geren_s.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        if (MyCheckNet.isNetworkAvailable(getActivity())) {
                            sendQueryServer();
                        } else {
                            Toast.makeText(getActivity(), "请检查您的网络连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 1:
                        if (MyCheckNet.isNetworkAvailable(getActivity())) {
                            sendQueryShoucangServer();
                        } else {
                            Toast.makeText(getActivity(), "请检查您的网络连接", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 向服务器请求数据
     */
    protected void sendQueryShoucangServer() {
        String locUrl = ServerInformation.URL + "mycollection/querybyuserid?userid="
                + MySharedPreferences.getUserID(getActivity());
        mQueue = Volley.newRequestQueue(getActivity());
        volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    Toast.makeText(getActivity(), "服务器异常！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //显示提示消息
                    Toast.makeText(getActivity(), jsonMap.get("returnString").toString(), Toast.LENGTH_SHORT).show();
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        mIntent = new Intent(getActivity(), ShowCollectionActivity.class);
                        mIntent.putExtra("returnBean", jsonMap.getString("returnBean"));
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, locUrl, volleyCallback);
    }

    /**
     * 向服务器请求数据
     */
    protected void sendQueryServer() {
        String locUrl = ServerInformation.URL + "user/userfabu?userid="
                + MySharedPreferences.getUserID(getActivity());
        mQueue = Volley.newRequestQueue(getActivity());
        volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    Toast.makeText(getActivity(), "服务器异常！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //显示提示消息
                    Toast.makeText(getActivity(), jsonMap.get("returnString").toString(), Toast.LENGTH_SHORT).show();
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        mIntent = new Intent(getActivity(), UserFabuActivity.class);
                        mIntent.putExtra("returnBean", jsonMap.getString("returnBean"));
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, locUrl, volleyCallback);
    }

    protected void mySetAdapter2(GridView gv, List<Map<String, Object>> list, SimpleAdapter adapter
            , String[] str, int[] pic) {
        list = new ArrayList<Map<String, Object>>();
        GridViewData gridViewData = new GridViewData(list);
        final List<Map<String, Object>> finalList = gridViewData.getData2(pic, str);
        adapter = new SimpleAdapter(getActivity(), finalList
                , R.layout.gv_fenlei_item, new String[]{"pic", "name"}, new int[]{R.id.iv_fenlei_item
                , R.id.tv_fenlei_item});
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view
                    , int position, long id) {
                Log.d(Tag, "点击:" + position);
                Log.d(Tag, "ID:" + id);
                Map<String, Object> locMap = finalList.get(position);
                String typeName = locMap.get("typeName").toString();
                mIntent = new Intent(getActivity(),
                        FabudetailActivity.class);
                mIntent.putExtra("typeName", typeName);
                startActivity(mIntent);
            }
        });
    }

    protected void mySetAdapter3(GridView gv, List<Map<String, Object>> list, SimpleAdapter adapter
            , String[] str, String[] strshow, int[] pic) {
        list = new ArrayList<Map<String, Object>>();
        GridViewData gridViewData = new GridViewData(list);
        final List<Map<String, Object>> finalList = gridViewData.getData3(pic, str, strshow);
        adapter = new SimpleAdapter(getActivity(), finalList
                , R.layout.gv_geren_itemx, new String[]{"pic", "name1", "name2"}
                , new int[]{R.id.iv_geren_item, R.id.tv_geren_item_name, R.id.tv_fenlei_item_miaoshu});
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view
                    , int position, long id) {
                Log.d(Tag, "点击:" + position);
                Log.d(Tag, "ID:" + id);
                Map<String, Object> locMap = finalList.get(position);
                String typeName = locMap.get("typeName").toString();
                mIntent = new Intent(getActivity(),
                        FabudetailActivity.class);
                mIntent.putExtra("typeName", typeName);
                startActivity(mIntent);
            }
        });
    }

    protected boolean checkLogin() {
        isLogin = false;
        if (MySharedPreferences.getLogin(getActivity()).equals("yes")) {
            isLogin = true;
        }
        return isLogin;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(Tag, "onActivityResult");
        if (requestCode == 66) {
            Log.i(Tag, "onActivityResult_OK");
            Basic basic = MySharedPreferences.getBasicData(getActivity());
            Bitmap bitmap = BitmapFactory.decodeFile(basic.getPicture());
            ivPhoto.setImageBitmap(bitmap);
            tvName.setText(basic.getName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_photo:
                if (isLogin) {
                    mIntent = new Intent(getActivity(), Person_DataActivity.class);
                    startActivityForResult(mIntent, 66);
                } else {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                    startActivityForResult(mIntent, 77);
                }
                break;
            case R.id.iv_geren_set:
                if (isLogin) {
                    mIntent = new Intent(getActivity(), SettingActivity.class);
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    startActivityForResult(mIntent, 77);
                }
                break;
        }
    }
}
