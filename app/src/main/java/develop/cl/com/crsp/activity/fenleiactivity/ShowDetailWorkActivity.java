package develop.cl.com.crsp.activity.fenleiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.Serializable;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.MyCollection;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.SendMessageActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class ShowDetailWorkActivity extends BaseActivity implements View.OnClickListener {

    private Map<String, Object> mapw;
    private Map<String, Object> mapc;
    private Intent mIntent;
    private static final String Tag = "ShowDetailWorkActivity";
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;

    private TextView tvTitile;
    private TextView tvTime;
    private TextView tvSalary;
    private TextView tvPosition;
    private TextView tvCount;
    private TextView tvEdu;
    private TextView tvExp;
    private TextView tvArea;
    private TextView tvWelfare;
    private TextView tvDetail;
    private TextView tvCompanyName;
    private TextView tvScale;
    private TextView tvNature;
    private TextView tvTel;

    private LinearLayout lyCompany;
    private TextView tvShoucang;
    private Button btnSummit;
    private Button btnCall;
    private String showtpye;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_work);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvTitile = (TextView) this.findViewById(R.id.tv_workdetail_title);
        tvTime = (TextView) this.findViewById(R.id.tv_workdetail_releasetime);
        tvSalary = (TextView) this.findViewById(R.id.tv_workdetail_sal);
        tvPosition = (TextView) this.findViewById(R.id.tv_workdetail_position);
        tvCount = (TextView) this.findViewById(R.id.tv_workdetail_count);
        tvEdu = (TextView) this.findViewById(R.id.tv_workdetail_edu);
        tvExp = (TextView) this.findViewById(R.id.tv_workdetail_exp);
        tvArea = (TextView) this.findViewById(R.id.tv_workdetail_area);
        tvWelfare = (TextView) this.findViewById(R.id.tv_workdetail_welfare);
        tvDetail = (TextView) this.findViewById(R.id.tv_workdetail_detail);
        tvCompanyName = (TextView) this.findViewById(R.id.tv_workdetail_companyname);
        tvScale = (TextView) this.findViewById(R.id.tv_workdetail_scale);
        tvNature = (TextView) this.findViewById(R.id.tv_workdetail_nature);
        tvTel = (TextView) this.findViewById(R.id.tv_workdetail_tel);

        lyCompany = (LinearLayout) this.findViewById(R.id.ly_workdetail_company);
        tvShoucang = (TextView) this.findViewById(R.id.tv_work_shoucang);
        btnSummit = (Button) this.findViewById(R.id.btn_workdetail_summit);
        btnCall = (Button) this.findViewById(R.id.btn_wrokdetail_calluser);
    }

    @Override
    protected void initView() {

        lyCompany.setOnClickListener(this);
        tvShoucang.setOnClickListener(this);
        btnSummit.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        //从Intent获得额外信息
        mIntent = this.getIntent();
        mapw = (Map<String, Object>) mIntent.getSerializableExtra("mapw");
        mapc = (Map<String, Object>) mIntent.getSerializableExtra("mapc");
        showtpye = mapw.get("type").toString();

        tvTitile.setText(mapw.get("title").toString());
        tvTime.setText(mapw.get("release_time").toString());
        tvSalary.setText(mapw.get("salary").toString());
        tvPosition.setText(mapw.get("position").toString());
        tvCount.setText(mapw.get("count").toString());
        tvEdu.setText(mapw.get("education").toString());
        tvExp.setText(mapw.get("experience").toString());
        tvArea.setText(mapw.get("work_area").toString());
        tvWelfare.setText(mapw.get("welfare").toString());
        tvDetail.setText(mapw.get("detail").toString());

        tvCompanyName.setText(mapc.get("companyname").toString());
        tvScale.setText(mapc.get("scale").toString());
        tvNature.setText(mapc.get("nature").toString());
        tvTel.setText(mapc.get("tel").toString());
    }

    protected void addShoucang() {
        mQueue = Volley.newRequestQueue(ShowDetailWorkActivity.this);
        MyCollection mycollection = new MyCollection();
        mycollection.setUserid(MySharedPreferences.getUserID(ShowDetailWorkActivity.this));
        mycollection.setServiceid((Integer) mapw.get("workid"));
        mycollection.setTypeName("兼职/全职");
        mycollection.setType((Integer) mapw.get("type"));
        mycollection.setServiceTitle(mapw.get("title").toString());
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
            case R.id.tv_work_shoucang:
                addShoucang();
                break;
            case R.id.btn_workdetail_summit:
                break;
            case R.id.btn_wrokdetail_calluser:
                mIntent = new Intent(ShowDetailWorkActivity.this, SendMessageActivity.class);
                mIntent.putExtra("touserid", mapw.get("userid").toString());
                mIntent.putExtra("type", showtpye);
                startActivity(mIntent);
                break;
            case R.id.ly_workdetail_company:
                mIntent = new Intent(ShowDetailWorkActivity.this,
                        ShowCompanyActivity.class);
                mIntent.putExtra("mapc", (Serializable) mapc);
                startActivity(mIntent);
                break;
            default:
                break;
        }
    }
}
