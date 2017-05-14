package develop.cl.com.crsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.JavaBean.XUser;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.util.DFVolley;
import develop.cl.com.crsp.util.MyList;
import develop.cl.com.crsp.util.ServerInformation;
import develop.cl.com.crsp.util.VolleyCallback;


public class RegisterBormalActivity extends BaseActivity implements View.OnClickListener {
    private RequestQueue mQueue;
    private boolean ps;
    private VolleyCallback volleyCallback;
    private static final String Tag = "RegisterBormalActivity";
    private EditText registerAccount;
    private EditText registerEmail;
    private EditText registerPasswrod;
    private EditText registerRePasswrod;
    private Button registerDo;
    private Intent mIntent;
    String getregisterAccount;
    String getregisterEmail;
    String getregisterPasswrod;
    String getregisterRePasswrod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_normal);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        registerAccount = (EditText) this.findViewById(R.id.et_register_account);
        registerEmail = (EditText) this.findViewById(R.id.et_register_email);
        registerPasswrod = (EditText) this.findViewById(R.id.et_register_password);
        registerRePasswrod = (EditText) this.findViewById(R.id.et_register_repassword);
        registerDo = (Button) this.findViewById(R.id.btn_register_do);
    }

    @Override
    protected void initView() {
        registerAccount.setText("");
        registerEmail.setText("");
        registerPasswrod.setText("");
        registerRePasswrod.setText("");
        registerDo.setOnClickListener(this);
    }

    protected void checkRegister() {
        ps = false;
        getregisterAccount = registerAccount.getText().toString();
        getregisterEmail = registerEmail.getText().toString();
        getregisterPasswrod = registerPasswrod.getText().toString();
        getregisterRePasswrod = registerRePasswrod.getText().toString();
        if (!getregisterAccount.equals("") && getregisterAccount.length() > 0 &&
                !getregisterEmail.equals("") && getregisterEmail.length() > 0 &&
                !getregisterPasswrod.equals("") && getregisterPasswrod.length() > 0 &&
                !getregisterRePasswrod.equals("") && getregisterRePasswrod.length() > 0) {
            if (getregisterPasswrod.equals(getregisterRePasswrod)) {
                ps = true;
            } else {
                DisPlay("两次输入的密码不一致！");
                return;
            }
        } else if (getregisterAccount.equals("") && getregisterAccount.length() <= 0) {
            ps = false;
            DisPlay("帐号不能为空！");
            return;
        } else if (getregisterEmail.equals("") && getregisterEmail.length() <= 0) {
            ps = false;
            DisPlay("邮箱不能为空！");
            return;
        } else if (getregisterPasswrod.equals("") && getregisterPasswrod.length() <= 0) {
            ps = false;
            DisPlay("密码不能为空！");
            return;
        } else if (getregisterRePasswrod.equals("") && getregisterRePasswrod.length() <= 0) {
            ps = false;
            DisPlay("请确认密码！");
            return;
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(RegisterBormalActivity.this);
            XUser xuser = new XUser();
            xuser.setUserid(getregisterAccount);
            xuser.setPassword(getregisterPasswrod);
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
                            mIntent = new Intent(RegisterBormalActivity.this, LoginActivity.class);
                            startActivity(mIntent);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            //声明自定义Volley实例
//            DFVolley dfv = new DFVolley(volleyCallback);
            String url = ServerInformation.URL + "user/register";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, xuser), volleyCallback);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_do:
                checkRegister();
                sendServer();
                break;
            default:
                break;
        }
    }
}
