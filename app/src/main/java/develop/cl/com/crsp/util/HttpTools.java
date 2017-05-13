package develop.cl.com.crsp.util;

import android.util.Log;

import com.sun.star.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 文件处理
 *
 * @author CSY
 * @date 2017-04-16
 */
public class HttpTools {

    private static Logger log = LoggerFactory.getLogger(HttpTools.class);

    /**
     * 保存文件
     *
     * @param getUrl 请求路径
     * @param dir    要保存到的文件夹地址
     * @return
     */
    public static String downloadFile2(String getUrl, String dir) {
        String path = null;
        try {
            URL url = new URL(getUrl);
            URLConnection uc = url.openConnection();
            String fileName = uc.getHeaderField(2) + ".jpg";

            // System.out.println("文件大小："+(uc.getContentLength()/1024)+"KB");
            path = dir + fileName;
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
        return path;
    }

    /**
     * 新连接
     *
     * @param getUrl
     * @return
     */
    public static String getNewPath(String getUrl) {
        String filePathUrl = null;
        try {
            // 统一资源
            URL url = new URL(getUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();

            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            int fileLength = httpURLConnection.getContentLength();

            filePathUrl = httpURLConnection.getURL().getFile();

        } catch (Exception e) {
            // TODO: handle exception
            log.error("错误信息:" + e.getMessage());
        }
        return filePathUrl;
    }

    /**
     * 读取行形式
     *
     * @param getUrl
     * @return
     */
    public static String readLine(String getUrl) {
        StringBuffer html = null;
        try {
            URL u = new URL(getUrl);
            URLConnection conn = u.openConnection();
            // Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101
            // Firefox/50.0
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(), "UTF-8"));
            String str = reader.readLine();
            html = new StringBuffer();
            while (str != null) {
                html.append(str);
                str = reader.readLine();
            }
            reader.close();
        } catch (Exception ex) {
            System.out.println("错误信息：" + ex.getMessage());
        }
        return html.toString();
    }

    /**
     * @param getUrl      下载路径
     * @param downloadDir 下载存放目录
     * @return 返回下载文件
     */
    @SuppressWarnings("finally")
    public static String downloadFile(String getUrl, String downloadDir)
            throws IOException {
        System.out.println(getUrl);

        File file = null;
        String path = null;
        try {
            // 统一资源
            URL url = new URL(getUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();

            String fileFullName = filePathUrl.substring(filePathUrl
                    .lastIndexOf(File.separatorChar) + 1);

            System.out.println(filePathUrl);
            System.out.println(fileFullName);

            System.out.println("file length---->" + fileLength);

            URLConnection con = url.openConnection();

            BufferedInputStream bin = new BufferedInputStream(
                    httpURLConnection.getInputStream());

            path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 打印下载百分比
                // System.out.println("下载了-------> " + len * 100 / fileLength +
                // "%\n");
            }
            bin.close();
            out.close();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            return path;
        }

    }

    /**
     * 多文件上传的方法
     *
     * @param actionUrl       ：上传的路径
     * @param uploadFilePaths ：需要上传的文件路径，数组
     * @return
     */
    @SuppressWarnings("finally")
    public static String uploadFile(String actionUrl, String[] uploadFilePaths)
            throws IOException {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        Log.i("HttpTool", actionUrl);

        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;

        try {
            // 统一资源
            URL url = new URL(actionUrl);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;

            // 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setDoInput(true);
            // 设置是否向httpUrlConnection输出
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码连接参数
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 设置请求内容类型
            httpURLConnection.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);

            // 设置DataOutputStream
            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                Log.i("uploadFile", uploadFile);
                String filename = uploadFile.substring(uploadFile
                        .lastIndexOf("//") + 1);
                Log.i("filename", filename);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; "
                        + "name=\"file" + i + "\";filename=\"" + filename
                        + "\"" + end);
                ds.writeBytes(end);
                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                /* close streams */
                fStream.close();
            }

            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            ds.flush();
            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is "
                                + httpURLConnection.getResponseCode() + httpURLConnection.getResponseMessage());
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (java.io.IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (java.io.IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (java.io.IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (java.io.IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }


}
