package develop.cl.com.crsp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Setting;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private static final String Tag = "SettingActivity";
    private boolean isChange = false;
    private RequestQueue mQueue;
    private Intent mIntent;

    private LinearLayout lyTongzhi;
    private LinearLayout lyTanchuang;
    private LinearLayout lyDelay;

    private TextView tvTongzhi;
    private TextView tvTanchaung;
    private TextView tvDelay;
    private Button btnExit;

    private String gettvTongzhi;
    private String gettvTanchaung;
    private String gettvDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        findViewById();
        initView();
        Log.d(Tag, "onCreate");
    }

    @Override
    protected void findViewById() {
        lyTongzhi = (LinearLayout) this.findViewById(R.id.ly_setting_tongzhi);
        lyTanchuang = (LinearLayout) this.findViewById(R.id.ly_setting_tanchuang);
        lyDelay = (LinearLayout) this.findViewById(R.id.ly_setting_delay);

        tvTongzhi = (TextView) this.findViewById(R.id.tv_setting_tongzhi);
        tvTanchaung = (TextView) this.findViewById(R.id.tv_setting_tanchuang);
        tvDelay = (TextView) this.findViewById(R.id.tv_setting_delay);
        btnExit = (Button) this.findViewById(R.id.btn_setting_exit);
    }

    @Override
    protected void initView() {
        lyTongzhi.setOnClickListener(this);
        lyTanchuang.setOnClickListener(this);
        lyDelay.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        /**
         * 获取配置文件中内容
         */
        Setting setting = getSetting();

        gettvTongzhi = setting.getTongzhi();
        gettvTanchaung = setting.getTanchuang();
        gettvDelay = setting.getDelay();

        tvTongzhi.setText(gettvTongzhi);
        tvTanchaung.setText(gettvTanchaung);
        tvDelay.setText(gettvDelay);

        if ("是".equals(gettvTongzhi)) {
            lyTanchuang.setVisibility(View.VISIBLE);
            lyDelay.setVisibility(View.VISIBLE);
        } else {
            lyTanchuang.setVisibility(View.INVISIBLE);
            lyDelay.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获取配置文件中Basic内容
     */
    protected Setting getSetting() {
        return MySharedPreferences.getSetting(SettingActivity.this);
    }

    /**
     * 显示多按钮对话框
     *
     * @param tv    TextView
     * @param str   提示消息
     * @param title 标题
     */
    private void showMultiBtnDialog(final TextView tv, String str, String title) {
        AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingActivity.this);
        normalDialog.setIcon(R.mipmap.setp);
        normalDialog.setTitle(title).setMessage(str);
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isChange = true;
                        if (tv.getId() == R.id.tv_setting_tongzhi) {
                            lyTanchuang.setVisibility(View.VISIBLE);
                            lyDelay.setVisibility(View.VISIBLE);
                        }
                        tv.setText("是");
                    }
                });
        normalDialog.setNeutralButton("否",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isChange = true;
                        tv.setText("否");
                        if (tv.getId() == R.id.tv_setting_tongzhi) {
                            lyTanchuang.setVisibility(View.INVISIBLE);
                            lyDelay.setVisibility(View.INVISIBLE);
                        }
                    }
                });
        normalDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        normalDialog.show();
    }

    /**
     * 输入对话框
     *
     * @param tv      TextView
     * @param str     默认已输入内容
     * @param message 标题
     */
    private void showInputDialog(final TextView tv, String str, String message) {
        final EditText editText = new EditText(SettingActivity.this);
        editText.setText(str);
        AlertDialog.Builder inputDialog = new AlertDialog.Builder(SettingActivity.this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ly_setting_tongzhi:
                showMultiBtnDialog(tvTongzhi, "是否允许消息通知？", "修改消息通知设置");
                break;
            case R.id.ly_setting_tanchuang:
                showMultiBtnDialog(tvTanchaung, "允许消息通知显示任务栏提醒？", "修改消息弹窗通知设置");
                break;
            case R.id.ly_setting_delay:
                showInputDialog(tvDelay, tvDelay.getText().toString(), "修改消息刷新间隔");
                break;
            case R.id.btn_setting_exit:
                showNormalDialog();
                break;
            default:
                break;
        }
    }

    private void showNormalDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SettingActivity.this);
        normalDialog.setTitle("确定要退出登录吗？");
        normalDialog.setPositiveButton("是",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MySharedPreferences.setLogin(SettingActivity.this, "no");
                        mIntent = new Intent(SettingActivity.this, LoginActivity.class);
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


    /**
     * 更新内容至服务器和本地
     */
    protected void sendUpdateServer() {
        mQueue = Volley.newRequestQueue(SettingActivity.this);
        Setting setting = new Setting();
        gettvTongzhi = tvTongzhi.getText().toString();
        gettvTanchaung = tvTanchaung.getText().toString();
        gettvDelay = tvDelay.getText().toString();

        setting.setTongzhi(gettvTongzhi);
        setting.setTanchuang(gettvTanchaung);
        setting.setDelay(gettvDelay);
//        Setting preSetting = MySharedPreferences.getSetting(SettingActivity.this);

        setting.setSettingid(getSetting().getSettingid());
        MySharedPreferences.setSetting(SettingActivity.this, setting);
        String[] strSetting = new String[]{"settingid", "tongzhi", "tanchuang", "delay"};
        //创建回调接口并实例化方法
        VolleyCallback volleyCallback = new VolleyCallback() {
            @Override
            //回调内容result
            public void onSuccessResponse(String result) {
                Log.d("callBack result", result);
            }
        };
        String url = ServerInformation.URL + "setting/update";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, url, MyList.strList(strSetting, setting), volleyCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(Tag, "onDestroy");
        if (isChange) {
            Log.i(Tag, "isChange");
            if (MyCheckNet.isNetworkAvailable(SettingActivity.this)) {
                sendUpdateServer();
            } else {
                DisPlay("请检查您的网络连接");
            }
        }
    }
}
