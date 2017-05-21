package develop.cl.com.crsp.activity.fenleiactivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.MyCollection;
import develop.cl.com.crsp.JavaBean.WebChar;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class ShowDetailInfoActivity extends BaseActivity implements View.OnClickListener {

    private Map<String, Object> map;
    private Intent mIntent;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;
    private static final String Tag = "ShowDetailInfoActivity";

    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvTheme;
    private TextView tvShoucang;
    private TextView tvDetail;
    private TextView tvET;
    private TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_info);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvTitle = (TextView) this.findViewById(R.id.tv_infodetail_title);
        tvTime = (TextView) this.findViewById(R.id.tv_infodetail_releasetime);
        tvTheme = (TextView) this.findViewById(R.id.tv_infodetail_theme);
        tvShoucang = (TextView) this.findViewById(R.id.tv_info_shoucang);
        tvDetail = (TextView) this.findViewById(R.id.tv_infodetail_detal);
        tvET = (TextView) this.findViewById(R.id.tv_infodetail_et);
        tvShow = (TextView) this.findViewById(R.id.tv_infodetail_show);
    }

    @Override
    protected void initView() {

        tvShoucang.setOnClickListener(this);
        tvET.setOnClickListener(this);
        tvShow.setOnClickListener(this);
        //从Intent获得额外信息
        mIntent = this.getIntent();
        map = (Map<String, Object>) mIntent.getSerializableExtra("map");

        tvTitle.setText(map.get("title").toString());
        tvTime.setText(map.get("release_time").toString());
        tvTheme.setText(map.get("theme").toString());
        tvDetail.setText(map.get("detail").toString());
    }

    private void showInputDialog() {
        final EditText editText = new EditText(ShowDetailInfoActivity.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(ShowDetailInfoActivity.this);
        inputDialog.setTitle("输入评论内容").setView(editText);
        inputDialog.setPositiveButton("评论",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addWebchar(editText.getText().toString());
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        inputDialog.show();
    }

    protected void addWebchar(String etStr) {
        mQueue = Volley.newRequestQueue(ShowDetailInfoActivity.this);
        WebChar webchar = new WebChar();
        webchar.setInformationid(Integer.parseInt(map.get("informationid").toString()));
        webchar.setUserid(MySharedPreferences.getUserID(ShowDetailInfoActivity.this));
        webchar.setContent(etStr);
        String[] str = new String[]{"informationid", "userid", "content"};
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
        String url = ServerInformation.URL + "webchar/add";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, webchar), volleyCallback);
    }

    protected void addShoucang() {
        mQueue = Volley.newRequestQueue(ShowDetailInfoActivity.this);
        MyCollection mycollection = new MyCollection();
        mycollection.setUserid(MySharedPreferences.getUserID(ShowDetailInfoActivity.this));
        mycollection.setServiceid((Integer) map.get("informationid"));
        mycollection.setTypeName("校内资讯互动");
        mycollection.setType((Integer) map.get("type"));
        mycollection.setServiceTitle(map.get("title").toString());
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

    protected void showwebchar() {
        mQueue = Volley.newRequestQueue(ShowDetailInfoActivity.this);
        //创建回调接口并实例化方法
        VolleyCallback delVolleyCallback = new VolleyCallback() {
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
                        mIntent = new Intent(ShowDetailInfoActivity.this, WebCharListActivity.class);
                        mIntent.putExtra("resultMap", jsonObject.get("resultMap").toString());
                        mIntent.putExtra("informationid", (Integer) map.get("informationid"));
                        mIntent.putExtra("title", map.get("title").toString());
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "webchar/querybyinfo?informationid=" + map.get("informationid");
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, url, delVolleyCallback);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info_shoucang:
                addShoucang();
                break;
            case R.id.tv_infodetail_et:
                showInputDialog();
                break;
            case R.id.tv_infodetail_show:
                showwebchar();
                break;
            default:
                break;
        }
    }
}
