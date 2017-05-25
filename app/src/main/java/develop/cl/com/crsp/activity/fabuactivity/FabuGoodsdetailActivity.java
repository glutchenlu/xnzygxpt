package develop.cl.com.crsp.activity.fabuactivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;
import cn.qqtheme.framework.picker.AddressPicker;
import cn.qqtheme.framework.util.ConvertUtils;
import cn.qqtheme.framework.util.LogUtils;
import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Goods;
import develop.cl.com.crsp.JavaBean.SecondGoods;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.activity.MainActivity;
import develop.cl.com.crsp.adapter.GridImageAdapter;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.SetHight;
import develop.cl.com.crsp.myutil.UploadUtil;
import develop.cl.com.crsp.myutil.VolleyCallback;
import develop.cl.com.crsp.util.ImageUtils;


public class FabuGoodsdetailActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 物品出租或二手交易
     */
    private int servicePosition;
    /**
     * 拍照图片保存路径
     */
    private String myImagePath;
    /**
     * 图片GridView
     */
    private GridView gridView;
    /**
     * 图片数据list
     */
    private ArrayList<String> dataList = new ArrayList<String>();
    /**
     * 图片数据构造器
     */
    private GridImageAdapter gridImageAdapter;

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

    private static final String Tag = "FabuGoodsdetailActivity";
    /**
     * 跳转对象
     */
    private Intent mIntent;

    /**
     * 类别
     */
    private TextView tvTop;
    /**
     * 返回上一界面
     */
    private Button btnBack;
    /**
     * 提交
     */
    private Button btnSubmit;
    /**
     * 标题
     */
    private EditText etTitle;
    /**
     * 物品名
     */
    private EditText etGoodsName;
    /**
     * 价钱
     */
    private EditText etGoodsPrice;
    /**
     * 新旧程度
     */
    private Spinner spGoodsDegree;
    /**
     * 物品描述
     */
    private EditText etGoodsDetail;
    /**
     * 交易区域
     */
    private TextView etGoodsArea;
    /**
     * 物品来源
     */
    private EditText etGoodsSource;
    /**
     * 联系人名称
     */
    private EditText etGoodsTelName;
    /**
     * 联系人电话
     */
    private EditText etGoodsTel;
    private String getPic = "";
    private String gettvTop;
    private String getetTitle;
    private String getetGoodsName;
    private String getetGoodsPrice;
    private String getspGoodsDegree;
    private String getetGoodsDetail;
    private String getetGoodsArea;
    private String getetGoodsSource;
    private String getetGoodsTelName;
    private String getetGoodsTel;
    /**
     * 回调内容
     */
    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AlbumActivity.SELECT_OK) {
                ArrayList<String> tDataList = (ArrayList<String>) msg.obj;
                if (tDataList != null) {
                    Log.e("handleMessage", "" + "running");

                    Log.e("tDataList.size()", "" + tDataList.size());
                    if (tDataList.size() < 8) {
                        tDataList.add("camera_default");
                    }
                    dataList.clear();
                    dataList.addAll(tDataList);
                    deHight();
                    init();
                }
            }
        }
    };

    /**
     * 根据item行数设置gv高度
     */
    protected void deHight() {
        if (dataList.size() <= 4) {
            Log.d(Tag, dataList.size() + "");
            SetHight.setListViewHeightBasedOnChildren(gridView, 0);
        }
        if (dataList.size() >= 5) {
            Log.d(Tag, dataList.size() + "");
            SetHight.setListViewHeightBasedOnChildren(gridView, 1);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fabu_goods_detail);
        gridView = (GridView) findViewById(R.id.myGrid);
        dataList.add("camera_default");
        AlbumActivity.setmHandler(mHandler);
        findViewById();
        initView();
        initListener();
    }

    /**
     * 图片列表初始化
     */
    private void init() {
        gridImageAdapter = new GridImageAdapter(this, dataList);
        gridView.setAdapter(gridImageAdapter);
        gridImageAdapter.notifyDataSetChanged();
    }

    /**
     * 填写界面单机操作
     */
    private void initListener() {
        gridView.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == dataList.size() - 1
                        && "camera_default".equals(dataList.get(position))) {
                    showMyPictureDailog();
                } else {
                    dialog(position);
                }
            }
        });
    }

    /**
     * 拍照或从图库选择图片(Dialog形式)
     */
    public void showMyPictureDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(new String[]{"拍摄照片", "选择照片", "取消"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0://拍照
                                myTakePhoto();
                                break;
                            case 1://相册选择图片
                                Intent intent = new Intent(FabuGoodsdetailActivity.this,
                                        ImageList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                Bundle bundle = new Bundle();
                                bundle.putStringArrayList("dataList",
                                        getIntentArrayList(dataList));
                                intent.putExtras(bundle);
                                startActivityForResult(intent, 0);
                                break;
                            case 2://取消
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    /**
     * 拍照获取图片
     */
    private void myTakePhoto() {
        // 执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            /**
             * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
             */
            //获取与应用相关联的路径
            String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            //根据当前时间生成图片的名称
            String timestamp = "/" + formatter.format(new Date()) + ".jpg";
            File imageFile = new File(imageFilePath, timestamp);// 通过路径创建保存文件
            myImagePath = imageFile.getAbsolutePath();
            Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
            startActivityForResult(intent, 200);
        } else {
            Toast.makeText(this, "内存卡不存在!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 是否删除对话框
     */
    protected void dialog(final int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(FabuGoodsdetailActivity.this);
        builder.setMessage("确认删除吗？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataList.remove(index);
                ArrayList<String> tDataList = getIntentArrayList(dataList);
                tDataList.add("camera_default");
                dataList.clear();
                dataList.addAll(tDataList);
                deHight();
                init();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    String[] proj = {MediaStore.MediaColumns.DATA};

    /**
     * Intent回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("onActivityResult==>", "" + "running");
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ArrayList<String> tDataList = (ArrayList<String>) bundle
                        .getSerializable("dataList");
                if (tDataList != null) {
                    if (tDataList.size() < 8) {
                        tDataList.add("camera_default");
                    }
                    dataList.clear();
                    dataList.addAll(tDataList);
                    deHight();
                    gridImageAdapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode == 200 && resultCode == RESULT_OK) {
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
            } else {// 无数据使用指定的图片路径
                imagePath = myImagePath;
            }
            Log.d("size", dataList.size() + "");
            dataList.remove(dataList.size() - 1);
            dataList.add(imagePath);
            dataList.add("camera_default");
            deHight();
            gridImageAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<String> getIntentArrayList(ArrayList<String> dataList) {
        ArrayList<String> tDataList = new ArrayList<String>();
        for (String s : dataList) {
            if (!s.contains("default")) {
                tDataList.add(s);
            }
        }
        return tDataList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        gridImageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void findViewById() {
        btnBack = (Button) this.findViewById(R.id.btn_fabu_goods_detail_back);
        btnSubmit = (Button) this.findViewById(R.id.btn_fabu_goods_submit);
        tvTop = (TextView) this.findViewById(R.id.tv_fabu_goods_top);
        etTitle = (EditText) this.findViewById(R.id.et_fabu_goods_title);
        etGoodsName = (EditText) this.findViewById(R.id.et_fabu_goods_name);
        etGoodsPrice = (EditText) this.findViewById(R.id.et_fabu_goods_price);
        spGoodsDegree = (Spinner) this.findViewById(R.id.sp_fabu_goods_degree);
        etGoodsDetail = (EditText) this.findViewById(R.id.et_fabu_goods_detail);
        etGoodsArea = (TextView) this.findViewById(R.id.et_fabu_goods_area);
        etGoodsSource = (EditText) this.findViewById(R.id.et_fabu_goods_source);
        etGoodsTelName = (EditText) this.findViewById(R.id.et_fabu_goods_telname);
        etGoodsTel = (EditText) this.findViewById(R.id.et_fabu_goods_tel);
    }

    @Override
    protected void initView() {
        init();
        btnSubmit.setOnClickListener(this);
        mIntent = this.getIntent();
        Log.d(Tag, mIntent.getStringExtra("typeName"));
        //从Intent获得额外信息，设置为TextView的文本
        tvTop.setText(mIntent.getStringExtra("typeName"));
        servicePosition = mIntent.getIntExtra("servicePosition", -1);
        spGoodsDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getspGoodsDegree = FabuGoodsdetailActivity.this.getResources().getStringArray(R.array.fabu_goods_degree)[position];
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
        getspGoodsDegree = spGoodsDegree.getSelectedItem().toString();
        gettvTop = tvTop.getText().toString();
        getetTitle = etTitle.getText().toString();
        getetGoodsName = etGoodsName.getText().toString();
        getetGoodsPrice = etGoodsPrice.getText().toString();
        getetGoodsDetail = etGoodsDetail.getText().toString();
        getetGoodsArea = etGoodsArea.getText().toString();
        getetGoodsSource = etGoodsSource.getText().toString();
        getetGoodsTelName = etGoodsTelName.getText().toString();
        getetGoodsTel = etGoodsTel.getText().toString();
        if (!getetTitle.equals("") && getetTitle.length() > 0
                && !getetGoodsName.equals("") && getetGoodsName.length() > 0
                && !getetGoodsPrice.equals("") && getetGoodsPrice.length() > 0
                && !getetGoodsDetail.equals("") && getetGoodsDetail.length() > 0
                && !getetGoodsArea.equals("") && getetGoodsArea.length() > 0
                && !getetGoodsSource.equals("") && getetGoodsSource.length() > 0
                && !getetGoodsTelName.equals("") && getetGoodsTelName.length() > 0
                && !getetGoodsTel.equals("") && getetGoodsTel.length() > 0) {
            ps = true;
        } else if (getetTitle.equals("") && getetTitle.length() <= 0) {
            ps = false;
            DisPlay("标题不能为空！");
            return;
        } else if (getetGoodsName.equals("") && getetGoodsName.length() <= 0) {
            ps = false;
            DisPlay("物品名称不能为空！");
            return;
        } else if (getetGoodsPrice.equals("") && getetGoodsPrice.length() <= 0) {
            ps = false;
            DisPlay("物品价格不能为空！");
            return;
        } else if (getetGoodsDetail.equals("") && getetGoodsDetail.length() <= 0) {
            ps = false;
            DisPlay("物品描述不能为空！");
            return;
        } else if (getetGoodsSource.equals("") && getetGoodsSource.length() <= 0) {
            ps = false;
            DisPlay("物品来源不能为空！");
            return;
        } else if (getetGoodsTelName.equals("") && getetGoodsTelName.length() <= 0) {
            ps = false;
            DisPlay("联系人名称不能为空！");
            return;
        } else if (getetGoodsTel.equals("") && getetGoodsTel.length() <= 0) {
            ps = false;
            DisPlay("联系人电话不能为空！");
            return;
        }
    }

    /**
     * 添加goods
     */
    protected void sendAddGoodsServer() {
        Goods goods = new Goods();
        goods.setClassify(gettvTop);
        goods.setSource(getetGoodsSource);
        goods.setTitle(getetTitle);
        goods.setArea(getetGoodsArea);
        goods.setDetail(getetGoodsDetail);
        goods.setGoods_name(getetGoodsName);
        goods.setKeyword(gettvTop + "," + getetTitle + "," + getetGoodsArea + "," + getetGoodsName);
        goods.setPrice(getetGoodsPrice);
        goods.setTel(getetGoodsTel);
        goods.setSchool(MySharedPreferences.getSchool(FabuGoodsdetailActivity.this));
        goods.setTelname(getetGoodsTelName);
        goods.setDegree(getspGoodsDegree);
        goods.setPic(getPic);
        goods.setUserid(MySharedPreferences.getUserID(FabuGoodsdetailActivity.this));
        String[] str = new String[]{"userid", "classify", "area", "price", "title", "keyword"
                , "detail", "goods_name", "source", "tel", "telname", "degree", "pic", "school"};
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
                        mIntent = new Intent(FabuGoodsdetailActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "goods/addGoods";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, goods), volleyCallback);
    }

    /**
     * 添加secondgoods
     */
    protected void sendAddSecondGoodsServer() {
        SecondGoods secondGoods = new SecondGoods();
        secondGoods.setClassify(gettvTop);
        secondGoods.setSource(getetGoodsSource);
        secondGoods.setTitle(getetTitle);
        secondGoods.setArea(getetGoodsArea);
        secondGoods.setDetail(getetGoodsDetail);
        secondGoods.setGoods_name(getetGoodsName);
        secondGoods.setKeyword(gettvTop + "," + getetTitle + "," + getetGoodsArea + "," + getetGoodsName);
        secondGoods.setPrice(getetGoodsPrice);
        secondGoods.setTel(getetGoodsTel);
        secondGoods.setSchool(MySharedPreferences.getSchool(FabuGoodsdetailActivity.this));
        secondGoods.setTelname(getetGoodsTelName);
        secondGoods.setDegree(getspGoodsDegree);
        secondGoods.setPic(getPic);
        secondGoods.setUserid(MySharedPreferences.getUserID(FabuGoodsdetailActivity.this));
        String[] str = new String[]{"userid", "classify", "area", "price", "title", "keyword"
                , "detail", "goods_name", "source", "tel", "telname", "degree", "pic", "school"};
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
                        mIntent = new Intent(FabuGoodsdetailActivity.this, MainActivity.class);
                        startActivity(mIntent);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "secondgoods/addsecondGoods";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, secondGoods), volleyCallback);
    }

    /**
     * 上传文件向服务器发送请求并解析
     */
    protected void sendUploadServer() {
        if (ps) {
            mQueue = Volley.newRequestQueue(FabuGoodsdetailActivity.this);
            showProgressDialog();
            String uploadUrl = "";
            if (servicePosition == 0) {
                uploadUrl = ServerInformation.URL + "secondgoods/upload";
            } else if (servicePosition == 1) {
                uploadUrl = ServerInformation.URL + "goods/upload";
            }
            VolleyCallback volleyUploadBack = new VolleyCallback() {
                @Override
                public void onSuccessResponse(String result) {
                    JSONObject jsonObject = JSON.parseObject(result);
                    JSONObject jsonMap = JSON.parseObject(jsonObject.get("resultMap").toString());
                    if (jsonMap.get("returnCode").toString().equals("1")) {
                        getPic = jsonMap.get("dbStr").toString();
                        Log.d("getPic", getPic);
                        if (servicePosition == 0) {
                            sendAddSecondGoodsServer();
                        } else if (servicePosition == 1) {
                            sendAddGoodsServer();
                        }
                    }
                }
            };
            if (dataList.size() > 1) {
                UploadUtil.UploadReq(uploadUrl, dataList, FabuGoodsdetailActivity.this, mQueue, volleyUploadBack);
            } else {
                if (servicePosition == 0) {
                    sendAddSecondGoodsServer();
                } else if (servicePosition == 1) {
                    sendAddGoodsServer();
                }
            }
        }
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
                    getetGoodsArea = province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName();
                    etGoodsArea.setText(province.getAreaName() + "-" + city.getAreaName() + "-" + county.getAreaName());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fabu_goods_detail_back:
                break;
            case R.id.btn_fabu_goods_submit:
                checkEdit();
                if (MyCheckNet.isNetworkAvailable(FabuGoodsdetailActivity.this)) {
                    sendUploadServer();
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
