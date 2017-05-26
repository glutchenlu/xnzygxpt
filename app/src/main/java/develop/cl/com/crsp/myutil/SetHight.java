package develop.cl.com.crsp.myutil;


import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

/**
 * 设置GridView的高度，进行动态调整GridView的高度
 */
public class SetHight {
    public static void setListViewHeightBasedOnChildren(GridView listView, int code) {
        // 获取listview的adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        // 固定列宽，有多少列
        int col = 4;// listView.getNumColumns();
        int totalHeight = 0;
        // i每次加4，相当于listAdapter.getCount()小于等于4时 循环一次，计算一次item的高度，
        // listAdapter.getCount()小于等于8时计算两次高度相加
        for (int i = 0; i < listAdapter.getCount(); i += col) {
            // 获取listview的每一个item
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            // 获取item的高度和
            totalHeight += listItem.getMeasuredHeight();
            Log.d("totalHeight", totalHeight + "");
        }

        // 获取listview的布局参数
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        // 设置高度
        if (code == 1) {
            params.height = totalHeight + 90;
        } else if (code == 0) {
            params.height = totalHeight + 30;
        }
        Log.d("params.height", params.height + "");
        // 设置margin
        // 设置参数
        listView.setLayoutParams(params);
    }
}
