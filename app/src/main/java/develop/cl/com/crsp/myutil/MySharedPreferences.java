package develop.cl.com.crsp.myutil;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Date;

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Dating;
import develop.cl.com.crsp.JavaBean.Setting;

/**
 * 使用SharedPreferences保存临时状态数据至客户端本地，可以让某些数据在请求服务器情况下获取，减少服务器请求次数
 */
public class MySharedPreferences {
    /**
     * 获取登录状态
     *
     * @param context 上下文
     * @return 登录状态字符串，yes已登录，no未登录
     */
    public static String getLogin(Context context) {
        SharedPreferences spf = context.getSharedPreferences("login", 0);
        return spf.getString("isLogin", "no");
    }

    /**
     * 设置登录状态
     *
     * @param context 上下文
     * @param str     设置登录状态的字符串，yes已登录，no未登录
     */
    public static void setLogin(Context context, String str) {
        SharedPreferences spf = context.getSharedPreferences("login", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("isLogin", str);
        editor.apply();
    }

    /**
     * 设置basic表内的school信息
     *
     * @param context 上下文
     * @param str     school信息
     */
    public static void setSchool(Context context, String str) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("school", str);
        editor.apply();
    }

    /**
     * 获取basic表内的school信息
     *
     * @param context 上下文
     * @return school信息
     */
    public static String getSchool(Context context) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        return spf.getString("school", "0");
    }

    /**
     * 获取当前登录用户的账户名
     *
     * @param context 上下文
     * @return 当前登录用户的账户名
     */
    public static String getUserID(Context context) {
        SharedPreferences spf = context.getSharedPreferences("UserID", 0);
        return spf.getString("UserID", "0");
    }

    /**
     * 设置当前登录用户的账户名
     *
     * @param context 上下文
     * @param UserID  当前登录用户的账户名
     */
    public static void setUserID(Context context, String UserID) {
        SharedPreferences spf = context.getSharedPreferences("UserID", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("UserID", UserID);
        editor.apply();
    }

    /**
     * 设置客户端本地用户头像图片地址
     *
     * @param context  上下文
     * @param photoUrl 客户端本地用户头像图片地址URL
     */
    public static void setPhoto(Context context, String photoUrl) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("picture", photoUrl);
        editor.apply();
    }

    /**
     * 设置当前用户basic、dating数据
     *
     * @param context 上下文
     * @param basic   basic数据，无头像图片URL数据
     * @param dating  dating数据
     */
    public static void setPersonData(Context context, Basic basic, Dating dating) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        SharedPreferences.Editor editor = spf.edit();
//        editor.putString("picture", basic.getPicture());
        editor.putString("school", basic.getSchool());
        editor.putString("stuid", basic.getStuid());
        editor.putString("name", basic.getName());
        editor.putString("sex", basic.getSex());
        editor.putString("tel", basic.getTel());
        editor.putString("birthday", basic.getBirthday());
        editor.putString("email", basic.getEmail());

        editor.putString("professional", dating.getProfessional());
        editor.putString("business", dating.getBusiness());
        editor.putString("state", dating.getState());
        editor.putString("signature", dating.getSignature());
        editor.putString("hometown", dating.getHometown());
        editor.apply();
    }

    /**
     * 设置basic数据，用于头像图片只知道服务器URL情况下；
     * 先通过请求服务器，保存头像图片至本地，在获取本地头像图片URL进行保存
     *
     * @param context 上下文
     * @param basic   basic数据，头像图片URL数据为服务器内的地址
     */
    public static void setBasic(final Context context, final Basic basic) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        final SharedPreferences.Editor editor = spf.edit();
        //通过头像图片URL请求服务器，保存图片至本地，获得本地图片URL，再保存
        ImageRequest imageRequest = new ImageRequest(basic.getPicture(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        //保存图片至本地
                        basic.setPicture(BitmapUtil.saveImage(context, response));
                        Log.i("Picture", basic.getPicture());
                        editor.putString("basicid", basic.getBasicid() + "");
                        //保存本地头像图片地址URL
                        editor.putString("picture", basic.getPicture());
                        editor.putString("school", basic.getSchool());
                        editor.putString("stuid", basic.getStuid());
                        editor.putString("name", basic.getName());
                        editor.putString("credit", basic.getCredit() + "");
                        editor.putString("sex", basic.getSex());
                        editor.putString("tel", basic.getTel());
                        editor.putString("birthday", basic.getBirthday());
                        editor.putString("email", basic.getEmail());
                        editor.apply();
                    }
                }, 100, 100, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.getMessage());
            }
        });
        mQueue.add(imageRequest);
    }

    /**
     * 设置dating数据
     *
     * @param context 上下文
     * @param dating  dating数据
     */
    public static void setDating(Context context, Dating dating) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        SharedPreferences.Editor editor = spf.edit();

        editor.putString("dating", dating.getDating() + "");
        editor.putString("professional", dating.getProfessional());
        editor.putString("business", dating.getBusiness());
        editor.putString("state", dating.getState());
        editor.putString("signature", dating.getSignature());
        editor.putString("hometown", dating.getHometown());
        editor.apply();
    }

    /**
     * 获取bsaic数据
     *
     * @param context 上下文
     * @return basic数据
     */
    public static Basic getBasicData(Context context) {
        Basic basic = new Basic();
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        basic.setPicture(spf.getString("picture", "0"));
        basic.setSchool(spf.getString("school", "0"));
        basic.setStuid(spf.getString("stuid", "0"));
        basic.setBasicid(Integer.parseInt(spf.getString("basicid", "0")));
        basic.setCredit(Integer.parseInt(spf.getString("credit", "0")));
        basic.setName(spf.getString("name", "0"));
        basic.setSex(spf.getString("sex", "0"));
        basic.setTel(spf.getString("tel", "0"));
        basic.setBirthday(spf.getString("birthday", "0"));
        basic.setEmail(spf.getString("email", "0"));
        return basic;
    }

    /**
     * 获取dating数据
     *
     * @param context 上下文
     * @return dating数据
     */
    public static Dating getDatingData(Context context) {
        Dating dating = new Dating();
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        dating.setDating(Integer.parseInt(spf.getString("dating", "0")));
        dating.setProfessional(spf.getString("professional", "0"));
        dating.setBusiness(spf.getString("business", "0"));
        dating.setState(spf.getString("state", "0"));
        dating.setSignature(spf.getString("signature", "0"));
        dating.setHometown(spf.getString("hometown", "0"));
        return dating;
    }

    /**
     * 获取最后一次查询消息的时间，用于自动获取未读消息设置
     *
     * @param context 上下文
     * @return 时间
     */
    public static String getlastQueryMessageTime(Context context) {
        SharedPreferences spf = context.getSharedPreferences("MessageTime", 0);
        return spf.getString("lastQueryMessageTime", "2000-11-11 00:00:00");
    }

    /**
     * 设置最后一次查询消息的时间，用于自动获取未读消息设置
     *
     * @param context 上下文
     */
    public static void setlastQueryMessageTime(Context context) {
        SharedPreferences spf = context.getSharedPreferences("MessageTime", 0);
        SharedPreferences.Editor editor = spf.edit();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        Log.i("sdf", sdf.format(now));
        editor.putString("lastQueryMessageTime", sdf.format(now));
        editor.apply();
    }

    /**
     * 获取上一次查询消息未读数
     *
     * @param context 上下文
     * @return 上一次查询消息未读数
     */
    public static int getMessageCount(Context context) {
        SharedPreferences spf = context.getSharedPreferences("MessageCount", 0);
        return spf.getInt("count", 0);
    }

    /**
     * 设置本次未读消息未读数
     *
     * @param context 上下文
     * @param count   消息未读数目
     */
    public static void setMessageCount(Context context, int count) {
        SharedPreferences spf = context.getSharedPreferences("MessageCount", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("count", count);
        editor.apply();
    }

    /**
     * 获取当前用户设置内容参数；
     * 默认为允许通知，允许通知弹窗，获取消息间隔30秒一次
     *
     * @param context 上下文
     * @return 当前用户设置内容
     */
    public static Setting getSetting(Context context) {
        Setting setting = new Setting();
        SharedPreferences spf = context.getSharedPreferences("Setting", 0);
        setting.setSettingid(Integer.parseInt(spf.getString("settingid", "0")));
        setting.setTongzhi(spf.getString("tongzhi", "是"));
        setting.setTanchuang(spf.getString("tanchuang", "是"));
        setting.setDelay(spf.getString("delay", "30"));
        return setting;
    }

    /**
     * 设置当前用户设置内容参数
     *
     * @param context 上下文
     * @param setting 当前用户设置内容参数
     */
    public static void setSetting(Context context, Setting setting) {
        SharedPreferences spf = context.getSharedPreferences("Setting", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("settingid", setting.getSettingid() + "");
        editor.putString("tongzhi", setting.getTongzhi());
        editor.putString("tanchuang", setting.getTanchuang());
        editor.putString("delay", setting.getDelay());
        editor.apply();
    }

    /**
     * 获取当前用户简历的份数
     *
     * @param context 上下文
     * @return 当前用户简历的份数
     */
    public static int getResumeCount(Context context) {
        SharedPreferences spf = context.getSharedPreferences("ResumeCount", 0);
        return spf.getInt("count", 0);
    }

    /**
     * 设置当前用户简历的份数
     *
     * @param context 上下文
     * @param count   当前用户简历的份数
     */
    public static void setResumeCount(Context context, int count) {
        SharedPreferences spf = context.getSharedPreferences("ResumeCount", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt("count", count);
        editor.apply();
    }
}
