package develop.cl.com.crsp.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.adapter.MainFragmentAdapter;
import develop.cl.com.crsp.fragment.FabuFragment;
import develop.cl.com.crsp.fragment.FenleiFragment;
import develop.cl.com.crsp.fragment.FujinFragment;
import develop.cl.com.crsp.fragment.GerenFragment;
import develop.cl.com.crsp.fragment.XiaoxiFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private List<Fragment> fragList;
    private ViewPager vp_main;
    private Button ibtn_fenlei;
    private Button ibtn_fujin;
    private Button ibtn_fabu;
    private Button ibtn_xiaoxi;
    private Button ibtn_geren;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymain);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        vp_main = (ViewPager) findViewById(R.id.pager);
        ibtn_fenlei = (Button) findViewById(R.id.ibtn_fenlei);
        ibtn_fujin = (Button) findViewById(R.id.ibtn_fujin);
        ibtn_fabu = (Button) findViewById(R.id.ibtn_fabu);
        ibtn_xiaoxi = (Button) findViewById(R.id.ibtn_xiaoxi);
        ibtn_geren = (Button) findViewById(R.id.ibtn_geren);
    }

    @Override
    protected void initView() {
        ibtn_fenlei.setOnClickListener(this);
        ibtn_fujin.setOnClickListener(this);
        ibtn_fabu.setOnClickListener(this);
        ibtn_xiaoxi.setOnClickListener(this);
        ibtn_geren.setOnClickListener(this);
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 2) {
                    position = vp_main.getCurrentItem();
                    switch (position) {
                        case 0:
                            Log.d("tag", "ibtn_fenlei huadong");
                            break;
                        case 1:
                            Log.d("tag", "ibtn_fujin huadong");
                            break;
                        case 2:
                            Log.d("tag", "ibtn_fabu huadong");
                            break;
                        case 3:
                            Log.d("tag", "ibtn_xiaoxi huadong");
                            break;
                        case 4:
                            Log.d("tag", "ibtn_geren huadong");
                            break;
                    }
                }
            }
        });
        fragList = getData();
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragList);
        vp_main.setAdapter(adapter);
    }

    private List<Fragment> getData() {
        fragList = new ArrayList<Fragment>();
        FenleiFragment fenlei = new FenleiFragment();
        FujinFragment fujin = new FujinFragment();
        FabuFragment fabu = new FabuFragment();
        XiaoxiFragment xiaoxi = new XiaoxiFragment();
        GerenFragment geren = new GerenFragment();
        fragList.add(fenlei);
        fragList.add(fujin);
        fragList.add(fabu);
        fragList.add(xiaoxi);
        fragList.add(geren);
        return fragList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibtn_fabu:
                position = 2;
                Log.d("tag", "ibtn_fabu click");
                vp_main.setCurrentItem(position);
                break;
            case R.id.ibtn_fujin:
                position = 1;
                Log.d("tag", "ibtn_fujin click");
                vp_main.setCurrentItem(position);
                break;
            case R.id.ibtn_xiaoxi:
                position = 3;
                Log.d("tag", "ibtn_xiaoxi click");
                vp_main.setCurrentItem(position);
                break;
            case R.id.ibtn_geren:
                position = 4;
                Log.d("tag", "ibtn_geren click");
                vp_main.setCurrentItem(position);
                break;
            case R.id.ibtn_fenlei:
                position = 0;
                Log.d("tag", "ibtn_fenlei click");
                vp_main.setCurrentItem(position);
                break;
            default:
                break;
        }
    }
}
