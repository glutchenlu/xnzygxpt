package develop.cl.com.crsp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Resume;
import develop.cl.com.crsp.R;

public class ShowJianliDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String Tag = "ShowJianliDetailActivity";
    private Intent mIntent;
    private String resultMap;
    private String dotype;
    private String companyname;
//    private Resume preResume;

    private TextView tvName;
    private TextView tvJianliName;
    private TextView tvTel;
    private TextView tvEmail;
    private TextView tvSex;
    private TextView tvEdu;
    private TextView tvExp;
    private TextView tvDate;
    private TextView tvExpSal;
    private TextView tvExpArea;
    private TextView tvExpPosition;
    private TextView tvDescribe;

    private Button btnSubmit;
    private LinearLayout lySubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jianli_detail);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {

        tvName = (TextView) this.findViewById(R.id.tv_jianli_name);
        tvJianliName = (TextView) this.findViewById(R.id.tv_jianli_jianliname);
        tvTel = (TextView) this.findViewById(R.id.tv_jianli_tel);
        tvEmail = (TextView) this.findViewById(R.id.tv_jianli_email);
        tvSex = (TextView) this.findViewById(R.id.tv_jianli_sex);
        tvEdu = (TextView) this.findViewById(R.id.tv_jianli_edu);
        tvExp = (TextView) this.findViewById(R.id.tv_jianli_exp);
        tvDate = (TextView) this.findViewById(R.id.tv_jianli_data);
        tvExpSal = (TextView) this.findViewById(R.id.tv_jianli_sal);
        tvExpArea = (TextView) this.findViewById(R.id.tv_jianli_area);
        tvExpPosition = (TextView) this.findViewById(R.id.tv_jianli_position);
        tvDescribe = (TextView) this.findViewById(R.id.tv_jianli_describe);
        btnSubmit = (Button) this.findViewById(R.id.btn_detail_submit);
        lySubmit = (LinearLayout) this.findViewById(R.id.ly_detail_submit);

    }

    @Override
    protected void initView() {
        mIntent = this.getIntent();
        dotype = mIntent.getStringExtra("dotype");
        lySubmit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        if ("查看".equals(dotype)) {
            lySubmit.setVisibility(View.GONE);
        } else if ("投递查看".equals(dotype)) {
            companyname = mIntent.getStringExtra("companyname");
        }
        resultMap = mIntent.getStringExtra("resultMap");
        JSONObject jsonMap = JSON.parseObject(resultMap);
        Resume preResume = JSON.parseObject(jsonMap.get("resume").toString(), Resume.class);

        tvName.setText(preResume.getName());
        tvJianliName.setText(preResume.getResumename());
        tvTel.setText(preResume.getTel());
        tvEmail.setText(preResume.getEmail());
        tvSex.setText(preResume.getSex());
        tvEdu.setText(preResume.getEducation());
        tvExp.setText(preResume.getWork_year());
        tvDate.setText(preResume.getBirth_year());
        tvExpSal.setText(preResume.getExp_salary());
        tvExpArea.setText(preResume.getExp_area());
        tvExpPosition.setText(preResume.getExp_position());
        tvDescribe.setText(preResume.getRedescribe());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_detail_submit:
                DisPlay("已经向" + companyname + "公司投递简历！");
                break;
        }
    }
}
