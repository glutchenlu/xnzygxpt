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

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Message;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class SendMessageActivity extends BaseActivity implements View.OnClickListener {

    private static final String Tag = "SendMessageActivity";
    private RequestQueue mQueue;
    private Intent mIntent;

    private Button btnSend;
    private EditText etMessage;

    private String getetMessage;
    //    private String nextMap;
    private String type;
    private String touserid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_message);
        findViewById();
        initView();
        Log.d(Tag, "onCreate");
    }

    @Override
    protected void findViewById() {
        btnSend = (Button) this.findViewById(R.id.btn_send_message);
        etMessage = (EditText) this.findViewById(R.id.et_send_message);
    }

    @Override
    protected void initView() {
        btnSend.setOnClickListener(this);
        //从Intent获得额外信息
        mIntent = this.getIntent();
//        nextMap = mIntent.getStringExtra("nextMap");
        type = mIntent.getStringExtra("type");
//        JSONObject beanMap = JSON.parseObject(nextMap);
//        XUser locuser = JSON.parseObject(beanMap.get("user").toString(), XUser.class);
        touserid = mIntent.getStringExtra("touserid");
    }

    protected void sendMessage() {
        getetMessage = etMessage.getText().toString();
        if (getetMessage.equals("") && getetMessage.length() <= 0) {
            DisPlay("发送内容不能为空！");
            return;
        } else {
            mQueue = Volley.newRequestQueue(SendMessageActivity.this);
            Message message = new Message();
            message.setUserid(touserid);
            message.setMessage(getetMessage);
            message.setType(Integer.parseInt(type));
            message.setFromuserid(MySharedPreferences.getUserID(SendMessageActivity.this));
            String[] str = new String[]{"userid", "message", "type", "fromuserid"};
            String url = ServerInformation.URL + "message/sendmessage";
            VolleyCallback volleyCallback = new VolleyCallback() {
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
                            DisPlay(jsonMap.get("returnString").toString());
                            etMessage.setText("");
                        } else if (jsonMap.get("returnCode").toString().equals("2")) {
                            DisPlay(jsonMap.get("returnString").toString());
                            etMessage.setText("");
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, message), volleyCallback);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send_message) {
            sendMessage();
        }
    }
}
