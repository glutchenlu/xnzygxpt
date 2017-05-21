package develop.cl.com.crsp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Resume;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;


public class JianliActivity extends BaseActivity implements View.OnClickListener
        , AdapterView.OnItemSelectedListener {

    private boolean ps;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;
    private static final String Tag = "JianliActivity";
    private Intent mIntent;
    private String dotype;
    private String resultMap;
    private Resume preResume;

    private Button btnSubmi;
    private EditText etName;
    private EditText etJianliName;
    private EditText etTel;
    private EditText etEmail;
    private Spinner spSex;
    private Spinner spEdu;
    private Spinner spExp;
    private TextView tvDate;
    private Spinner spExpSal;
    private TextView tvExpArea;
    private EditText etExpPosition;
    private EditText etDescribe;

    private String getetName;
    private String getetTel;
    private String getetEmail;
    private String getspSex;
    private String getspEdu;
    private String getspExp;
    private String gettvDate;
    private String getetJianliName;
    private String getspExpSal;
    private String gettvExpArea;
    private String getetExpPosition;
    private String getetDescribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jianli);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        btnSubmi = (Button) this.findViewById(R.id.btn_submit);
        etJianliName = (EditText) this.findViewById(R.id.et_jianli_jianliname);
        etName = (EditText) this.findViewById(R.id.et_jianli_name);
        etTel = (EditText) this.findViewById(R.id.et_jianli_tel);
        etEmail = (EditText) this.findViewById(R.id.et_jianli_email);
        spSex = (Spinner) this.findViewById(R.id.sp_jianli_sex);
        spEdu = (Spinner) this.findViewById(R.id.sp_jianli_edu);
        spExp = (Spinner) this.findViewById(R.id.sp_jianli_exp);
        tvDate = (TextView) this.findViewById(R.id.tv_jianli_data);
        spExpSal = (Spinner) this.findViewById(R.id.tv_jianli_sal);
        tvExpArea = (TextView) this.findViewById(R.id.tv_jianli_area);
        etExpPosition = (EditText) this.findViewById(R.id.et_jianli_position);
        etDescribe = (EditText) this.findViewById(R.id.et_jianli_describe);
    }

    @Override
    protected void initView() {

        mIntent = this.getIntent();
        dotype = mIntent.getStringExtra("dotype");
//        Resume preResume = new Resume();
        if ("修改".equals(dotype)) {
            resultMap = mIntent.getStringExtra("resultMap");
            JSONObject jsonMap = JSON.parseObject(resultMap);
            preResume = JSON.parseObject(jsonMap.get("resume").toString(), Resume.class);
            etName.setText(preResume.getName());
            etJianliName.setText(preResume.getResumename());
            etTel.setText(preResume.getTel());
            etEmail.setText(preResume.getEmail());
            spSex.setSelection(setSpDefault(R.array.jianli_sex, preResume.getSex()));
            spEdu.setSelection(setSpDefault(R.array.fabu_work_education, preResume.getEducation()));
            spExp.setSelection(setSpDefault(R.array.fabu_work_experience, preResume.getWork_year()));
            spExpSal.setSelection(setSpDefault(R.array.sal, preResume.getExp_salary()));
            tvDate.setText(preResume.getBirth_year());
            tvExpArea.setText(preResume.getExp_area());
            etExpPosition.setText(preResume.getExp_position());
            etDescribe.setText(preResume.getRedescribe());
        }
        btnSubmi.setOnClickListener(this);
        spSex.setOnItemSelectedListener(this);
        spEdu.setOnItemSelectedListener(this);
        spExp.setOnItemSelectedListener(this);
    }

    /**
     * 检查所输入的发布信息内容
     */
    protected void checkEdit() {
        ps = false;
        getspSex = spSex.getSelectedItem().toString();
        getspEdu = spEdu.getSelectedItem().toString();
        getspExp = spExp.getSelectedItem().toString();
        getetName = etName.getText().toString();
        getetTel = etTel.getText().toString();
        getetEmail = etEmail.getText().toString();
        getetJianliName = etJianliName.getText().toString();
        gettvDate = tvDate.getText().toString();
        getspExpSal = spExpSal.getSelectedItem().toString();
        gettvExpArea = tvExpArea.getText().toString();
        getetExpPosition = etExpPosition.getText().toString();
        getetDescribe = etDescribe.getText().toString();

        if (!getetName.equals("") && getetName.length() > 0
                && !getetTel.equals("") && getetTel.length() > 0
                && !getetEmail.equals("") && getetEmail.length() > 0
                && !getetJianliName.equals("") && getetJianliName.length() > 0
                && !gettvDate.equals("") && gettvDate.length() > 0) {
            ps = true;
        } else if (getetJianliName.equals("") && getetJianliName.length() <= 0) {
            ps = false;
            DisPlay("简历名称不能为空！");
            return;
        } else if (getetName.equals("") && getetName.length() <= 0) {
            ps = false;
            DisPlay("姓名不能为空！");
            return;
        } else if (getetTel.equals("") && getetTel.length() <= 0) {
            ps = false;
            DisPlay("手机号码不能为空！");
            return;
        } else if (getetEmail.equals("") && getetEmail.length() <= 0) {
            ps = false;
            DisPlay("邮箱地址不能为空！");
            return;
        } else if (gettvDate.equals("") && gettvDate.length() <= 0) {
            ps = false;
            DisPlay("出生年月不能为空！");
            return;
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendSaveServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(JianliActivity.this);
            Resume resume = new Resume();
            resume.setUserid(MySharedPreferences.getUserID(JianliActivity.this));
            if ("修改".equals(dotype)) {
                resume.setResumeid(preResume.getResumeid());
            }
            resume.setSex(getspSex);
            resume.setBirth_year(gettvDate);
            resume.setEducation(getspEdu);
            resume.setExp_salary(getspExpSal);
            resume.setExp_area(gettvExpArea);
            resume.setRedescribe(getetDescribe);
            resume.setTel(getetTel);
            resume.setExp_position(getetExpPosition);
            resume.setWork_year(getspExp);
            resume.setName(getetName);
            resume.setResumename(getetJianliName);
            resume.setEmail(getetEmail);

            String[] str = new String[]{"userid", "sex", "birth_year", "education", "exp_salary"
                    , "exp_area", "redescribe", "tel", "exp_position", "work_year", "name", "resumename", "email"};
            String[] str1 = new String[]{"resumeid", "userid", "sex", "birth_year", "education", "exp_salary"
                    , "exp_area", "redescribe", "tel", "exp_position", "work_year", "name", "resumename", "email"};
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
                            if ("新增".equals(dotype)) {
                                MySharedPreferences.setResumeCount(JianliActivity.this
                                        , Integer.parseInt(jsonMap.get("count").toString()));
                            }
                            //显示提示消息
                            DisPlay(jsonMap.get("returnString").toString());
                            mIntent = new Intent(JianliActivity.this, MainActivity.class);
                            startActivity(mIntent);
                        } else if (jsonMap.get("returnCode").toString().equals("2")) {
                            //显示提示消息
                            DisPlay(jsonMap.get("returnString").toString());
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            String url = "";
            if ("修改".equals(dotype)) {
                url = ServerInformation.URL + "resume/update";
                //调用自定义的Volley函数
                DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str1, resume), volleyCallback);
            } else if ("新增".equals(dotype)) {
                url = ServerInformation.URL + "resume/add";
                //调用自定义的Volley函数
                DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, resume), volleyCallback);
            }
        }
    }

    public void onYearMonthDayResumePicker(View view) {
        final DatePicker picker = new DatePicker(this);
        picker.setCanceledOnTouchOutside(true);
        picker.setUseWeight(true);
        picker.setTopPadding(ConvertUtils.toPx(this, 20));
        picker.setRangeEnd(2111, 12, 31);
        picker.setRangeStart(1930, 01, 01);
        picker.setSelectedItem(1994, 12, 16);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                tvDate.setText(year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    public void onAddressResumePicker(final View view) {
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
//                    showToast("province : " + province + ", city: " + city + ", county: " + county);
                    tvExpArea.setText(province.getAreaName() + "-" + city.getAreaName());
                }
            });
            picker.show();
        } catch (Exception e) {
//            showToast(LogUtils.toStackTraceString(e));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:
                checkEdit();
                sendSaveServer();
                break;
            default:
                break;
        }
    }

    protected int setSpDefault(int srcID, String str) {
        String[] strs = JianliActivity.this.getResources().getStringArray(srcID);
        int strLength = strs.length;
        int rtPosition = -1;
        for (int i = 0; i < strLength; i++) {
            if (str.equals(strs[i])) {
                rtPosition = i;
            }
        }
        return rtPosition;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (view.getId()) {
            case R.id.sp_jianli_sex:
                getspSex = JianliActivity.this.getResources().getStringArray(
                        R.array.jianli_sex)[position];
                break;
            case R.id.sp_jianli_edu:
                getspEdu = JianliActivity.this.getResources().getStringArray(
                        R.array.fabu_work_education)[position];
                break;
            case R.id.tv_jianli_data:
                getspExp = JianliActivity.this.getResources().getStringArray(
                        R.array.fabu_work_experience)[position];
                break;
            case R.id.tv_jianli_sal:
                getspExp = JianliActivity.this.getResources().getStringArray(
                        R.array.sal)[position];
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
