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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import develop.cl.com.crsp.util.MultipartRequest;

public class UploadUtil {

    public static void UploadReq(String url, List<String> dataList, final Context context, RequestQueue mQueue, final VolleyCallback callback) {
        List<Part> partList = new ArrayList<Part>();
        String picStr[];
        String pic = "";
        int datasiez = dataList.size();
        if (datasiez > 1) {
            if (datasiez > 1) {
                for (int i = 0; i <= datasiez - 2; i++) {
                    if (i == 0) {
                        pic = dataList.get(i);
                    } else {
                        pic = pic + "," + dataList.get(i);
                    }
                }
            }
            picStr = pic.split(",");
            int i = 0;
            for (String aPicStr : picStr) {
                i++;
                FilePart filePart = null;
                try {
                    filePart = new FilePart("photo", new File(aPicStr));
                    filePart.setCharSet("UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (filePart == null) {
                    Toast.makeText(context, "第" + i + "个文件格式不正确！", Toast.LENGTH_SHORT).show();
                }
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
    }

    public static void UploadData(String url, List<String> dataList, final Context context, RequestQueue mQueue, final VolleyCallback callback) {
        List<Part> partList = new ArrayList<Part>();
        String picStr[];
        String pic = "";
        int datasiez = dataList.size();
        if (datasiez > 0) {
            for (int i = 0; i < datasiez; i++) {
                if (i == 0) {
                    pic = dataList.get(i);
                } else {
                    pic = pic + "," + dataList.get(i);
                }
            }
            picStr = pic.split(",");
            int i = 0;
            for (String aPicStr : picStr) {
                i++;
                FilePart filePart = null;
                try {
                    filePart = new FilePart("photo", new File(aPicStr));
                    filePart.setCharSet("UTF-8");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (filePart == null) {
                    Toast.makeText(context, "第" + i + "个文件格式不正确！", Toast.LENGTH_SHORT).show();
                }
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

    /**
     * 下载文件
     *
     * @param getUrl 请求文件的URL
     * @param dir    保存文件的地址
     * @param fname  保存文件的名称
     */
    public static void downloadFile(final String getUrl, final String dir, final String fname) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(getUrl);
                    URLConnection uc = url.openConnection();
                    // System.out.println("文件大小："+(uc.getContentLength()/1024)+"KB");
                    String path = dir + "/" + fname;
                    File file = new File(path);
                    if (!file.getParentFile().exists()) {
                        System.out.println("目录路径不存在,创建中...");
                        file.getParentFile().mkdirs();
                        System.out.println("保存目录已创建。");
                    }
                    FileOutputStream os = new FileOutputStream(path);
                    InputStream is = uc.getInputStream();
                    byte[] b = new byte[1024];
                    int len = 0;
                    while ((len = is.read(b)) != -1) {
                        os.write(b, 0, len);
                    }
                    os.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        });
        thread.start();
    }
}
