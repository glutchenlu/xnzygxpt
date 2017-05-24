package develop.cl.com.crsp.myutil;


import android.content.Context;

public class CheckUtil {
    public static boolean checkLogin(Context contex) {
        boolean isLogin = false;
        if (MySharedPreferences.getLogin(contex).equals("yes")) {
            isLogin = true;
        }
        return isLogin;
    }
}
