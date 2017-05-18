package develop.cl.com.crsp.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Timer;
import java.util.TimerTask;

import develop.cl.com.crsp.JavaBean.Message;
import develop.cl.com.crsp.MyApplication;
import develop.cl.com.crsp.R;
import develop.cl.com.crsp.fragment.XiaoxiFragment;
import develop.cl.com.crsp.myutil.DFVolley;
import develop.cl.com.crsp.myutil.MyList;
import develop.cl.com.crsp.myutil.MySharedPreferences;
import develop.cl.com.crsp.myutil.ServerInformation;
import develop.cl.com.crsp.myutil.VolleyCallback;

public class PushService extends Service {

    static Timer timer = null;
    private VolleyCallback volleyCallback;
    private RequestQueue mQueue;
    private MyApplication myapp;
    private Intent mIntent;
    private int count = 0;

    //    //清除通知
//    public static void cleanAllNotification() {
//        NotificationManager mn = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
//        mn.cancelAll();
//        if (timer != null) {
//            timer.cancel();
//            timer = null;
//        }
//    }
//
//    //添加通知
//    public static void addNotification(int delayTime, String tickerText, String contentTitle, String contentText) {
//        Intent intent = new Intent(MainActivity.getContext(), PushService.class);
//        intent.putExtra("delayTime", delayTime);
//        intent.putExtra("tickerText", tickerText);
//        intent.putExtra("contentTitle", contentTitle);
//        intent.putExtra("contentText", contentText);
//        MainActivity.getContext().startService(intent);
//    }
    protected void LocQueryServer() {
        Message message = new Message();
        message.setMessage_time(MySharedPreferences.getlastQueryMessageTime(PushService.this));
        message.setUserid(MySharedPreferences.getUserID(PushService.this));
        MySharedPreferences.setlastQueryMessageTime(PushService.this);
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
//                        JSONObject jsonMessage = JSON.parseObject(jsonMap.get("returnBean").toString());
//                        myapp.setxiaoxiMap(jsonMessage.getString("message"));
                        count = jsonMap.getInteger("count");
                        MySharedPreferences.setMessageCount(PushService.this, count);
                        if (count > 0) {
                            tongzhi();
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

    protected void DisPlay(String content) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    public void onCreate() {
        Log.e("addNotification", "===========create=======");
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {

//        long period = 24 * 60 * 60 * 1000; //24小时一个周期
        mIntent = intent;
//        if (myapp == null) {
//            myapp = (MyApplication) this.getApplicationContext();
//        }
        mQueue = Volley.newRequestQueue(PushService.this);
        long period = 3 * 1000;
        int delay = intent.getIntExtra("delayTime", 0);
        if (null == timer) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                LocQueryServer();
            }
        }, delay, period);

        return super.onStartCommand(intent, flags, startId);
    }

    protected void tongzhi() {
        NotificationManager mn = (NotificationManager) PushService.this.getSystemService(NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(PushService.this);
        Intent notificationIntent = new Intent(PushService.this, XiaoxiFragment.class);//点击跳转位置
        PendingIntent contentIntent = PendingIntent.getActivity(PushService.this, 0, notificationIntent, 0);
        builder.setContentIntent(contentIntent);
        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setTicker(mIntent.getStringExtra("新消息通知")); //测试通知栏标题
//        builder.setContentText(mIntent.getStringExtra("您有" + count + "条新的消息")); //下拉通知啦内容
//        builder.setContentTitle(mIntent.getStringExtra("您有" + count + "条新的消息"));//下拉通知栏标题
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        Notification notification = builder.build();
        mn.notify((int) System.currentTimeMillis(), notification);
    }

    @Override
    public void onDestroy() {
        Log.e("addNotification", "===========destroy=======");
        super.onDestroy();
    }
}
