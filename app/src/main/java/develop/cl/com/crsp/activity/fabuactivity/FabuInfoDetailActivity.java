package develop.cl.com.crsp.activity.fabuactivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Information;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class FabuInfoDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String Tag = "FabuInfoDetailActivity";
    private Intent mIntent;
    private boolean ps;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;

    private EditText etTitle;
    private EditText etTheme;
    private EditText etDetail;
    private TextView tvTop;
    private Button btnSubmit;

    private String getetTitle;
    private String getetTheme;
    private String getetDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_info_detail);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        btnSubmit = (Button) this.findViewById(R.id.btn_fabu_info_submit);
        etTitle = (EditText) this.findViewById(R.id.et_fabu_info_title);
        etTheme = (EditText) this.findViewById(R.id.et_fabu_info_theme);
        etDetail = (EditText) this.findViewById(R.id.et_fabu_info_detail);
        tvTop = (TextView) this.findViewById(R.id.tv_fabu_info_classify);
    }

    @Override
    protected void initView() {
        btnSubmit.setOnClickListener(this);

        mIntent = getIntent();
        tvTop.setText(mIntent.getStringExtra("typeName"));
    }

    /**
     * 检查所输入的发布信息内容
     */
    protected void checkEdit() {
        ps = false;
        getetTitle = etTitle.getText().toString();
        getetTheme = etTheme.getText().toString();
        getetDetail = etDetail.getText().toString();
        if (!getetTitle.equals("") && getetTitle.length() > 0
                && !getetTheme.equals("") && getetTheme.length() > 0
                && !getetDetail.equals("") && getetDetail.length() > 0) {
            ps = true;
        } else if (getetTitle.equals("") && getetTitle.length() <= 0) {
            ps = false;
            DisPlay("标题不能为空！");
            return;
        } else if (getetTheme.equals("") && getetTheme.length() <= 0) {
            ps = false;
            DisPlay("主题不能为空！");
            return;
        } else if (getetDetail.equals("") && getetDetail.length() <= 0) {
            ps = false;
            DisPlay("详细内容不能为空！");
            return;
        }
    }

    /**
     * 添加courier
     */
    protected void sendAddInfoServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(FabuInfoDetailActivity.this);
            Information information = new Information();
            information.setUserid(MySharedPreferences.getUserID(FabuInfoDetailActivity.this));
            information.setClassify(tvTop.getText().toString());
            information.setTitle(getetTitle);
            information.setTheme(getetTheme);
            information.setDetail(getetDetail);
            information.setKeyword(getetTitle + "," + getetTheme);
            information.setSchool(MySharedPreferences.getSchool(FabuInfoDetailActivity.this));
            String[] str = new String[]{"userid", "classify", "title", "theme", "keyword"
                    , "detail", "school"};
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
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                        //根据返回内容执行操作
                        if (jsonMap.get("returnCode").toString().equals("1")) {
                            mIntent = new Intent(FabuInfoDetailActivity.this, MainActivity.class);
                            startActivity(mIntent);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            String url = ServerInformation.URL + "information/add";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, information), volleyCallback);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fabu_info_submit:
                checkEdit();
                if (MyCheckNet.isNetworkAvailable(FabuInfoDetailActivity.this)) {
                    sendAddInfoServer();
                } else {
                    DisPlay("请检查您的网络连接");
                }
                break;
            default:
                break;
        }
    }
}
