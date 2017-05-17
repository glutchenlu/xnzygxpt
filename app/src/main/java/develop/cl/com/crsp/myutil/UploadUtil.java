package develop.cl.com.crsp.myutil;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.http.multipart.FilePart;
import com.android.internal.http.multipart.Part;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import develop.cl.com.crsp.util.MultipartRequest;
import develop.cl.com.crsp.util.VolleyCallback;

public class UploadUtil {

    public static void UploadReq(String url, ArrayList<String> dataList, final Context context, RequestQueue mQueue, final VolleyCallback callback) throws FileNotFoundException {
        List<Part> partList = new ArrayList<Part>();
        String picStr[];
        String pic = "";
        if (dataList.size() > 1) {
            for (int i = 0; i <= dataList.size() - 2; i++) {
                if (i == 0) {
                    pic = dataList.get(i);
                } else {
                    pic = pic + "," + dataList.get(i);
                }
            }
        }
        picStr = pic.split(",");
        for (String aPicStr : picStr) {
            FilePart filePart = new FilePart("photo", new File(aPicStr));
            filePart.setCharSet("UTF-8");
            partList.add(filePart);
        }
        MultipartRequest profileUpdateRequest = new MultipartRequest(url, partList.toArray(new Part[partList.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //处理成功返回信息
                callback.onSuccessResponse(response);
                Log.d("Multipartresponse", response);
//                String info = response.substring(0, 20);
//                Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //处理失败错误信息
                Log.e("MultipartRequest", error.getMessage(), error);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //将请求加入队列
        mQueue.add(profileUpdateRequest);
    }

    public static void UploadReqPhoto(String url, String picUrl, final Context context
            , RequestQueue mQueue, final VolleyCallback callback) throws FileNotFoundException {
        List<Part> partList = new ArrayList<Part>();
        FilePart filePart = new FilePart("photo", new File(picUrl));
        filePart.setCharSet("UTF-8");
        partList.add(filePart);
        MultipartRequest profileUpdateRequest = new MultipartRequest(url, partList.toArray(new Part[partList.size()]), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //处理成功返回信息
                callback.onSuccessResponse(response);
                Log.d("Multipartresponse", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //处理失败错误信息
                Log.e("MultipartRequest", error.getMessage(), error);
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //将请求加入队列
        mQueue.add(profileUpdateRequest);
    }
}
