package develop.cl.com.crsp.activity.fabuactivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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

import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Company;
import develop.cl.com.crsp.JavaBean.Work;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;


public class FabudetailActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvTop;
    private Button btnBack;
    private Button btnSubmit;
    private EditText etTitle;
    private EditText etPosition;
    private Spinner spSal;
    private Spinner spEducation;
    private Spinner spExperience;
    private Spinner spNature;
    private EditText etCount;
    private TextView etArea;
    private EditText etArea1;
    private EditText etTel;
    private EditText etWelfare;
    private EditText etDetail;
    private EditText etCompanyName;
    private TextView etClass;
    private EditText etCompanyDetail;

    private String getetTitle;
    private String getetPosition;
    private String getspEducation;
    private String getspExperience;
    private String getspSal;
    private String getspNature;
    private String getetCount;
    private String getetArea;
    private String getetArea1;
    private String getetTel;
    private String getetWelfare;
    private String getetDetail;
    private String getetCompanyName;
    private String getetClass;
    private String getetCompanyDetail;

    private boolean ps;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;
    private static final String Tag = "FabudetailActivity";
    private Intent mIntent;

    /**
     * 加载提示框
     */
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_detail);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvTop = (TextView) this.findViewById(R.id.tv_fabu_work_classify);
        btnBack = (Button) this.findViewById(R.id.btn_fabu_work_detail_back);
        btnSubmit = (Button) this.findViewById(R.id.btn_fabu_submit);
        etTitle = (EditText) this.findViewById(R.id.et_fabu_work_title);
        etPosition = (EditText) this.findViewById(R.id.et_fabu_work_position);
        spSal = (Spinner) this.findViewById(R.id.sp_fabu_work_salary);
        spNature = (Spinner) this.findViewById(R.id.sp_fabu_work_nature);
        spEducation = (Spinner) this.findViewById(R.id.sp_fabu_work_education);
        spExperience = (Spinner) this.findViewById(R.id.sp_fabu_work_experience);
        etCount = (EditText) this.findViewById(R.id.et_fabu_work_count);
        etArea = (TextView) this.findViewById(R.id.et_fabu_work_area);
        etArea1 = (EditText) this.findViewById(R.id.et_fabu_work_area1);
        etTel = (EditText) this.findViewById(R.id.et_fabu_work_tel);
        etWelfare = (EditText) this.findViewById(R.id.et_fabu_work_welfare);
        etDetail = (EditText) this.findViewById(R.id.et_fabu_work_detail);
        etCompanyName = (EditText) this.findViewById(R.id.et_fabu_work_companyname);
        etClass = (TextView) this.findViewById(R.id.et_fabu_work_classify);
        etCompanyDetail = (EditText) this.findViewById(R.id.et_fabu_work_companydetail);
    }

    @Override
    protected void initView() {
        btnSubmit.setOnClickListener(this);
        mIntent = this.getIntent();
        Log.d(Tag, mIntent.getStringExtra("typeName"));
        //从Intent获得额外信息，设置为TextView的文本
        tvTop.setText(mIntent.getStringExtra("typeName"));
        etClass.setText(mIntent.getStringExtra("typeName"));
        spSal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getspSal = FabudetailActivity.this.getResources().getStringArray(R.array.sal)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spNature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getspNature = FabudetailActivity.this.getResources().getStringArray(R.array.work_nature)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spEducation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getspEducation = FabudetailActivity.this.getResources().getStringArray(R.array.fabu_work_education)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spExperience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getspExperience = FabudetailActivity.this.getResources().getStringArray(R.array.fabu_work_experience)[position];
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
        getspNature = spNature.getSelectedItem().toString();
        getspSal = spSal.getSelectedItem().toString();
        getspEducation = spEducation.getSelectedItem().toString();
        getspExperience = spExperience.getSelectedItem().toString();
        getetTitle = etTitle.getText().toString();
        getetPosition = etPosition.getText().toString();
        getetCount = etCount.getText().toString();
        getetArea = etArea.getText().toString();
        getetArea1 = etArea1.getText().toString();
        getetTel = etTel.getText().toString();
        getetWelfare = etWelfare.getText().toString();
        getetDetail = etDetail.getText().toString();
        getetCompanyName = etCompanyName.getText().toString();
        getetClass = etClass.getText().toString();
        getetCompanyDetail = etCompanyDetail.getText().toString();

        if (!getetTitle.equals("") && getetTitle.length() > 0
                && !getetPosition.equals("") && getetPosition.length() > 0
                && !getetCount.equals("") && getetCount.length() > 0
                && !getetArea.equals("") && getetArea.length() > 0
                && !getetArea1.equals("") && getetArea1.length() > 0
                && !getetTel.equals("") && getetTel.length() > 0
                && !getetWelfare.equals("") && getetWelfare.length() > 0
                && !getetDetail.equals("") && getetDetail.length() > 0
                && !getetCompanyName.equals("") && getetCompanyName.length() > 0
                && !getetClass.equals("") && getetClass.length() > 0
                && !getetCompanyDetail.equals("") && getetCompanyDetail.length() > 0) {
            ps = true;
        } else if (getetTitle.equals("") && getetTitle.length() <= 0) {
            ps = false;
            DisPlay("标题不能为空！");
            return;
        } else if (getetPosition.equals("") && getetPosition.length() <= 0) {
            ps = false;
            DisPlay("职位名称不能为空！");
            return;
        } else if (getetCount.equals("") && getetCount.length() <= 0) {
            ps = false;
            DisPlay("招聘人数不能为空！");
            return;
        } else if (getetArea.equals("") && getetArea.length() <= 0) {
            ps = false;
            DisPlay("工作区域不能为空！");
            return;
        } else if (getetArea1.equals("") && getetArea1.length() <= 0) {
            ps = false;
            DisPlay("详细地址不能为空！");
            return;
        } else if (getetTel.equals("") && getetTel.length() <= 0) {
            ps = false;
            DisPlay("联系电话不能为空！");
            return;
        } else if (getetWelfare.equals("") && getetWelfare.length() <= 0) {
            ps = false;
            DisPlay("福利不能为空！");
            return;
        } else if (getetDetail.equals("") && getetDetail.length() <= 0) {
            ps = false;
            DisPlay("职位描述不能为空！");
            return;
        } else if (getetCompanyName.equals("") && getetCompanyName.length() <= 0) {
            ps = false;
            DisPlay("公司名称不能为空！");
            return;
        } else if (getetCompanyDetail.equals("") && getetCompanyDetail.length() <= 0) {
            ps = false;
            DisPlay("公司简介不能为空！");
            return;
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendCheckServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(FabudetailActivity.this);
            Company company = new Company();
            company.setCompanyname(getetCompanyName);
            String[] str = new String[]{"companyname"};
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
                        if (jsonMap.get("returnCode").toString().equals("0")) {
//                            DisPlay(jsonMap.get("returnString").toString());
                            showNormalDialog("是否进行公司信息注册？", jsonMap.get("returnString").toString());
                        }
                        //根据返回内容执行操作
                        if (jsonMap.get("returnCode").toString().equals("1")) {
                            sendAddServer();
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            //声明自定义Volley实例
//            DFVolley dfv = new DFVolley(volleyCallback);
            String url = ServerInformation.URL + "company/checkCompany";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(0, mQueue, url, MyList.strList(str, company), volleyCallback);
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendAddServer() {
        mQueue = Volley.newRequestQueue(FabudetailActivity.this);
        Work work = new Work();
        work.setUserid(MySharedPreferences.getUserID(FabudetailActivity.this));
        work.setClassify(getspNature);
        work.setDetail(getetDetail);
        work.setCount(getetCount);
        work.setEducation(getspEducation);
        work.setIndustry(getetClass);
        work.setExperience(getspExperience);
        work.setKeyword(getetTitle + "," + getetCompanyName + "," + getspNature + "," + getetPosition);
        work.setPosition(getetPosition);
        work.setSchool(MySharedPreferences.getSchool(FabudetailActivity.this));
        work.setSalary(getspSal);
        work.setSource(getetCompanyName);
        work.setWelfare(getetWelfare);
        work.setTel(getetTel.trim());
        work.setWork_area(getetArea + "-" + getetArea1);
        work.setTitle(getetTitle);
        String[] str = new String[]{"userid", "classify", "keyword", "title", "work_area", "welfare"
                , "detail", "salary", "education", "source", "experience", "position", "count"
                , "tel", "industry", "school"};
        //创建回调接口并实例化方法
        this.showProgressDialog();
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
                        mIntent = new Intent(FabudetailActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        //声明自定义Volley实例
//        DFVolley dfv = new DFVolley(volleyCallback);
        String url = ServerInformation.URL + "work/addWork";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, work), volleyCallback);
    }

    protected void showNormalDialog(String title, String message) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(FabudetailActivity.this);
        normalDialog.setIcon(R.drawable.ic_launcher);
        normalDialog.setTitle(title);
        normalDialog.setMessage(message);
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mIntent = new Intent(FabudetailActivity.this, AddCompanyActivity.class);
                        startActivity(mIntent);
                    }
                });
        normalDialog.setNegativeButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        // 显示
        normalDialog.show();
    }

    public void onAddress4Picker(final View view) {
        try {
            ArrayList<Province> data = new ArrayList<>();
            String json = ConvertUtils.toString(getAssets().open("city.json"));
            data.addAll(JSON.parseArray(json, Province.class));
            AddressPicker picker = new AddressPicker(this, data);
            picker.setShadowVisible(true);
            picker.setHideProvince(false);
            picker.setHideCounty(true);
            picker.setSelectedItem("广西壮族自治区", "桂林", "雁山区");
            picker.setOnAddressPickListener(new AddressPicker.OnAddressPickListener() {
                @Override
                public void onAddressPicked(Province province, City city, County county) {
                    showToast("province : " + province + ", city: " + city + ", county: " + county);
                    getetArea = province.getAreaName() + "-" + city.getAreaName();
                    etArea.setText(province.getAreaName() + "-" + city.getAreaName());
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
            case R.id.btn_fabu_work_detail_back:
                break;
            case R.id.btn_fabu_submit:
                checkEdit();
                sendCheckServer();
                break;
            default:
                break;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
