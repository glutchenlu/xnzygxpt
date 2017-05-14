package develop.cl.com.crsp.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Map;

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.adapter.ViewPagerNetAdapter;
import develop.cl.com.crsp.image.BitmapCache;
import develop.cl.com.crsp.image.CircleImageView;
import develop.cl.com.crsp.util.DFVolley;
import develop.cl.com.crsp.util.ServerInformation;
import develop.cl.com.crsp.util.VolleyCallback;


public class ShowDetailGoodsActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    private Map<String, Object> map;
    private Intent mIntent;
    private RequestQueue mQueue;
    private String UrlStr[];
    private static final String Tag = "ShowDetailGoodsActivity";
    private ViewPager mViewPage;

    private LinearLayout ll_container;//小圆点容器
    private int mCurrentIndex = 0;//当前小圆点的位置

    private TextView tvTitle;
    private TextView tvSource;
    private TextView tvSal;
    private TextView tvShoucang;
    private TextView tvDegree;
    private TextView tvDetail;
    private TextView tvArea;
    private TextView tvName;
    private CircleImageView ivPic;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_detail_goods);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        mViewPage = (ViewPager) findViewById(R.id.show_detail_viewpage);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

        tvTitle = (TextView) this.findViewById(R.id.tv_goodsdetail_title);
        tvSource = (TextView) this.findViewById(R.id.tv_goodsdetail_source);
        tvSal = (TextView) this.findViewById(R.id.tv_goodsdetail_sal);
        tvShoucang = (TextView) this.findViewById(R.id.tv_goods_shoucang);
        tvDegree = (TextView) this.findViewById(R.id.tv_goodsdetail_degree);
        tvDetail = (TextView) this.findViewById(R.id.tv_goodsdetail_detail);
        tvArea = (TextView) this.findViewById(R.id.tv_goodsdetail_area);
        tvName = (TextView) this.findViewById(R.id.tv_goods_user_name);
        ivPic = (CircleImageView) this.findViewById(R.id.iv_goods_user_pic);
        btnCall = (Button) this.findViewById(R.id.btn_goodsdetail_calluser);
    }

    @Override
    protected void initView() {

        //从Intent获得额外信息
        mIntent = this.getIntent();
        map = (Map<String, Object>) mIntent.getSerializableExtra("map");
        mQueue = Volley.newRequestQueue(ShowDetailGoodsActivity.this);
        UrlStr = map.get("pic").toString().split(",");
        setViewPageArr();

        /**
         * 请求数据
         */
        String userUrl = ServerInformation.URL + "user/findbyid?userid=" + map.get("userid");
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
                if ("".equals(result)) {
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
                        tvName.setText(locbasic.getName());
                        tvTitle.setText(map.get("title").toString());
                        tvSource.setText(map.get("source").toString());
                        tvSal.setText(map.get("price").toString());
                        tvSource.setText(map.get("source").toString());
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

    /**
     * 设置viewpage内容
     */
    protected void setViewPageArr() {
        /**
         * 声明NetworkImageView控件数组
         */
        ArrayList<NetworkImageView> list = new ArrayList<NetworkImageView>();
        //获取屏幕的高度,取其三分之一
        int height = getWindowManager().getDefaultDisplay().getHeight();
        ViewGroup.LayoutParams layoutParams = mViewPage.getLayoutParams();
        int viewPageHeight = height * 3 / 7;
        //设置ViewPager的高度为整个屏幕的3分之一
        layoutParams.height = viewPageHeight;
        /**
         * 构造ImageLoader对象
         */
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        //遍历URL
        for (int i = 0; i < UrlStr.length; i++) {
            NetworkImageView networkImageView = new NetworkImageView(this);
            /**
             * 通过NetworkImageView控件进行网络请求加载图片
             */
            networkImageView.setImageUrl(UrlStr[i], imageLoader);
            networkImageView.setMaxHeight(viewPageHeight);

            /**
             * 小圆点索引
             */
            ImageView dot = new ImageView(this);
            if (i == mCurrentIndex) {
                dot.setImageResource(R.mipmap.red_dot);//设置当前页的圆点
            } else {
                dot.setImageResource(R.mipmap.white_dot);//其余页的圆点
            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout
                    .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.leftMargin = 5;//设置圆点边距
            }
            dot.setLayoutParams(params);
            ll_container.addView(dot);//将圆点添加到容器中

            list.add(networkImageView);
        }

        ViewPagerNetAdapter adapter = new ViewPagerNetAdapter(list, this);
        mViewPage.setAdapter(adapter);
        mViewPage.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        for (int i = 0; i < ll_container.getChildCount(); i++) {
            ImageView imageView = (ImageView) ll_container.getChildAt(i);
            if (i == position) {
                imageView.setImageResource(R.mipmap.red_dot);
            } else {
                imageView.setImageResource(R.mipmap.white_dot);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
