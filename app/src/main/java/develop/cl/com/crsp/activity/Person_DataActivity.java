package develop.cl.com.crsp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Dating;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.adapter.AddressPickTask;
import develop.cl.com.crsp.image.CircleImageView;
import develop.cl.com.crsp.util.DFVolley;
import develop.cl.com.crsp.util.ImageUtils;
import develop.cl.com.crsp.util.MyList;
import develop.cl.com.crsp.util.MySharedPreferences;
import develop.cl.com.crsp.util.ServerInformation;
import develop.cl.com.crsp.util.UploadUtil;
import develop.cl.com.crsp.util.VolleyCallback;


public class Person_DataActivity extends BaseActivity implements View.OnClickListener {

    private boolean isChange = false;
    private RequestQueue mQueue;
    private static final String Tag = "Person_DataActivity";
    private Intent mIntent;

    private Button btnBack;

    private LinearLayout lySchool;
    private LinearLayout lyStuid;
    private LinearLayout lyName;
    private LinearLayout lySex;
    private LinearLayout lyData;
    private LinearLayout lyTel;
    private LinearLayout lyEmail;
    private LinearLayout lyAccount;
    private LinearLayout lyHome;
    private LinearLayout lyWork;
    private LinearLayout lyAddress;
    private LinearLayout lyLstate;
    private LinearLayout lyMark;

    private CircleImageView ivPic;
    private TextView tvSchool;
    private TextView tvStuid;
    private TextView tvName;
    private TextView tvSex;
    private TextView tvData;
    private TextView tvTel;
    private TextView tvEmail;
    private TextView tvAccount;
    private TextView tvHome;
    private TextView tvWork;
    private TextView tvAddress;
    private TextView tvLstate;
    private TextView tvMark;

    private String getivPic;
    private String gettvName;
    private String gettvSchool;
    private String gettvStuid;
    private String gettvSex;
    private String gettvData;
    private String gettvTel;
    private String gettvEmail;
    private String gettvAccount;
    private String gettvHome;
    private String gettvWork;
    private String gettvAddress;
    private String gettvLstate;
    private String gettvMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_data);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {

        lySchool = (LinearLayout) this.findViewById(R.id.ly_person_school);
        lyStuid = (LinearLayout) this.findViewById(R.id.ly_person_stuid);
        lyName = (LinearLayout) this.findViewById(R.id.ly_person_name);
        lySex = (LinearLayout) this.findViewById(R.id.ly_person_sex);
        lyData = (LinearLayout) this.findViewById(R.id.lv_person_data);
        lyTel = (LinearLayout) this.findViewById(R.id.lv_person_tel);
        lyEmail = (LinearLayout) this.findViewById(R.id.lv_person_email);
        lyAccount = (LinearLayout) this.findViewById(R.id.lv_person_account);
        lyHome = (LinearLayout) this.findViewById(R.id.lv_person_home);
        lyWork = (LinearLayout) this.findViewById(R.id.lv_person_work);
        lyAddress = (LinearLayout) this.findViewById(R.id.lv_person_address);
        lyLstate = (LinearLayout) this.findViewById(R.id.lv_person_lstate);
        lyMark = (LinearLayout) this.findViewById(R.id.lv_person_mark);

        ivPic = (CircleImageView) this.findViewById(R.id.iv_person_pic);
        tvSchool = (TextView) this.findViewById(R.id.tv_person_school);
        tvStuid = (TextView) this.findViewById(R.id.tv_person_stuid);
        tvName = (TextView) this.findViewById(R.id.tv_person_name);
        tvSex = (TextView) this.findViewById(R.id.tv_person_sex);
        tvData = (TextView) this.findViewById(R.id.tv_person_data);
        tvTel = (TextView) this.findViewById(R.id.tv_person_tel);
        tvEmail = (TextView) this.findViewById(R.id.tv_person_email);
        tvAccount = (TextView) this.findViewById(R.id.tv_person_account);
        tvHome = (TextView) this.findViewById(R.id.tv_person_home);
        tvWork = (TextView) this.findViewById(R.id.tv_person_work);
        tvAddress = (TextView) this.findViewById(R.id.tv_person_address);
        tvLstate = (TextView) this.findViewById(R.id.tv_person_lstate);
        tvMark = (TextView) this.findViewById(R.id.tv_person_mark);
    }

    @Override
    protected void initView() {

//        lySchool.setOnClickListener(this);
        lyStuid.setOnClickListener(this);
        lyName.setOnClickListener(this);
        lySex.setOnClickListener(this);
//        lyData.setOnClickListener(this);
        lyTel.setOnClickListener(this);
        lyEmail.setOnClickListener(this);
        lyAccount.setOnClickListener(this);
//        lyHome.setOnClickListener(this);
        lyWork.setOnClickListener(this);
//        lyAddress.setOnClickListener(this);
        lyLstate.setOnClickListener(this);
        lyMark.setOnClickListener(this);
        ivPic.setOnClickListener(this);

        /**
         * 获取配置文件中内容
         */
        Basic basic = getBasic();
        Dating dating = getDating();

        getivPic = basic.getPicture();
        gettvSchool = basic.getSchool();
        gettvStuid = basic.getStuid();
        gettvName = basic.getName();
        gettvSex = basic.getSex();
        gettvData = basic.getBirthday();
        gettvTel = basic.getTel();
        gettvEmail = basic.getEmail();

        gettvHome = dating.getHometown();
        gettvWork = dating.getProfessional();
        gettvAddress = dating.getBusiness();
        gettvLstate = dating.getState();
        gettvMark = dating.getSignature();
        /**
         * 初始化显示参数
         */
        Bitmap bitmap = BitmapFactory.decodeFile(basic.getPicture());
        ivPic.setImageBitmap(bitmap);
        tvSchool.setText(gettvSchool);
        tvStuid.setText(gettvStuid);
        tvName.setText(gettvName);
        tvSex.setText(gettvSex);
        tvData.setText(gettvData);
        tvTel.setText(gettvTel);
        tvEmail.setText(gettvEmail);
        tvAccount.setText(MySharedPreferences.getUserID(Person_DataActivity.this));

        tvHome.setText(gettvHome);
        tvWork.setText(gettvWork);
        tvAddress.setText(gettvAddress);
        tvLstate.setText(gettvLstate);
        tvMark.setText(gettvMark);
    }

    /**
     * 获取配置文件中Basic内容
     */
    protected Basic getBasic() {
        return MySharedPreferences.getBasicData(Person_DataActivity.this);
    }

    /**
     * 获取配置文件中Dating内容
     */
    protected Dating getDating() {
        return MySharedPreferences.getDatingData(Person_DataActivity.this);
    }

    private void showInputDialog(final TextView tv, String str, String message) {
        final EditText editText = new EditText(Person_DataActivity.this);
        editText.setText(str);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(Person_DataActivity.this);
        inputDialog.setTitle(message).setView(editText);
        inputDialog.setIcon(R.mipmap.setp);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isChange = true;
                        tv.setText(editText.getText().toString());
                    }
                });
        inputDialog.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        inputDialog.show();
    }

    protected void checkData(TextView tv, String title) {
        if (!"等待完善".equals(tv.getText().toString())) {
            showInputDialog(tv, tv.getText().toString(), title);
        } else {
            showInputDialog(tv, "", title);
        }
    }

    public void onAddress3Picker(View view) {
        AddressPickTask task = new AddressPickTask(this);
        task.setHideCounty(true);
        task.setCallback(new AddressPickTask.Callback() {
            @Override
            public void onAddressInitFailed() {
                showToast("数据初始化失败");
            }

            @Override
            public void onAddressPicked(Province province, City city, County county) {
//                showToast(province.getAreaName() + " " + city.getAreaName());
                tvSchool.setText(city.getAreaName());
                isChange = true;
            }
        });
        task.execute("广西", "桂林理工大学");
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_person_pic:
                pickPhoto();
                break;
//            case R.id.ly_person_school:
//                checkData(tvSchool, "修改学校");
//                break;
            case R.id.ly_person_stuid:
                checkData(tvStuid, "修改学号");
                break;
            case R.id.ly_person_name:
                checkData(tvName, "修改昵称");
                break;
            case R.id.ly_person_sex:
                checkData(tvSex, "修改性别");
                break;
//            case R.id.lv_person_data:
//                checkData(tvData, "修改生日");
//                break;
            case R.id.lv_person_tel:
                checkData(tvTel, "修改手机号码");
                break;
            case R.id.lv_person_email:
                checkData(tvEmail, "修改邮箱地址");
                break;
//            case R.id.lv_person_home:
//                checkData(tvHome, "修改家乡");
//                break;
            case R.id.lv_person_work:
                checkData(tvWork, "修改工作");
                break;
//            case R.id.lv_person_address:
//                checkData(tvAddress, "修改地址");
//                break;
            case R.id.lv_person_lstate:
                checkData(tvLstate, "修改情感状态");
                break;
            case R.id.lv_person_mark:
                checkData(tvMark, "修改签名");
                break;
        }
    }

    public void onYearMonthDayPicker(View view) {
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
                isChange = true;
                tvData.setText(year + "-" + month + "-" + day);
                showToast(year + "-" + month + "-" + day);
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
                    showToast("province : " + province + ", city: " + city + ", county: " + county);
                    tvHome.setText(province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName());
                    isChange = true;
                }
            });
            picker.show();
        } catch (Exception e) {
            showToast(LogUtils.toStackTraceString(e));
        }
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
                    tvAddress.setText(province.getAreaName() + "-" + city.getAreaName());
                    isChange = true;
                }
            });
            picker.show();
        } catch (Exception e) {
            showToast(LogUtils.toStackTraceString(e));
        }
    }

    /**
     * 更新内容至服务器和本地
     */
    protected void sendUpdateServer() {

        Dating dating = new Dating();
        Basic basic = new Basic();
        gettvSchool = tvSchool.getText().toString();
        gettvStuid = tvStuid.getText().toString();
        gettvName = tvName.getText().toString();
        gettvSex = tvSex.getText().toString();
        gettvData = tvData.getText().toString();
        gettvTel = tvTel.getText().toString();
        gettvEmail = tvEmail.getText().toString();
//        basic.setPicture(getivPic);
        basic.setSchool(gettvSchool);
        basic.setStuid(gettvStuid);
        basic.setName(gettvName);
        basic.setSex(gettvSex);
        basic.setBirthday(gettvData);
        basic.setTel(gettvTel);
        basic.setEmail(gettvEmail);

        gettvHome = tvHome.getText().toString();
        gettvWork = tvWork.getText().toString();
        gettvAddress = tvAddress.getText().toString();
        gettvLstate = tvLstate.getText().toString();
        gettvMark = tvMark.getText().toString();
        dating.setHometown(gettvHome);
        dating.setSignature(gettvMark);
        dating.setBusiness(gettvAddress);
        dating.setProfessional(gettvWork);
        dating.setState(gettvLstate);

        Basic preBasic = MySharedPreferences.getBasicData(Person_DataActivity.this);
        Dating preDating = MySharedPreferences.getDatingData(Person_DataActivity.this);

        MySharedPreferences.setPersonData(Person_DataActivity.this, basic, dating);

        basic.setPicture(getivPic);

        basic.setBasicid(preBasic.getBasicid());
        dating.setDating(preDating.getDating());
        String[] strBasic = new String[]{"basicid", "picture", "name", "sex", "birthday", "tel", "email", "school", "stuid"};
        String[] strDating = new String[]{"dating", "hometown", "signature", "business"
                , "professional", "state"};
        //创建回调接口并实例化方法
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
            }
        };
        String url = ServerInformation.URL + "basic/update";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(strBasic, basic), volleyCallback);

        //创建回调接口并实例化方法
        VolleyCallback volleyCallback1 = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack1 result", result);
            }
        };
        String url1 = ServerInformation.URL + "dating/update";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url1, MyList.strList(strDating, dating), volleyCallback1);
    }

    /**
     * 上传头像至服务器
     *
     * @param pthotoUrl 本地头像地址
     * @throws IOException
     * @throws FileNotFoundException
     */
    protected void sendUploadServer(String pthotoUrl) throws IOException {
        mQueue = Volley.newRequestQueue(Person_DataActivity.this);
        String uploadUrl = "";
        uploadUrl = ServerInformation.URL + "basic/upload";
        VolleyCallback volleyUploadBack = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                JSONObject jsonObject = JSON.parseObject(result);
                JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                if (jsonMap.get("returnCode").toString().equals("1")) {
                    getivPic = jsonMap.get("dbStr").toString();
                    Log.d("getivPic", getivPic);
                    sendUpdateServer();
                }
            }
        };
        UploadUtil.UploadReqPhoto(uploadUrl, pthotoUrl, Person_DataActivity.this, mQueue, volleyUploadBack);
    }

    /**
     * 从相册中取图片
     */
    private void pickPhoto() {
        mIntent = new Intent(Intent.ACTION_PICK);
//        mIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "*/*");
        mIntent.setType("image/*");
        startActivityForResult(mIntent, 200);
    }

    String[] proj = {MediaStore.MediaColumns.DATA};

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            isChange = true;
            String imagePath = "";
            Uri uri = null;
            if (data != null && data.getData() != null) {// 有数据返回直接使用返回的图片地址
                uri = data.getData();
                Cursor cursor = getContentResolver().query(uri, proj, null,
                        null, null);
                if (cursor == null) {
                    uri = ImageUtils.getUri(this, data);
                }
                imagePath = ImageUtils.getFilePathByFileUri(this, uri);
            }
            getivPic = imagePath;
            Bitmap bitmap = BitmapFactory.decodeFile(getivPic);
            ivPic.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag, "onDestroy");
        if (isChange) {
            Log.i(Tag, "isChange");
            MySharedPreferences.setPhoto(Person_DataActivity.this, getivPic);
            try {
                sendUploadServer(getivPic);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
