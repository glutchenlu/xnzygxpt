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

import java.util.HashMap;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class ModifyPassWordActivity extends BaseActivity implements View.OnClickListener {
    private RequestQueue mQueue;
    private boolean ps;
    private VolleyCallback volleyCallback;
    private static final String Tag = "ModifyPassWordActivity";
    private Intent mIntent;
    private Button btnDo;
    private EditText etAccount;
    private EditText etPassword;
    private EditText etNewpassword;
    private EditText etReNewPassword;

    private String getetAccount;
    private String getetPassword;
    private String getetNewpassword;
    private String getetReNewPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_password);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {

        etAccount = (EditText) this.findViewById(R.id.et_modify_account);
        etPassword = (EditText) this.findViewById(R.id.et_modify_password);
        etNewpassword = (EditText) this.findViewById(R.id.et_modify_newpassword);
        etReNewPassword = (EditText) this.findViewById(R.id.et_modify_re_newpassword);
        btnDo = (Button) this.findViewById(R.id.btn_modify_do);
    }

    @Override
    protected void initView() {
        btnDo.setOnClickListener(this);
        etAccount.setText("");
        etPassword.setText("");
        etNewpassword.setText("");
        etReNewPassword.setText("");
    }

    protected void check() {
        ps = false;
        getetAccount = etAccount.getText().toString();
        getetPassword = etPassword.getText().toString();
        getetNewpassword = etNewpassword.getText().toString();
        getetReNewPassword = etReNewPassword.getText().toString();
        if (!getetAccount.equals("") && getetAccount.length() > 0 &&
                !getetPassword.equals("") && getetPassword.length() > 0 &&
                !getetNewpassword.equals("") && getetNewpassword.length() > 0 &&
                !getetReNewPassword.equals("") && getetReNewPassword.length() > 0) {
            if (getetNewpassword.equals(getetReNewPassword)) {
                ps = true;
            } else {
                DisPlay("两次输入的密码不一致！");
                return;
            }
        } else if (getetAccount.equals("") && getetAccount.length() <= 0) {
            ps = false;
            DisPlay("帐号不能为空！");
            return;
        } else if (getetPassword.equals("") && getetPassword.length() <= 0) {
            ps = false;
            DisPlay("旧密码不能为空！");
            return;
        } else if (getetNewpassword.equals("") && getetNewpassword.length() <= 0) {
            ps = false;
            DisPlay("新密码不能为空！");
            return;
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(ModifyPassWordActivity.this);
            Map<String, String> hmap = new HashMap<String, String>();
            hmap.put("userid", getetAccount);
            hmap.put("newpassword", getetNewpassword);
            hmap.put("password", getetPassword);
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
                            JSONObject userMap = JSON.parseObject(jsonMap.getString("returnUser"));
                            mIntent = new Intent(ModifyPassWordActivity.this, LoginActivity.class);
                            startActivity(mIntent);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            String url = ServerInformation.URL + "user/modify";
            //调用自定义的Volley函数
            DFVolley.NoMPots(mQueue, url, volleyCallback, hmap);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_do:
                if (MyCheckNet.isNetworkAvailable(ModifyPassWordActivity.this)) {
                    check();
                    sendServer();
                } else {
                    DisPlay("请检查您的网络连接");
                }
                break;
            default:
                break;
        }
    }
}
