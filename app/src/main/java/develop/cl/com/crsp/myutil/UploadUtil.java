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

/**
 * 文件的上传下载工具类
 */
public class UploadUtil {
    /**
     * 上传GridView图片，适用于多图片上传
     *
     * @param url      请求的服务器地址
     * @param dataList 图片本地地址的list
     * @param context  上下文
     * @param mQueue   请求队列
     * @param callback 回调监听
     */
    public static void UploadReq(String url, List<String> dataList, final Context context, RequestQueue mQueue, final VolleyCallback callback) {
        List<Part> partList = new ArrayList<Part>();
        String picStr[];
        String pic = "";
        int datasiez = dataList.size();
        //GridView中有一个'+'图片，除了'+'图片之外的图片都上传至服务器
        if (datasiez > 1) {
            if (datasiez > 1) {
                //将图片本地地址转换为字符串
                for (int i = 0; i <= datasiez - 2; i++) {
                    if (i == 0) {
                        pic = dataList.get(i);
                    } else {
                        pic = pic + "," + dataList.get(i);
                    }
                }
            }
            //将字符串以","区分，转换为字符串数组
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
                //加入partlist中
                partList.add(filePart);
            }
            //将Volley框架自定义多文件上传请求
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

    /**
     * 上传文件，适用多文件上传
     *
     * @param url      请求的服务器地址
     * @param dataList 文件本地地址的list
     * @param context  上下文
     * @param mQueue   网络请求队列
     * @param callback 回调监听
     */
    public static void UploadData(String url, List<String> dataList, final Context context, RequestQueue mQueue, final VolleyCallback callback) {
        List<Part> partList = new ArrayList<Part>();
        String picStr[];
        String pic = "";
        int datasiez = dataList.size();
        if (datasiez > 0) {
            //将文件本地地址转换为字符串
            for (int i = 0; i < datasiez; i++) {
                if (i == 0) {
                    pic = dataList.get(i);
                } else {
                    pic = pic + "," + dataList.get(i);
                }
            }
            //将字符串以","区分，转换为字符串数组
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
                //加入partlist
                partList.add(filePart);
            }
            //将Volley框架自定义多文件上传请求
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

    /**
     * 上传图片，适用于单张图片
     *
     * @param url      请求的服务器地址
     * @param picUrl   图片本地地址url
     * @param context  上下文
     * @param mQueue   网络请求队列
     * @param callback 回调监听
     * @throws FileNotFoundException
     */
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
