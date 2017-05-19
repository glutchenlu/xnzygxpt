package develop.cl.com.crsp.activity.fabuactivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Courier;
import develop.cl.com.crsp.JavaBean.TrainTicket;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class FabuFuwuDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String Tag = "FabuFuwuDetailActivity";
    private Intent mIntent;

    /**
     * 所输入的参数是否符合
     */
    private boolean ps;
    /**
     * 网络请求Volley
     */
    private VolleyCallback volleyCallback;
    /**
     * 存放网络请求的队列
     */
    private RequestQueue mQueue;
    /**
     * 加载提示框
     */
    private ProgressDialog progressDialog;

    private TextView tvStationName;
    private TextView tvReceiveTime;
    private TextView tvArea;

    private Button btnSubmit;
    private EditText etStation;
    private EditText etPrice;
    private EditText etDetail;
    private Spinner spDegree;

    private String gettvReceiveTime;
    private String gettvArea;
    private String getetStation;
    private String getetPrice;
    private String getetDetail;
    private String getspDegree;
    private String typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_fuwu_detail);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvStationName = (TextView) this.findViewById(R.id.tv_fabu_fuwu_station_name);
        tvReceiveTime = (TextView) this.findViewById(R.id.tv_fabu_fuwu_receive);
        tvArea = (TextView) this.findViewById(R.id.tv_fabu_fuwu_area);

        btnSubmit = (Button) this.findViewById(R.id.btn_fabu_fuwu_submit);
        etStation = (EditText) this.findViewById(R.id.et_fabu_fuwu_station);
        etPrice = (EditText) this.findViewById(R.id.et_fabu_fuwu_price);
        etDetail = (EditText) this.findViewById(R.id.et_fabu_fuwu_detail);
        spDegree = (Spinner) this.findViewById(R.id.et_fabu_fuwu_degree);
    }

    @Override
    protected void initView() {
        mIntent = getIntent();
        typeName = mIntent.getStringExtra("typeName");
        if ("快递代领".equals(typeName)) {
            tvStationName.setText("快递商家");
        } else if ("火车票代领".equals(typeName)) {
            tvStationName.setText("火 车 站");
        }
        btnSubmit.setOnClickListener(this);
        spDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getspDegree = FabuFuwuDetailActivity.this.getResources().getStringArray(R.array.fuwu_area)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * 检查所输入的发布信息内容
     */
    protected void checkEdit() {
        ps = false;
        getspDegree = spDegree.getSelectedItem().toString();
        gettvReceiveTime = tvReceiveTime.getText().toString();
        getetStation = etStation.getText().toString();
        getetPrice = etPrice.getText().toString();
        getetDetail = etDetail.getText().toString();
        gettvArea = tvArea.getText().toString();
        if (!getetStation.equals("") && getetStation.length() > 0
                && !gettvReceiveTime.equals("") && gettvReceiveTime.length() > 0
                && !gettvArea.equals("") && gettvArea.length() > 0
                && !getetPrice.equals("") && getetPrice.length() > 0
                && !getetDetail.equals("") && getetDetail.length() > 0) {
            ps = true;
        } else if (getetStation.equals("") && getetStation.length() <= 0) {
            ps = false;
            if ("快递代领".equals(typeName)) {
                DisPlay("快递商名称不能为空！");
            } else if ("快递代领".equals(typeName)) {
                DisPlay("火车站不能为空！");
            }
            return;
        } else if (gettvReceiveTime.equals("") && gettvReceiveTime.length() <= 0) {
            ps = false;
            DisPlay("领取时间不能为空！");
            return;
        } else if (getetPrice.equals("") && getetPrice.length() <= 0) {
            ps = false;
            DisPlay("价格不能为空！");
            return;
        } else if (gettvArea.equals("") && gettvArea.length() <= 0) {
            ps = false;
            DisPlay("交易地点不能为空！");
            return;
        } else if (getetDetail.equals("") && getetDetail.length() <= 0) {
            ps = false;
            DisPlay("描述不能为空！");
            return;
        }
    }

    /**
     * 添加courier
     */
    protected void sendAddCourierServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(FabuFuwuDetailActivity.this);
            Courier courier = new Courier();
            courier.setUserid(MySharedPreferences.getUserID(FabuFuwuDetailActivity.this));
            courier.setReceive_time(gettvReceiveTime);
            courier.setMerchant(getetStation);
            courier.setDetail(getetDetail);
            courier.setPrice(getetDetail);
            courier.setSchool(MySharedPreferences.getSchool(FabuFuwuDetailActivity.this));
            courier.setDegree(StringUtils.left(getspDegree, 1));
            courier.setArea(gettvArea);
            String[] str = new String[]{"userid", "receive_time", "merchant", "detail", "price"
                    , "school", "degree", "area"};
            //创建回调接口并实例化方法
            volleyCallback = new VolleyCallback() {
                @Override
                //回调内容result
                public void onSuccessResponse(String result) {
                    Log.d("callBack result", result);
                    if ("error".equals(result)) {
                        DisPlay("服务器异常！");
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        return;
                    } else {
                        //解析返回的json
                        JSONObject jsonObject = JSON.parseObject(result);
                        JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                        //根据返回内容执行操作
                        if (jsonMap.get("returnCode").toString().equals("1")) {
                            mIntent = new Intent(FabuFuwuDetailActivity.this, MainActivity.class);
                            startActivity(mIntent);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            String url = ServerInformation.URL + "courier/add";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, courier), volleyCallback);
        }
    }

    /**
     * 添加TrainTicket
     */
    protected void sendAddTrainTicketServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(FabuFuwuDetailActivity.this);
            TrainTicket trainTicket = new TrainTicket();
            trainTicket.setUserid(MySharedPreferences.getUserID(FabuFuwuDetailActivity.this));
            trainTicket.setReceive_time(gettvReceiveTime);
            trainTicket.setStation(getetStation);
            trainTicket.setDetail(getetDetail);
            trainTicket.setPrice(getetDetail);
            trainTicket.setSchool(MySharedPreferences.getSchool(FabuFuwuDetailActivity.this));
            trainTicket.setDegree(StringUtils.left(getspDegree, 1));
            trainTicket.setArea(gettvArea);
            String[] str = new String[]{"userid", "receive_time", "station", "detail", "price"
                    , "school", "degree", "area"};
            //创建回调接口并实例化方法
            volleyCallback = new VolleyCallback() {
                @Override
                //回调内容result
                public void onSuccessResponse(String result) {
                    Log.d("callBack result", result);
                    if ("error".equals(result)) {
                        DisPlay("服务器异常！");
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
                        return;
                    } else {
                        //解析返回的json
                        JSONObject jsonObject = JSON.parseObject(result);
                        JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                        //根据返回内容执行操作
                        if (jsonMap.get("returnCode").toString().equals("1")) {
                            mIntent = new Intent(FabuFuwuDetailActivity.this, MainActivity.class);
                            startActivity(mIntent);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            String url = ServerInformation.URL + "trainticket/add";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, trainTicket), volleyCallback);
        }
    }

    /**
     * 加载进度条
     */
    public void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        Drawable drawable = getResources().getDrawable(R.drawable.loading_animation);
        progressDialog.setIndeterminateDrawable(drawable);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("请稍候，正在努力加载...");
        progressDialog.show();
    }

    public void onMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this, DatePicker.MONTH_DAY);
        picker.setGravity(Gravity.CENTER);//弹框居中
        picker.setRangeStart(01, 01);
        picker.setRangeEnd(12, 31);
        picker.setSelectedItem(12, 19);
        picker.setOnDatePickListener(new DatePicker.OnMonthDayPickListener() {
            @Override
            public void onDatePicked(String month, String day) {
                tvReceiveTime.setText(month + "-" + day);
//                showToast(month + "-" + day);
            }
        });
        picker.show();
    }

    public void onAddress2Picker(final View view) {
        try {
            ArrayList<Province> data = new ArrayList<>();
            String json = ConvertUtils.toString(getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            AddressPicker picker = new AddressPicker(this, data);
            picker.setShadowVisible(true);
            picker.setHideProvince(false);
            picker.setHideCounty(false);
            picker.setSelectedItem("广西壮族自治区", "桂林", "雁山区");
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    tvArea.setText(province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName());
                }
            });
            picker.show();
        } catch (Exception e) {
            showToast(LogUtils.toStackTraceString(e));
        }
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fabu_fuwu_submit:
                checkEdit();
                if (MyCheckNet.isNetworkAvailable(FabuFuwuDetailActivity.this)) {
                    if ("快递代领".equals(typeName)) {
                        showProgressDialog();
                        sendAddCourierServer();
                    } else if ("火车票代领".equals(typeName)) {
                        showProgressDialog();
                        sendAddTrainTicketServer();
                    }
                } else {
                    DisPlay("请检查您的网络连接");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
