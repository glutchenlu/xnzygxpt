package develop.cl.com.crsp.myutil;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义封装Volley框架，用来处理一定的逻辑后在发送网络请求服务器
 */
public class DFVolley {

    /**
     * 以get请求方式请求服务器，适用于参数较少的情况
     *
     * @param mQueue   请求队列
     * @param getUrl   请求路径
     * @param callback 请求所得到的返回内容回调接口
     */
    public static void NoMReq(RequestQueue mQueue, String getUrl, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onSuccessResponse(response);
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onSuccessResponse("error");
                Log.e("TAG", error.getMessage(), error);
            }
        });
        //添加请求至队列中
        mQueue.add(stringRequest);
    }

    /**
     * 向服务器发送请求，获取数据或提交数据，适用于参数较多时，支持get和post
     *
     * @param mothod   请求方式；0为get，1为post
     * @param mQueue   请求队列对象
     * @param getUrl   请求URL地址
     * @param mList    请求参数；包含两个数组，两数组一个存储请求参数名称，另一个以相同顺序存储参数名称的值
     * @param callback 请求所得到的返回内容回调接口
     */
    public static void VolleyUtilWithGet(int mothod, RequestQueue mQueue, String getUrl, List<String[]> mList
            , final VolleyCallback callback) {
        //get方式请求
        if (mothod == Request.Method.GET) {
            if (mList.size() != 0) {
                getUrl = getUrl + "?";
                //遍历数组，拼凑URL中get参数
                for (int i = 0; i < mList.size(); i++) {
                    if (i == 0) {
                        getUrl = getUrl + mList.get(i)[0] + "=" + mList.get(i)[1];
                    } else {
                        getUrl = getUrl + "&" + mList.get(i)[0] + "=" + mList.get(i)[1];
                    }
                }
            }
            Log.d("getUrl", getUrl);
            //创建请求
            StringRequest stringRequest = new StringRequest(mothod, getUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //回调，请求成功时
                            callback.onSuccessResponse(response);
                            Log.d("TAG", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //回调，请求错误时
                    callback.onSuccessResponse("error");
                    Log.e("TAG", error.getMessage(), error);
                }
            });
            //添加请求至队列中
            mQueue.add(stringRequest);
        } else if (mothod == Request.Method.POST) {
            //post方式请求，将list的两键值参数数组转换为map，用来进行post请求
            Map<String, String> hmap = new HashMap<String, String>();
            if (mList.size() != 0) {
                for (int i = 0; i < mList.size(); i++) {
                    hmap.put(mList.get(i)[0], mList.get(i)[1]);
                }
            }
            //自定义post请求
            MyStringRequest stringRequest = new MyStringRequest(mothod, getUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //回调，请求成功时
                            callback.onSuccessResponse(response);
                            Log.d("TAG", response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //回调，请求错误时
                    callback.onSuccessResponse("error");
                    Log.e("TAG", error.getMessage(), error);
                }
            }, hmap);
            //添加请求至队列中
            mQueue.add(stringRequest);
        }
    }

    /**
     * 以post请求方式请求服务器，适用于参数较少的情况
     *
     * @param mQueue   请求队列
     * @param getUrl   请求路径
     * @param callback 请求所得到的返回内容回调接口
     * @param hmap     以请求键值方式封装起来的map
     */
    public static void NoMPots(RequestQueue mQueue, String getUrl, final VolleyCallback callback, Map<String, String> hmap) {
        MyStringRequest stringRequest = new MyStringRequest(1, getUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //回调，请求成功时
                        callback.onSuccessResponse(response);
                        Log.d("TAG", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //回调，请求错误时
                callback.onSuccessResponse("error");
                Log.e("TAG", error.getMessage(), error);
            }
        }, hmap);
        mQueue.add(stringRequest);
    }
}
