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

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Dating;
import develop.cl.com.crsp.JavaBean.XUser;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.util.DFVolley;
import develop.cl.com.crsp.util.MyList;
import develop.cl.com.crsp.util.MySharedPreferences;
import develop.cl.com.crsp.util.ServerInformation;
import develop.cl.com.crsp.util.VolleyCallback;

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
    private TextView find_password;
    private Button register;
    private Button loginBtn;
    private Intent mIntent;
    private VolleyCallback volleyCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
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
        find_password = (TextView) this.findViewById(R.id.find_password);
        getpassword = loginpassword.getText().toString();
        getloginaccount = loginaccount.getText().toString();
    }


    @Override
    protected void initView() {
        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        find_password.setOnClickListener(this);
        isShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(Tag, "开关按钮状态=" + isChecked);
                if (isChecked) {
                    //隐藏
                    loginpassword.setInputType(0x90);
                    //loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //明文显示
                    loginpassword.setInputType(0x81);
                    //loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                Log.i("togglebutton", "" + isChecked);
                //loginpassword.postInvalidate();
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
                            sendFindBasicServer(jsonUser);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            //声明自定义Volley实例
            DFVolley dfv = new DFVolley(volleyCallback);
            String url = ServerInformation.URL + "user/login";
            //调用自定义的Volley函数
            dfv.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, xuser), volleyCallback);
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendFindBasicServer(final XUser jsonUser) {
        Basic basic = new Basic();
        basic.setBasicid(jsonUser.getBasicid());
        String[] str = new String[]{"basicid"};
        VolleyCallback volleyCallbackBasic = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("".equals(result)) {
                    DisPlay("服务器异常！");
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //显示提示消息
//                    DisPlay(jsonMap.get("returnString").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        Basic jsonBasic = JSON.parseObject(jsonMap.get("resultBasic").toString(), Basic.class);
                        MySharedPreferences.setBasic(LoginActivity.this, jsonBasic);
                        sendFindDatingServer(jsonUser);
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
        //声明自定义Volley实例
        DFVolley dfv = new DFVolley(volleyCallbackBasic);
        String url = ServerInformation.URL + "basic/findbyid";
        //调用自定义的Volley函数
        dfv.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, basic), volleyCallbackBasic);
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendFindDatingServer(XUser jsonUser) {
        Dating dating = new Dating();
        dating.setDating(jsonUser.getDatingid());
        String[] str = new String[]{"dating"};
        VolleyCallback volleyCallbackBasic = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("".equals(result)) {
                    DisPlay("服务器异常！");
                    return;
                } else {
                    //解析返回的json
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    //显示提示消息
//                    DisPlay(jsonMap.get("returnString").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        Dating jsonDating = JSON.parseObject(jsonMap.get("resultDating").toString(), Dating.class);
                        MySharedPreferences.setDating(LoginActivity.this, jsonDating);
                        MySharedPreferences.setLogin(LoginActivity.this, "yes");
                        MySharedPreferences.setUserID(LoginActivity.this, getloginaccount);
                        mIntent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //声明自定义Volley实例
        DFVolley dfv = new DFVolley(volleyCallbackBasic);
        String url = ServerInformation.URL + "dating/findbyid";
        //调用自定义的Volley函数
        dfv.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, dating), volleyCallbackBasic);
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
                sendServer();
                break;
            case R.id.register:
                mIntent = new Intent(LoginActivity.this, RegisterBormalActivity.class);
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
