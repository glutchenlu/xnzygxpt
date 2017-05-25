package develop.cl.com.crsp.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Dating;
import develop.cl.com.crsp.JavaBean.Setting;
import develop.cl.com.crsp.JavaBean.XUser;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

;

public class LoginActivity extends BaseActivity implements OnClickListener {
    /**
     * 加载提示框
     */
    private ProgressDialog progressDialog;
    private boolean ps;
    private RequestQueue mQueue;
    private static final String Tag = "LoginActivity";
    private ImageView loginLogo;
    private ImageView login_more;
    private EditText loginpassword;
    private EditText loginaccount;
    private ToggleButton isShowPassword;
    private boolean isDisplayflag = false;//是否显示密码
    private String getloginaccount;
    private String getpassword;
    private TextView tvToMain;
    private Button register;
    private Button loginBtn;
    private Intent mIntent;
    private VolleyCallback volleyCallback;
    private TextView etModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        loginLogo = (ImageView) this.findViewById(R.id.logo);
        login_more = (ImageView) this.findViewById(R.id.login_more);
        loginaccount = (EditText) this.findViewById(R.id.loginaccount);
        loginpassword = (EditText) this.findViewById(R.id.loginpassword);
        isShowPassword = (ToggleButton) this.findViewById(R.id.isShowPassword);
        loginBtn = (Button) this.findViewById(R.id.login);
        register = (Button) this.findViewById(R.id.register);
        tvToMain = (TextView) this.findViewById(R.id.login_toMain);
        getpassword = loginpassword.getText().toString();
        getloginaccount = loginaccount.getText().toString();
        etModify = (TextView) this.findViewById(R.id.modify_password);
    }


    @Override
    protected void initView() {
        etModify.setOnClickListener(this);
        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        tvToMain.setOnClickListener(this);
        isShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(Tag, "开关按钮状态=" + isChecked);
                if (isChecked) {
                    //隐藏
                    loginpassword.setInputType(0x90);
                } else {
                    //明文显示
                    loginpassword.setInputType(0x81);
                }
                Log.i("togglebutton", "" + isChecked);
            }
        });
    }

    /**
     * 检查所输入的登录信息内容
     */
    protected void checkLogin() {
        //获取当前所输入的帐号密码信息
        getpassword = loginpassword.getText().toString();
        getloginaccount = loginaccount.getText().toString();
        ps = false;
        if (getloginaccount.equals("") || getloginaccount.length() <= 0) {
            DisPlay("帐号不能为空");
            ps = false;
            return;
        } else {
            ps = true;
        }
        if (getpassword.equals("") || getpassword.length() <= 0) {
            DisPlay("密码不能为空");
            ps = false;
            return;
        } else {
            ps = true;
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(LoginActivity.this);
            //帐号密码都不为空
            showProgressDialog();
            XUser xuser = new XUser();
            xuser.setUserid(getloginaccount);
            xuser.setPassword(getpassword);
            String[] str = new String[]{"userid", "password"};
            //创建回调接口并实例化方法
            volleyCallback = new VolleyCallback() {
                @Override
                //回调内容result
                public void onSuccessResponse(String result) {
                    Log.d("callBack result", result);
                    if ("".equals(result)) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
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
                            XUser jsonUser = JSON.parseObject(jsonMap.get("resultUser").toString(), XUser.class);
                            sendFindServer();
                        } else {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                                progressDialog = null;
                            }
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            String url = ServerInformation.URL + "user/login";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, xuser), volleyCallback);
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendFindServer() {
        XUser user = new XUser();
        user.setUserid(getloginaccount);
        String[] str = new String[]{"userid"};
        VolleyCallback volleyCallbackBasic = new VolleyCallback() {
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
                        JSONObject jsonBean = JSON.parseObject(jsonMap.get("returnBean").toString());
                        Basic jsonBasic = JSON.parseObject(jsonBean.get("basic").toString(), Basic.class);
                        Dating jsonDating = JSON.parseObject(jsonBean.get("dating").toString(), Dating.class);
                        Setting jsonSetting = JSON.parseObject(jsonBean.get("setting").toString(), Setting.class);
                        int count = Integer.parseInt(jsonBean.get("count").toString());
                        MySharedPreferences.setBasic(LoginActivity.this, jsonBasic);
                        MySharedPreferences.setDating(LoginActivity.this, jsonDating);
                        MySharedPreferences.setSetting(LoginActivity.this, jsonSetting);
                        MySharedPreferences.setLogin(LoginActivity.this, "yes");
                        MySharedPreferences.setUserID(LoginActivity.this, getloginaccount);
                        MySharedPreferences.setResumeCount(LoginActivity.this, count);
                        mIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    } else {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "user/findbyid";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, user), volleyCallbackBasic);
    }

    /**
     * 加载进度条
     */
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        Drawable drawable = getResources().getDrawable(R.drawable.loading_animation);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("请稍候，正在努力加载...");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                checkLogin();
                if (MyCheckNet.isNetworkAvailable(LoginActivity.this)) {
                    sendServer();
                } else {
                    DisPlay("请检查您的网络连接");
                }
                break;
            case R.id.register:
                mIntent = new Intent(LoginActivity.this, RegisterBormalActivity.class);
                startActivity(mIntent);
                break;
            case R.id.login_toMain:
                mIntent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(mIntent);
            case R.id.modify_password:
                mIntent = new Intent(LoginActivity.this, ModifyPassWordActivity.class);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
