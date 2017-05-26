package develop.cl.com.crsp.myutil;


import android.content.Context;
import android.net.ConnectivityManager;

/**
 * 判断用户网络状态
 */
public class MyCheckNet {
    /**
     * 判断用户网络状态
     *
     * @param context 上下文
     * @return 网络状态已连通返回true，否则false
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果仅仅是用来判断网络连接
        //则可以使用 cm.getActiveNetworkInfo().isAvailable();
        return cm.getActiveNetworkInfo().isAvailable();
    }
}