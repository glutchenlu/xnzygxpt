package develop.cl.com.crsp.activity;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Resume;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;


public class ShowJianliListActivity extends BaseActivity implements View.OnClickListener {
    private static final String Tag = "ShowJianliListActivity";
    private Intent mIntent;
    private String resultMap;
    private int count;

    private RequestQueue mQueue;

    private Button btnTop;
    private TextView tvAdd;
    private ListView lvUserJianli;
    private SimpleAdapter sadapter;
    private List<Map<String, Object>> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_fabu);
        findViewById();
        initView();
    }

    @Override
    protected void findViewById() {
        lvUserJianli = (ListView) this.findViewById(R.id.lv_userfabu);
        btnTop = (Button) this.findViewById(R.id.btn_userfabu_top);
        tvAdd = (TextView) this.findViewById(R.id.tv_userfabu_add);
    }

    @Override
    protected void initView() {
        tvAdd.setOnClickListener(this);
        btnTop.setText("我的简历");
        mIntent = this.getIntent();
        resultMap = mIntent.getStringExtra("resultMap");
        JSONObject jsonMap = JSON.parseObject(resultMap);
        count = (Integer) jsonMap.get("count");
        List<Resume> rlist = JSON.parseArray(jsonMap.get("resume").toString(), Resume.class);

        datalist = new ArrayList<Map<String, Object>>();
        listBeanToMap(rlist);
        String[] mapName = new String[]{"resumename", "write_time"};
        int[] controlId = new int[]{R.id.lv_jianli_item_resumename, R.id.lv_jianli_item_time};
        sadapter = new SimpleAdapter(this, datalist, R.layout.lv_jianli_item, mapName, controlId);
        lvUserJianli.setAdapter(sadapter);
        //单击监听
        lvUserJianli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int locresumeid = (Integer) datalist.get(position).get("resumeid");
                showListDialog(locresumeid);
            }
        });
        //长按监听
        lvUserJianli.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showNormalDialog(datalist.get(position), position);
                return true;
            }
        });
    }

    private void showNormalDialog(final Map<String, Object> delMap, final int locposition) {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(ShowJianliListActivity.this);
        normalDialog.setTitle("确定要删除这条收藏记录吗？");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int locresumeid = (Integer) delMap.get("resumeid");
                        delJianli(locresumeid, locposition);
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

    protected void delJianli(int id, final int locposition) {
        mQueue = Volley.newRequestQueue(ShowJianliListActivity.this);
        //创建回调接口并实例化方法
        VolleyCallback delVolleyCallback = new VolleyCallback() {
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
                        datalist.remove(locposition);
                        MySharedPreferences.setResumeCount(ShowJianliListActivity.this, datalist.size());
                        sadapter.notifyDataSetChanged();
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "resume/del?resumeid=" + id;
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, url, delVolleyCallback);
    }

    private void showListDialog(final int locresumeid) {
        final String[] items = {"查看", "修改", "取消"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(ShowJianliListActivity.this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // 0、查看 1、修改
                        SendQueryByIDServer(locresumeid, which);
                        break;
                    case 1:
                        // 0、查看 1、修改
                        SendQueryByIDServer(locresumeid, which);
                        break;
                    case 2:
                        break;
                    default:
                }
            }
        });
        listDialog.show();
    }

    protected void SendQueryByIDServer(int locresumeid, final int showorupdate) {
        mQueue = Volley.newRequestQueue(ShowJianliListActivity.this);
        //创建回调接口并实例化方法
        VolleyCallback delVolleyCallback = new VolleyCallback() {
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
                        if (showorupdate == 0) {
                            mIntent = new Intent(ShowJianliListActivity.this, ShowJianliDetailActivity.class);
                            mIntent.putExtra("resultMap", jsonObject.get("resultMap").toString());
                            mIntent.putExtra("dotype", "查看");
                            startActivity(mIntent);
                        } else if (showorupdate == 1) {
                            mIntent = new Intent(ShowJianliListActivity.this, JianliActivity.class);
                            mIntent.putExtra("resultMap", jsonObject.get("resultMap").toString());
                            mIntent.putExtra("dotype", "修改");
                            startActivity(mIntent);
                        }
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String url = ServerInformation.URL + "resume/querybyid?resumeid=" + locresumeid;
        //调用自定义的Volley函数
        DFVolley.NoMReq(mQueue, url, delVolleyCallback);
    }

    /**
     * 将list中的bean转换成map
     *
     * @param olist
     */
    protected void listBeanToMap(List<?> olist) {
        for (Object list : olist) {
            final Map<String, Object> map = MyList.transBean2Map(list);
            datalist.add(map);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_userfabu_add:
                if (count == 3) {
                    DisPlay("您已经有三份简历，不能再添加简历 ！");
                } else {
                    mIntent = new Intent(ShowJianliListActivity.this, JianliActivity.class);
                    mIntent.putExtra("dotype", "新增");
                    startActivity(mIntent);
                }
                break;
            default:
                break;
        }
    }
}
