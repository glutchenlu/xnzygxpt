package develop.cl.com.crsp.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import develop.cl.com.crsp.R;

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        search = (SearchView) this.findViewById(R.id.sv_select_class_fabu);
    }

    @Override
    protected void initView() {
        search.setSubmitButtonEnabled(true);
        search.onActionViewExpanded();
    }

    @Override
    public void onClick(View v) {

    }

}
