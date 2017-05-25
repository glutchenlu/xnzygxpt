package develop.cl.com.crsp.activity.fabuactivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.qqtheme.framework.picker.FilePicker;
import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.LearningData;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.UploadUtil;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class FabuDataDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final String Tag = "FabuDataDetailActivity";
    private GridView gv_data;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;
    private Intent mIntent;
    private boolean ps;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;

    private TextView tvTop;
    private Button btnSubmit;
    private TextView tvTopData;
    private EditText etTitle;
    private EditText etDetail;

    private String getetTitle;
    private String getetDetail;
    private String gettvTop;
    private String getreturnStr = "";
    private List<String> pathList;
    private List<String> nameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_data_detail);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        tvTop = (TextView) this.findViewById(R.id.tv_data_classify);
        tvTopData = (TextView) this.findViewById(R.id.tv_top_data);
        btnSubmit = (Button) this.findViewById(R.id.btn_data_submit);
        etTitle = (EditText) this.findViewById(R.id.et_data_title);
        etDetail = (EditText) this.findViewById(R.id.et_data_detail);
        gv_data = (GridView) this.findViewById(R.id.gv_data);
    }

    @Override
    protected void initView() {
        btnSubmit.setOnClickListener(this);
        mIntent = this.getIntent();
        Log.d(Tag, mIntent.getStringExtra("typeName"));
        //从Intent获得额外信息，设置为TextView的文本
        tvTop.setText(mIntent.getStringExtra("typeName"));
        pathList = new ArrayList<String>();
        nameList = new ArrayList<String>();
        datalist = new ArrayList<Map<String, Object>>();
        String[] mapName = new String[]{"data_name"};
        int[] controlId = new int[]{R.id.tv_fabu_class_item};
        sadapter = new SimpleAdapter(this, datalist, R.layout.gv_fabu_class_item, mapName, controlId);
        gv_data.setAdapter(sadapter);
        gv_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                datalist.remove(position);
                sadapter.notifyDataSetChanged();
            }
        });
    }

    public void onFilePicker(View view) {
        FilePicker picker = new FilePicker(this, FilePicker.FILE);
        picker.setShowHideDir(false);
        //picker.setAllowExtensions(new String[]{".apk"});
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                Log.i(Tag, currentPath);
                DisPlay(currentPath);
                String locStr = currentPath.substring(currentPath.lastIndexOf("/") + 1);
                nameList.add(locStr);
                pathList.add(currentPath);
                Map<String, Object> addMap = new HashMap<String, Object>();
                addMap.put("data_name", locStr);
                datalist.add(addMap);
                sadapter.notifyDataSetChanged();
            }
        });
        picker.show();
    }

    /**
     * 添加goods
     */
    protected void sendAddLearningdataServer() {
        int listsize = nameList.size();
        String data_name = "";
        for (int i = 0; i < listsize; i++) {
            if (i == 0) {
                data_name = nameList.get(i);
            } else {
                data_name = data_name + "," + nameList.get(i);
            }
        }
        LearningData learningData = new LearningData();
        learningData.setClassify(gettvTop);
        learningData.setUserid(MySharedPreferences.getUserID(FabuDataDetailActivity.this));
        learningData.setTitle(getetTitle);
        learningData.setDetail(getetDetail);
        learningData.setKeyword(gettvTop + "," + getetTitle);
        learningData.setData_name(data_name);
        learningData.setData_path(getreturnStr);
        learningData.setSchool(MySharedPreferences.getSchool(FabuDataDetailActivity.this));
        String[] str = new String[]{"userid", "classify", "title", "detail", "keyword", "data_name",
                "school", "data_path"};
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
                    //显示提示消息
                    DisPlay(jsonMap.get("returnString").toString());
                    //根据返回内容执行操作
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        mIntent = new Intent(FabuDataDetailActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "learningdata/add";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, learningData), volleyCallback);
    }

    /**
     * 上传文件向服务器发送请求并解析
     */
    protected void sendUploadServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(FabuDataDetailActivity.this);
//            showProgressDialog();
            String uploadUrl = "";
            uploadUrl = ServerInformation.URL + "learningdata/upload";
            VolleyCallback volleyUploadBack = new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        getreturnStr = jsonMap.get("dbStr").toString();
                        Log.d("getreturnStr", getreturnStr);
                        sendAddLearningdataServer();
                    }
                }
            };
            if (pathList.size() > 0) {
                UploadUtil.UploadData(uploadUrl, pathList, FabuDataDetailActivity.this, mQueue, volleyUploadBack);
            } else {
                sendAddLearningdataServer();
            }
        }
    }

    /**
     * 检查所输入的发布信息内容
     */
    protected void checkEdit() {
        ps = false;
        getetTitle = etTitle.getText().toString();
        gettvTop = tvTop.getText().toString();
        getetDetail = etDetail.getText().toString();
        if (!getetTitle.equals("") && getetTitle.length() > 0
                && !getetDetail.equals("") && getetDetail.length() > 0) {
            ps = true;
        } else if (getetTitle.equals("") && getetTitle.length() <= 0) {
            ps = false;
            DisPlay("标题不能为空！");
            return;
        } else if (getetDetail.equals("") && getetDetail.length() <= 0) {
            ps = false;
            DisPlay("详细内容不能为空！");
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        sadapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_data_submit:
                checkEdit();
                if (MyCheckNet.isNetworkAvailable(FabuDataDetailActivity.this)) {
                    sendUploadServer();
                } else {
                    DisPlay("请检查您的网络连接");
                }
                break;
            default:
                break;
        }
    }
}
