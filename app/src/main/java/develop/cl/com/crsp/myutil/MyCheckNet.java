package develop.cl.com.crsp.myutil;


import android.content.Context;
import android.net.ConnectivityManager;

public class MyCheckNet {
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        //如果仅仅是用来判断网络连接
        //则可以使用 cm.getActiveNetworkInfo().isAvailable();
        return cm.getActiveNetworkInfo().isAvailable();
    }
}