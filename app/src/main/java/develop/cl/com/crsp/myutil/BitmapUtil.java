package develop.cl.com.crsp.myutil;


import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 自定义保存图片类
 */
public class BitmapUtil {
    /**
     * 将取得的图片保存至手机本地，使得用户在没有网络情况下也能看到头像
     *
     * @param context 　上下文
     * @param bmp     需要保存的图片
     * @return 保存成功后的图片的名称和绝对路径
     */
    public static String saveImage(Context context, Bitmap bmp) {
        File appDir = new File(context.getCacheDir().getPath(), "xnzygxpt");
        String resultStr = "";
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + "_photo.jpg";
        resultStr = appDir.getPath() + "/" + fileName;
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr;
    }
}
