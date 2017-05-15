package develop.cl.com.crsp.activity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.R;

public class UserFabuActivity extends BaseActivity {
    private static final String Tag = "UserFabuActivity";
    private Intent mIntent;
    private String resultBean;

    private ListView lvUserFabu;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fabu);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }
}
