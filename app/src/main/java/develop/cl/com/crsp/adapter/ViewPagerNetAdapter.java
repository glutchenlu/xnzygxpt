package develop.cl.com.crsp.adapter;


import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

/**
 * ViewPagerNetAdapter 适配器
 */
public class ViewPagerNetAdapter extends PagerAdapter {

    List<NetworkImageView> list;
    Context context;

    /**
     * ViewPagerNetAdapter 适配器构造函数
     *
     * @param list    NetworkImageView Volley框架自定义控件
     * @param context
     */
    public ViewPagerNetAdapter(List<NetworkImageView> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView((View) object);
    }

}

