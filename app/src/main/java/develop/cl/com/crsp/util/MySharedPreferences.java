package develop.cl.com.crsp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import develop.cl.com.crsp.JavaBean.Basic;
import develop.cl.com.crsp.JavaBean.Dating;

public class MySharedPreferences {
    public static String getLogin(Context context) {
        SharedPreferences spf = context.getSharedPreferences("login", 0);
        String isLogin = spf.getString("isLogin", "no");
        return isLogin;
    }

    public static String getMyConfig(Context context) {
        SharedPreferences spf = context.getSharedPreferences("Myconfig", 0);
        String config = spf.getString("loginstate", "noLogin");
        return config;
    }

    public static void setLogin(Context context, String str) {
        SharedPreferences spf = context.getSharedPreferences("login", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("isLogin", str);
        editor.apply();
    }

    public static void setMyConfig(Context context, String str) {
        SharedPreferences spf = context.getSharedPreferences("Myconfig", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("loginstate", str);
        editor.apply();
    }

    public static void setSchool(Context context, String str) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("school", str);
        editor.apply();
    }

    public static String getSchool(Context context) {
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        String config = spf.getString("school", "0");
        return config;
    }


    public static String getUserID(Context context) {
        SharedPreferences spf = context.getSharedPreferences("UserID", 0);
        String isLogin = spf.getString("UserID", "0");
        return isLogin;
    }

    public static void setUserID(Context context, String UserID) {
        SharedPreferences spf = context.getSharedPreferences("UserID", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("UserID", UserID);
        editor.apply();
    }

    public static void setPhoto(Context context, String photoUrl) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        SharedPreferences.Editor editor = spf.edit();
        editor.putString("picture", photoUrl);
        editor.apply();
    }

    public static void setPersonData(Context context, Basic basic, Dating dating) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
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

    public static void setBasic(final Context context, final Basic basic) {
        RequestQueue mQueue = Volley.newRequestQueue(context);
        SharedPreferences spf = context.getSharedPreferences("PersonData", 0);
        final SharedPreferences.Editor editor = spf.edit();
        ImageRequest imageRequest = new ImageRequest(basic.getPicture(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        basic.setPicture(BitmapUtil.saveImage(context, response));
                        Log.i("Picture", basic.getPicture());
                        editor.putString("basicid", basic.getBasicid() + "");
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
}
