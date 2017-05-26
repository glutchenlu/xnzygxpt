package develop.cl.com.crsp.activity.fenleiactivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.util.StorageUtils;
import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.MyCollection;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.SendMessageActivity;
import develop.cl.com.crsp.image.CircleImageView;
import develop.cl.com.crsp.myutil.CheckUtil;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.UploadUtil;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class ShowDatailDataActivity extends BaseActivity implements View.OnClickListener {

    private Map<String, Object> map;
    private Intent mIntent;
    private RequestQueue mQueue;
    private VolleyCallback volleyCallback;
    private static final String Tag = "ShowDatailDataActivity";

    private Button btnCall;
    private TextView tvName;
    private CircleImageView ivPic;
    private LinearLayout lyUser;

    private GridView gv_data;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;

    private String nextMap;
    private String showtpye;
    private TextView tvDetail;
    private TextView tvShoucang;
    private TextView tvTitle;
    private TextView tvTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_data);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvShoucang = (TextView) this.findViewById(R.id.tv_data_shoucang);
        tvName = (TextView) this.findViewById(R.id.tv_data_user_name);
        ivPic = (CircleImageView) this.findViewById(R.id.iv_data_user_pic);
        btnCall = (Button) this.findViewById(R.id.btn_datadetail_calluser);
        lyUser = (LinearLayout) findViewById(R.id.ly_data_user);
        gv_data = (GridView) findViewById(R.id.gv_datadetail_data);

        tvTitle = (TextView) this.findViewById(R.id.tv_datadetail_title);
        tvTime = (TextView) this.findViewById(R.id.tv_datadetail_releasetime);
        tvDetail = (TextView) this.findViewById(R.id.tv_datadetail_detal);
    }

    @Override
    protected void initView() {
        lyUser.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        tvShoucang.setOnClickListener(this);
        datalist = new ArrayList<Map<String, Object>>();
        //从Intent获得额外信息
        mIntent = this.getIntent();
        map = (Map<String, Object>) mIntent.getSerializableExtra("map");
        showtpye = map.get("type").toString();
        String[] datanamestr = map.get("data_name").toString().split(",");
        String[] datapathistr = map.get("data_path").toString().split(",");
        strToMap(datanamestr, datapathistr);

        mQueue = Volley.newRequestQueue(ShowDatailDataActivity.this);
        String[] mapName = new String[]{"data_name"};
        int[] controlId = new int[]{R.id.tv_fabu_class_item};
        sadapter = new SimpleAdapter(ShowDatailDataActivity.this, datalist
                , R.layout.gv_fabu_class_item, mapName, controlId);
        gv_data.setAdapter(sadapter);
        /**
         * 请求数据
         */
        String userUrl = ServerInformation.URL + "user/findbyid?userid=" + map.get("userid");
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    DisPlay("服务器异常！");
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        JSONObject beanMap = JSON.parseObject(jsonMap.get("returnBean").toString());
                        nextMap = beanMap.toJSONString();
                        Basic locbasic = JSON.parseObject(beanMap.get("basic").toString(), Basic.class);
                        //卖家名称
                        tvName.setText(locbasic.getName());
                        tvDetail.setText(map.get("detail").toString());
                        tvTitle.setText(map.get("title").toString());
                        tvTime.setText(map.get("release_time").toString());
                        ImageRequest imageRequest = new ImageRequest(locbasic.getPicture(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        //获得卖家头像信息
                                        ivPic.setImageBitmap(response);
                                    }
                                }, 50, 50, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error", error.getMessage());
                            }
                        });
                        mQueue.add(imageRequest);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        /**
         * 请求卖家信息
         */
        DFVolley.NoMReq(mQueue, userUrl, volleyCallback);
        gv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = datalist.get(position).get("data_path").toString();
                String name = datalist.get(position).get("data_name").toString();
                if (CheckUtil.checkLogin(ShowDatailDataActivity.this)) {
                    onDirPicker(name, path);
                } else {
                    Toast.makeText(ShowDatailDataActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onDirPicker(final String name, final String path) {
        FilePicker picker = new FilePicker(this, FilePicker.DIRECTORY);
        picker.setRootPath(StorageUtils.getExternalRootPath() + "Download/");
        picker.setItemHeight(30);
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                Log.d("currentPath", currentPath);
                UploadUtil.downloadFile(path, currentPath, name);
            }
        });
        picker.show();
    }

    protected void strToMap(String[] strname, String[] strpath) {
        for (int i = 0; i < strname.length; i++) {
            Map<String, Object> locmap = new HashMap<String, Object>();
            locmap.put("data_name", strname[i]);
            locmap.put("data_path", strpath[i]);
            datalist.add(locmap);
        }
    }

    protected void addShoucang() {
        mQueue = Volley.newRequestQueue(ShowDatailDataActivity.this);
        MyCollection mycollection = new MyCollection();
        mycollection.setUserid(MySharedPreferences.getUserID(ShowDatailDataActivity.this));
        mycollection.setServiceid((Integer) map.get("learning_dataid"));
        mycollection.setTypeName("学习资料");
        mycollection.setType((Integer) map.get("type"));
        mycollection.setServiceTitle(map.get("detail").toString());
        String[] str = new String[]{"userid", "serviceid", "typeName", "type", "serviceTitle"};
        //创建回调接口并实例化方法
        volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("error".equals(result)) {
                    DisPlay("服务器异常！");
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                    } else if (jsonMap.get("returnCode").toString().equals("2")) {
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "mycollection/add";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, mycollection), volleyCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_data_user:
                mIntent = new Intent(ShowDatailDataActivity.this, ShowUserInfoActivity.class);
                mIntent.putExtra("nextMap", nextMap);
                startActivity(mIntent);
                break;
            case R.id.btn_datadetail_calluser:
                if (CheckUtil.checkLogin(ShowDatailDataActivity.this)) {
                    mIntent = new Intent(ShowDatailDataActivity.this, SendMessageActivity.class);
                    mIntent.putExtra("touserid", map.get("userid").toString());
                    mIntent.putExtra("type", showtpye);
                    startActivity(mIntent);
                } else {
                    Toast.makeText(ShowDatailDataActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_data_shoucang:
                if (CheckUtil.checkLogin(ShowDatailDataActivity.this)) {
                    addShoucang();
                } else {
                    Toast.makeText(ShowDatailDataActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                }
            default:
                break;
        }
    }
}
