package develop.cl.com.crsp.util;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DFVolley {
    final VolleyCallback callback;

    public DFVolley(VolleyCallback callback) {
        this.callback = callback;
    }

    public void NoMReq(RequestQueue mQueue, String getUrl, final VolleyCallback callback) {
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
        mQueue.add(stringRequest);
    }

    /**
     * @param mothod 请求方法
     * @param mQueue
     * @param getUrl 所要请求的url
     * @param mList  url参数
     */
    public void VolleyUtilWithGet(int mothod, RequestQueue mQueue, String getUrl, List<String[]> mList
            , final VolleyCallback callback) {
        if (mothod == Request.Method.GET) {
            if (mList.size() != 0) {
                getUrl = getUrl + "?";
                for (int i = 0; i < mList.size(); i++) {
                    if (i == 0) {
                        getUrl = getUrl + mList.get(i)[0] + "=" + mList.get(i)[1];
                    } else {
                        getUrl = getUrl + "&" + mList.get(i)[0] + "=" + mList.get(i)[1];
                    }
                }
            }
            Log.d("getUrl", getUrl);
            StringRequest stringRequest = new StringRequest(mothod, getUrl,
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
            mQueue.add(stringRequest);
        } else if (mothod == Request.Method.POST) {
            Map<String, String> hmap = new HashMap<String, String>();
            if (mList.size() != 0) {
                for (int i = 0; i < mList.size(); i++) {
                    hmap.put(mList.get(i)[0], mList.get(i)[1]);
                }
            }
            /**
             * 自定义post请求
             */
            MyStringRequest stringRequest = new MyStringRequest(mothod, getUrl,
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
//                    callback.onSuccessResponse(error.toString());
                    Log.e("TAG", error.getMessage(), error);
                }
            }, hmap);
            mQueue.add(stringRequest);
        }
    }

    public void VolleyUtilWithGet(int mothod, RequestQueue mQueue, String getUrl) {
        List<String[]> mList = new ArrayList<String[]>();
        this.VolleyUtilWithGet(mothod, mQueue, getUrl, mList, this.callback);
    }

    public void VolleyUtilWithGet(RequestQueue mQueue, String getUrl) {
        List<String[]> mList = new ArrayList<String[]>();
        this.VolleyUtilWithGet(0, mQueue, getUrl, mList, this.callback);
    }

    public void VolleyUtilWithGet(RequestQueue mQueue, String getUrl, List<String[]> mList) {
        this.VolleyUtilWithGet(0, mQueue, getUrl, mList, this.callback);
    }
}
