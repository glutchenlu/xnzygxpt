package develop.cl.com.crsp.activity.fenleiactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Dating;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.image.CircleImageView;

public class ShowUserInfoActivity extends BaseActivity implements View.OnClickListener {
    private String nextMap;
    private static final String Tag = "ShowUserInfoActivity";
    private Intent mIntent;
    private RequestQueue mQueue;

    private CircleImageView ivPic;
    private TextView tvName;
    private TextView tvSchool;
    private TextView tvSex;
    private TextView tvEmail;
    private TextView tvHome;
    //行业
    private TextView tvProfessional;
    //活动区域
    private TextView tvBusiness;
    private TextView tvCredit;

    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_user);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        ivPic = (CircleImageView) this.findViewById(R.id.iv_user_pic);
        tvName = (TextView) this.findViewById(R.id.tv_user_name);
        tvSchool = (TextView) this.findViewById(R.id.tv_user_school);
        tvSex = (TextView) this.findViewById(R.id.tv_user_sex);
        tvEmail = (TextView) this.findViewById(R.id.tv_user_email);
        tvHome = (TextView) this.findViewById(R.id.tv_user_home);
        tvProfessional = (TextView) this.findViewById(R.id.tv_user_professional);
        tvBusiness = (TextView) this.findViewById(R.id.tv_user_business);
        tvCredit = (TextView) this.findViewById(R.id.tv_user_credit);
        btnCall = (Button) this.findViewById(R.id.btn_user_call);

    }

    @Override
    protected void initView() {

        btnCall.setOnClickListener(this);
        //从Intent获得额外信息
        mIntent = this.getIntent();
        nextMap = mIntent.getStringExtra("nextMap");
        JSONObject beanMap = JSON.parseObject(nextMap);
        Basic locbasic = JSON.parseObject(beanMap.get("basic").toString(), Basic.class);
        Dating locdating = JSON.parseObject(beanMap.get("dating").toString(), Dating.class);

        tvName.setText(locbasic.getName());
        tvSchool.setText(locbasic.getSchool());
        tvSex.setText(locbasic.getSex());
        tvEmail.setText(locbasic.getEmail());
        tvHome.setText(locdating.getHometown());
        tvProfessional.setText(locdating.getProfessional());
        tvBusiness.setText(locdating.getBusiness());
        tvCredit.setText(locbasic.getCredit() + "级");
        mQueue = Volley.newRequestQueue(ShowUserInfoActivity.this);
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

    @Override
    public void onClick(View v) {

    }
}
