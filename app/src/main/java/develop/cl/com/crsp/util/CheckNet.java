package develop.cl.com.crsp.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import develop.cl.com.crsp.R;

public class CheckNet {

    private ConnectivityManager manager;
    private Context locContex;

    public CheckNet(Context locContex) {
        this.locContex = locContex;
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    private boolean checkNetworkState() {
        boolean flag = false;
//得到网络连接信息
        manager = (ConnectivityManager) locContex.getSystemService(Context.CONNECTIVITY_SERVICE);
//去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetwork();
        } else {
            isNetworkAvailable();
        }

        return flag;
    }


    /**
     * 网络未连接时，调用设置方法
     */
    private void setNetwork() {
        Toast.makeText(locContex, "wifi is closed!", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(locContex);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("网络提示信息");
        builder.setMessage("网络不可用，如果继续，请先设置网络！");
        builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = null;
                /**
                 * 判断手机系统的版本！如果API大于10 就是3.0+
                 * 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
                 */
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName(
                            "com.android.settings",
                            "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                locContex.startActivity(intent);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {

        NetworkInfo.State gprs = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        NetworkInfo.State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        if (gprs == NetworkInfo.State.CONNECTED || gprs == NetworkInfo.State.CONNECTING) {
            Toast.makeText(locContex, "wifi is open! gprs", Toast.LENGTH_SHORT).show();
        }
        //判断为wifi状态下才加载广告，如果是GPRS手机网络则不加载！
        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            Toast.makeText(locContex, "wifi is open! wifi", Toast.LENGTH_SHORT).show();
//            loadAdmob();
        }

    }

    /**
     * 在wifi状态下 加载admob广告
     */
//    private void loadAdmob() {
//        ll = (LinearLayout) findViewById(R.id.load_ads);
//        ll.removeAllViews();
//        adsView = new AdView(this, AdSize.BANNER, "a15194a1ac9505d");
//        ll.addView(adsView);
//
//        adsView.loadAd(new AdRequest());
//    }
}
