package develop.cl.com.crsp.myutil;


import android.content.Context;

/**
 * 检查用户当前状态
 */
public class CheckUtil {
    /**
     * 检测用户当前是否登录
     *
     * @param contex 上下文
     * @return 用户已登录返回true，否则false
     */
    public static boolean checkLogin(Context contex) {
        boolean isLogin = false;
        if (MySharedPreferences.getLogin(contex).equals("yes")) {
            isLogin = true;
        }
        return isLogin;
    }
}
