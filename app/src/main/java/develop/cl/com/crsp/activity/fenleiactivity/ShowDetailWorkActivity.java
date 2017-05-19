package develop.cl.com.crsp.activity.fenleiactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.SendMessageActivity;

public class ShowDetailWorkActivity extends BaseActivity implements View.OnClickListener {

    private Map<String, Object> mapw;
    private Map<String, Object> mapc;
    private Intent mIntent;
    private static final String Tag = "ShowDetailWorkActivity";

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_work_shoucang:
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
