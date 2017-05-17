package develop.cl.com.crsp.activity.fenleiactivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.R;

public class ShowCompanyActivity extends BaseActivity implements View.OnClickListener {
    private Map<String, Object> mapc;
    private Intent mIntent;
    private static final String Tag = "ShowDetailWorkActivity";

    private TextView tvCompanyName;
    private TextView tvScale;
    private TextView tvNature;
    private TextView tvArea;
    private TextView tvDetail;

    private Button btnSummit;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_company);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvCompanyName = (TextView) this.findViewById(R.id.tv_companydetail_name);
        tvScale = (TextView) this.findViewById(R.id.tv_companydetail_scale);
        tvNature = (TextView) this.findViewById(R.id.tv_workdetail_companynuture);
        tvArea = (TextView) this.findViewById(R.id.tv_companydetail_area);
        tvDetail = (TextView) this.findViewById(R.id.tv_companydetail_detail);

        btnSummit = (Button) this.findViewById(R.id.btn_companydetail_summit);
        btnCall = (Button) this.findViewById(R.id.btn_companydetail_calluser);
    }

    @Override
    protected void initView() {

        //从Intent获得额外信息
        mIntent = this.getIntent();
        mapc = (Map<String, Object>) mIntent.getSerializableExtra("mapc");

        btnSummit.setOnClickListener(this);
        btnCall.setOnClickListener(this);

        tvCompanyName.setText(mapc.get("companyname").toString());
        tvScale.setText(mapc.get("scale").toString());
        tvNature.setText(mapc.get("nature").toString());
        tvArea.setText(mapc.get("companyarea").toString());
        tvDetail.setText(mapc.get("detail").toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_workdetail_summit:
                break;
            case R.id.btn_wrokdetail_calluser:
                break;
        }
    }
}
