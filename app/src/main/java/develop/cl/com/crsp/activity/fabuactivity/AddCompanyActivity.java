package develop.cl.com.crsp.activity.fabuactivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Company;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class AddCompanyActivity extends BaseActivity implements View.OnClickListener {

    private Intent mIntent;
    private RequestQueue mQueue;
    private boolean ps;
    private VolleyCallback volleyCallback;
    private static final String Tag = "AddCompanyActivity";
    private EditText etaddCompanyName;
    private EditText etaddCompanyArea;
    private EditText etaddCompanyTel;
    private EditText etaddCompanyDetail;
    private Spinner spaddCompanyNature;
    private Spinner spaddCompanyScale;
    private Button btnaddCompanyAdd;

    private String getaddCompanyName;
    private String getaddCompanyArea;
    private String getaddCompanyTel;
    private String getaddCompanyDetail;
    private String getaddCompanyNature;
    private String getaddCompanyScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcompany);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        etaddCompanyName = (EditText) this.findViewById(R.id.et_add_companyname);
        etaddCompanyArea = (EditText) this.findViewById(R.id.et_add_companyarea);
        etaddCompanyTel = (EditText) this.findViewById(R.id.et_add_tel);
        etaddCompanyDetail = (EditText) this.findViewById(R.id.et_add_datail);

        spaddCompanyNature = (Spinner) this.findViewById(R.id.et_add_nature);
        spaddCompanyScale = (Spinner) this.findViewById(R.id.et_add_scale);

        btnaddCompanyAdd = (Button) this.findViewById(R.id.btn_add_do);

    }

    @Override
    protected void initView() {
        etaddCompanyName.setText("");
        etaddCompanyArea.setText("");
        etaddCompanyTel.setText("");
        etaddCompanyDetail.setText("");
        btnaddCompanyAdd.setOnClickListener(this);
        spaddCompanyNature.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getaddCompanyNature = AddCompanyActivity.this.getResources().getStringArray(R.array.company_nature)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spaddCompanyScale.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getaddCompanyScale = AddCompanyActivity.this.getResources().getStringArray(R.array.company_scale)[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 检查所输入的信息内容
     */
    protected void checkEdit() {
        ps = false;
        getaddCompanyNature = spaddCompanyNature.getSelectedItem().toString();
        getaddCompanyScale = spaddCompanyScale.getSelectedItem().toString();
        getaddCompanyName = etaddCompanyName.getText().toString();
        getaddCompanyArea = etaddCompanyArea.getText().toString();
        getaddCompanyTel = etaddCompanyTel.getText().toString();
        getaddCompanyDetail = etaddCompanyDetail.getText().toString();
        if (!getaddCompanyName.equals("") && getaddCompanyName.length() > 0 &&
                !getaddCompanyArea.equals("") && getaddCompanyArea.length() > 0 &&
                !getaddCompanyTel.equals("") && getaddCompanyTel.length() > 0 &&
                !getaddCompanyDetail.equals("") && getaddCompanyDetail.length() > 0) {
            ps = true;
        } else if (getaddCompanyName.equals("") && getaddCompanyName.length() <= 0) {
            ps = false;
            DisPlay("公司名称不能为空！");
            return;
        } else if (getaddCompanyArea.equals("") && getaddCompanyArea.length() <= 0) {
            ps = false;
            DisPlay("公司地址不能为空！");
            return;
        } else if (getaddCompanyTel.equals("") && getaddCompanyTel.length() <= 0) {
            ps = false;
            DisPlay("公司联系电话不能为空！");
            return;
        } else if (getaddCompanyDetail.equals("") && getaddCompanyDetail.length() <= 0) {
            ps = false;
            DisPlay("公司简介不能为空！");
            return;
        }
    }

    /**
     * 向服务器发送请求并解析
     */
    protected void sendServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(AddCompanyActivity.this);
            Company company = new Company();
            company.setCompanyarea(getaddCompanyArea);
            company.setCompanyname(getaddCompanyName);
            company.setDetail(getaddCompanyDetail);
            company.setNature(getaddCompanyNature);
            company.setScale(getaddCompanyScale);
            company.setTel(getaddCompanyTel);
            String[] str = new String[]{"companyname", "nature", "companyarea", "detail", "tel", "scale"};
            //创建回调接口并实例化方法
            volleyCallback = new VolleyCallback() {
                @Override
                //回调内容result
                public void onSuccessResponse(String result) {
                    Log.d("callBack result", result);
                    if ("".equals(result)) {
                        DisPlay("服务器异常！");
                        return;
                    } else {
                        //解析返回的json
                        JSONObject jsonObject = JSON.parseObject(result);
                        JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                        //根据返回内容执行操作
                        if (jsonMap.get("returnCode").toString().equals("1")) {
                            mIntent = new Intent(AddCompanyActivity.this, MainActivity.class);
                            startActivity(mIntent);
                        }
                        Log.d("returnCode", jsonMap.get("returnCode").toString());
                    }
                }
            };
            //声明自定义Volley实例
//            DFVolley dfv = new DFVolley(volleyCallback);
            String url = ServerInformation.URL + "company/addCompany";
            //调用自定义的Volley函数
            DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, company), volleyCallback);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_do:
                checkEdit();
                sendServer();
                break;
            default:
                break;
        }
    }
}
