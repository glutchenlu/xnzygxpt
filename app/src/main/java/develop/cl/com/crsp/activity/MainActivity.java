package develop.cl.com.crsp.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import develop.cl.com.crsp.BaseActivity;
import develop.cl.com.crsp.JavaBean.Message;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.adapter.MainFragmentAdapter;
import develop.cl.com.crsp.fragment.FabuFragment;
import develop.cl.com.crsp.fragment.FenleiFragment;
import develop.cl.com.crsp.fragment.GerenFragment;
import develop.cl.com.crsp.fragment.XiaoxiFragment;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyCheckNet;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

import static develop.cl.com.crsp.R.mipmap.add;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private List<Fragment> fragList;
    private ViewPager vp_main;
    //    private int position;
    private BottomNavigationBar bottomBar;
    private boolean isLogin;
    private static final String Tag = "MainActivity";

    static Timer timer = null;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;
    private int count = 0;
    private BadgeItem numberBadItem;
    private BottomNavigationItem xiaoxiBottomNavigationItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymain);
        findViewById();
        initView();
        Log.d(Tag, "onCreate");
    }

    @Override
    protected void findViewById() {
        vp_main = (ViewPager) findViewById(R.id.pager);
        bottomBar = (BottomNavigationBar) findViewById(R.id.bottom_bar);
    }

    @Override
    protected void initView() {
        mQueue = Volley.newRequestQueue(MainActivity.this);
        int delay = 0;
        if (null == timer) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                /**
                 * 登录状态并且网络状态可用
                 */
                if (checkLogin()) {
                    if (MyCheckNet.isNetworkAvailable(MainActivity.this)) {
                        //允许消息通知
                        if ("是".equals(MySharedPreferences
                                .getSetting(MainActivity.this).getTongzhi())) {
                            LocQueryServer();
                        }
                    }
                }
            }
        }, delay, Integer.parseInt(MySharedPreferences
                .getSetting(MainActivity.this).getDelay()) * 1000);

        numberBadItem = new BadgeItem().setBorderWidth(4)
                .setBackgroundColorResource(R.color.red)
                .setText(MySharedPreferences.getMessageCount(MainActivity.this) + "")
                .setHideOnSelect(false);
        xiaoxiBottomNavigationItem = new BottomNavigationItem(R.mipmap.message, "消息")
                .setInActiveColor(R.color.black).setBadgeItem(numberBadItem).setActiveColor("#32CD32");
        bottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomBar.addItem(new BottomNavigationItem(R.mipmap.fenlei, "分类")
                .setInActiveColor(R.color.black).setActiveColor("#32CD32"))
                .addItem(new BottomNavigationItem(add, "发布")
                        .setInActiveColor(R.color.black).setActiveColor("#32CD32"))
                .addItem(xiaoxiBottomNavigationItem)
                .addItem(new BottomNavigationItem(R.mipmap.person, "个人")
                        .setInActiveColor(R.color.black).setActiveColor("#32CD32"))
                .setFirstSelectedPosition(0)
                .initialise();
        if (MySharedPreferences.getMessageCount(MainActivity.this) == 0) {
            numberBadItem.hide();
        }
        /**
         * 获得fragment数组
         */
        fragList = getData();
        /**
         * 绑定
         */
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager(), fragList);
        vp_main.setAdapter(adapter);
        bottomBar.setTabSelectedListener(this);
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private List<Fragment> getData() {
        fragList = new ArrayList<Fragment>();
        FenleiFragment fenlei = new FenleiFragment();
        FabuFragment fabu = new FabuFragment();
        XiaoxiFragment xiaoxi = new XiaoxiFragment();
        GerenFragment geren = new GerenFragment();
        fragList.add(fenlei);
        fragList.add(fabu);
        fragList.add(xiaoxi);
        fragList.add(geren);
        return fragList;
    }

    @Override
    public void onTabSelected(int position) {
        if (position == 2) {
            Log.i("positon", position + "");
            xiaoxiBottomNavigationItem.setBadgeItem(numberBadItem);
            numberBadItem.hide(true);
        }
        Log.d("positon", position + "");
        vp_main.setCurrentItem(position, false);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    protected boolean checkLogin() {
        isLogin = false;
        if (MySharedPreferences.getLogin(MainActivity.this).equals("yes")) {
            isLogin = true;
        }
        return isLogin;
    }

    protected void LocQueryServer() {
        Message message = new Message();
        message.setMessage_time(MySharedPreferences.getlastQueryMessageTime(MainActivity.this));
        MySharedPreferences.setlastQueryMessageTime(MainActivity.this);
        message.setUserid(MySharedPreferences.getUserID(MainActivity.this));
        String[] str = new String[]{"userid", "message_time"};
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
                        count = jsonMap.getInteger("count");
                        Log.i("count", count + "");
                        MySharedPreferences.setMessageCount(MainActivity.this, count);
                        if (count > 0) {
                            numberBadItem.show(false);
                            numberBadItem.setText(count + "");
                            if ("是".equals(MySharedPreferences
                                    .getSetting(MainActivity.this).getTanchuang())) {
                                tongzhi();
                            }
                        }
                    }
                    Log.d("returnCode", jsonMap.get("returnCode").toString());
                }
            }
        };
        String locUrl = ServerInformation.URL + "message/querybymessage";
        //调用自定义的Volley函数
        DFVolley.VolleyUtilWithGet(1, mQueue, locUrl, MyList.strList(str, message), volleyCallback);
    }

    protected void tongzhi() {
        NotificationManager mn = (NotificationManager) MainActivity.this.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(MainActivity.this);
        Intent notificationIntent = new Intent(MainActivity.this, XiaoxiFragment.class);//点击跳转位置
        PendingIntent contentIntent = PendingIntent.getActivity(MainActivity.this, 0, notificationIntent, 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("新消息通知"); //测试通知栏标题
        builder.setContentText("您有" + count + "条新的消息"); //下拉通知啦内容
        builder.setContentTitle("您有" + count + "条新的消息");//下拉通知栏标题
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        mn.notify((int) System.currentTimeMillis(), notification);
    }
}
