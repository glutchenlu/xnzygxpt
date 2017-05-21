package develop.cl.com.crsp.activity.fenleiactivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.WebChar;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.image.CircleImageView;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class WebCharListActivity extends BaseActivity implements View.OnClickListener {

    private String resultMap;
    private Intent mIntent;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;
    private static final String Tag = "WebCharListActivity";
    private Button btnTop;
    private TextView tvET;
    private int informationid;
    private String title;

    private int listsize;
    private int count = 0;
    private ListView lvWebChar;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_webchar_list);
        findViewById();
        initView();
    }


    @Override
    protected void findViewById() {
        btnTop = (Button) this.findViewById(R.id.btn_showwebchar_top);
        lvWebChar = (ListView) this.findViewById(R.id.lv_webchar);
        tvET = (TextView) this.findViewById(R.id.tv_webchar_et);
    }

    @Override
    protected void initView() {
        tvET.setOnClickListener(this);

        mIntent = getIntent();
        resultMap = mIntent.getStringExtra("resultMap");
        informationid = mIntent.getIntExtra("informationid", -1);
        title = mIntent.getStringExtra("title");
        JSONObject jsonMap = JSON.parseObject(resultMap);
        List<WebChar> wlist = JSON.parseArray(jsonMap.get("webchar").toString(), WebChar.class);
        List<Basic> blist = JSON.parseArray(jsonMap.get("basic").toString(), Basic.class);
        btnTop.setText(title);
        datalist = new ArrayList<Map<String, Object>>();
        listBeanToMapPic(wlist, blist);
    }

    /**
     * 将list中的bean转换成map
     *
     * @param olist list
     * @return
     */
    protected void listBeanToMapPic(List<?> olist, List<?> blist) {

        mQueue = Volley.newRequestQueue(WebCharListActivity.this);
        listsize = olist.size();
        int sort = 0;
        Log.d(Tag, "listsize:" + listsize);
        for (int i = 0; i < listsize; i++) {
            final Map<String, Object> map = MyList.transBean2Map(olist.get(i));
            final Map<String, Object> mapb = MyList.transBean2Map(blist.get(i));
            sort++;
            map.put("sort", sort);
            map.put("name", mapb.get("name"));
            map.put("showtime", StringUtils.substring(map.get("release_time").toString(), 5, 16));
            /**
             * 根据地址请求服务器图片
             */
            ImageRequest imageRequest = new ImageRequest(mapb.get("picture").toString(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            count++;
                            Log.d(Tag, "count:" + count);
                            map.put("showpic", response);
                            datalist.add(map);
                            if (count == listsize) {
                                Collections.sort(datalist, new Comparator<Map<String, Object>>() {
                                    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                        return (int) o1.get("sort") - (int) o2.get("sort");
                                    }
                                });
                                /**
                                 * 构造数据填充listview
                                 */
                                String[] mapName = new String[]{"showpic", "name", "showtime", "content"};
                                int[] controlId = new int[]{R.id.iv_webchar_pic, R.id.tv_webchar_name
                                        , R.id.tv_webchar_time, R.id.tv_webchar_countent};
                                sadapter = new SimpleAdapter(WebCharListActivity.this, datalist
                                        , R.layout.lv_webchar_item, mapName, controlId);
                                sadapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                                    /**
                                     * 重写adapter设置图片
                                     * @param view
                                     * @param data
                                     * @param textRepresentation
                                     * @return
                                     */
                                    @Override
                                    public boolean setViewValue(View view, Object data,
                                                                String textRepresentation) {
                                        if (view instanceof CircleImageView && data instanceof Bitmap) {
                                            CircleImageView iv = (CircleImageView) view;
                                            iv.setImageBitmap((Bitmap) data);
                                            return true;
                                        }
                                        return false;
                                    }
                                });
                                lvWebChar.setAdapter(sadapter);
                            }
                        }
                    }, 50, 50, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.getMessage());
                }
            });
            mQueue.add(imageRequest);
        }
    }

    protected void addWebchar(String etStr) {
        mQueue = Volley.newRequestQueue(WebCharListActivity.this);
        WebChar webchar = new WebChar();
        webchar.setInformationid(informationid);
        webchar.setUserid(MySharedPreferences.getUserID(WebCharListActivity.this));
        webchar.setContent(etStr);
        String[] str = new String[]{"informationid", "userid", "content"};
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
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                        //更新添加后的界面
                        flushAdd(jsonMap);
                    } else if (jsonMap.get("returnCode").toString().equals("2")) {
                        //显示提示消息
                        DisPlay(jsonMap.get("returnString").toString());
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "webchar/add";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(str, webchar), volleyCallback);
    }

    protected void flushAdd(JSONObject jsonMap) {
        WebChar locwebchar = JSON.parseObject(jsonMap.get("webchar").toString(), WebChar.class);
        Basic locbasic = JSON.parseObject(jsonMap.get("basic").toString(), Basic.class);
        final Map<String, Object> map = MyList.transBean2Map(locwebchar);
        final Map<String, Object> mapb = MyList.transBean2Map(locbasic);
        map.put("name", mapb.get("name"));
        map.put("showtime", StringUtils.substring(map.get("release_time").toString(), 5, 16));
        /**
         * 根据地址请求服务器图片
         */
        ImageRequest imageRequest = new ImageRequest(mapb.get("picture").toString(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        map.put("showpic", response);
                        datalist.add(map);
                        sadapter.notifyDataSetChanged();
                    }
                }, 50, 50, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        });
        mQueue.add(imageRequest);
    }

    private void showInputDialog() {
        final EditText editText = new EditText(WebCharListActivity.this);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(WebCharListActivity.this);
        inputDialog.setTitle("输入评论内容").setView(editText);
        inputDialog.setPositiveButton("评论",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addWebchar(editText.getText().toString());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_webchar_et:
                showInputDialog();
                break;
            default:
                break;
        }
    }
}
