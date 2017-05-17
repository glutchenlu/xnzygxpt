package develop.cl.com.crsp.activity.fenleiactivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.image.CircleImageView;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class ShowDetailFuwuActivity extends BaseActivity implements View.OnClickListener {

    private Map<String, Object> map;
    private int classposition;
    private Intent mIntent;
    private RequestQueue mQueue;
    private static final String Tag = "ShowDetailGoodsActivity";

    private Button btnCall;
    private TextView tvStation;
    private TextView tvStationName;
    private TextView tvReceivetime;
    private TextView tvPrice;
    private TextView tvShoucang;
    private TextView tvDegree;
    private TextView tvDetail;
    private TextView tvArea;
    private TextView tvName;
    private CircleImageView ivPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_fuwu);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {

        tvPrice = (TextView) this.findViewById(R.id.tv_fuwudetail_price);
        tvStation = (TextView) this.findViewById(R.id.tv_fuwudetail_station);
        tvStationName = (TextView) this.findViewById(R.id.tv_fuwudetail_stationname);
        tvReceivetime = (TextView) this.findViewById(R.id.tv_fuwudetail_receivetime);
        tvShoucang = (TextView) this.findViewById(R.id.tv_fuwu_shoucang);
        tvDegree = (TextView) this.findViewById(R.id.tv_fuwudetail_degree);
        tvDetail = (TextView) this.findViewById(R.id.tv_fuwudetail_detail);
        tvArea = (TextView) this.findViewById(R.id.tv_fuwudetail_area);
        tvName = (TextView) this.findViewById(R.id.tv_fuwu_user_name);
        ivPic = (CircleImageView) this.findViewById(R.id.iv_fuwu_user_pic);
        btnCall = (Button) this.findViewById(R.id.btn_wrokdetail_calluser);
    }

    @Override
    protected void initView() {
//从Intent获得额外信息
        mIntent = this.getIntent();
        map = (Map<String, Object>) mIntent.getSerializableExtra("map");
        classposition = mIntent.getIntExtra("classposition", -1);
        mQueue = Volley.newRequestQueue(ShowDetailFuwuActivity.this);
        /**
         * 请求数据
         */
        String userUrl = ServerInformation.URL + "user/findbyid?userid=" + map.get("userid");
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
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
                        JSONObject beanMap = JSON.parseObject(jsonMap.get("returnBean").toString());
                        Basic locbasic = JSON.parseObject(beanMap.get("basic").toString(), Basic.class);
                        //卖家名称
                        if (classposition == 0) {
                            tvStationName.setText("快递商家");
                            tvStation.setText(map.get("merchant").toString());
                        } else if (classposition == 1) {
                            tvStation.setText(map.get("station").toString());
                        }
                        tvName.setText(locbasic.getName());
                        tvReceivetime.setText(map.get("receive_time").toString());
                        tvPrice.setText(map.get("price").toString());
                        tvDegree.setText(map.get("degree").toString());
                        tvDetail.setText(map.get("detail").toString());
                        tvArea.setText(map.get("area").toString());
                        ImageRequest imageRequest = new ImageRequest(locbasic.getPicture(),
                                new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap response) {
                                        //获得卖家头像信息
                                        ivPic.setImageBitmap(response);
                                    }
                                }, 50, 50, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("error", error.getMessage());
                            }
                        });
                        mQueue.add(imageRequest);
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        /**
         * 请求卖家信息
         */
        DFVolley.NoMReq(mQueue, userUrl, volleyCallback);
    }

    @Override
    public void onClick(View v) {

    }
}
