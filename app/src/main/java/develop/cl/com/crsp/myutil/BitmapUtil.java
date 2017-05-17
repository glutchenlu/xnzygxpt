package develop.cl.com.crsp.myutil;


import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

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
