package develop.cl.com.crsp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.util.ImageManager2;
import develop.cl.com.crsp.util.PhotoDirInfo;


public class ImageListAdapter extends BaseAdapter {
	private LayoutInflater inflater = null;
	private Context mContext;
	private ArrayList<PhotoDirInfo> listDir;

	public ImageListAdapter(Context context, ArrayList<PhotoDirInfo> objects) {
		super();
		// TODO Auto-generated constructor stub
		this.mContext = context;
		listDir = objects;
		inflater = LayoutInflater.from(mContext);
	}

	/**
	 * @return
	 * @see android.widget.ArrayAdapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listDir != null ? listDir.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 * @see android.widget.ArrayAdapter#getView(int, View,
	 *      ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		PhotoDirInfo localPhotoDirInfo = listDir.get(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.imagelist_item, parent,
					false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.iamge_icon);
			viewHolder.textView = (TextView) convertView
					.findViewById(R.id.iamge_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.textView.setText(localPhotoDirInfo.getDirName() + "("
				+ localPhotoDirInfo.getPicCount() + ")");
		String path = localPhotoDirInfo.getFirstPicPath();
		ImageManager2.from(mContext).displayImage(viewHolder.imageView, path,
				R.drawable.camera_default, 100, 100);
		return convertView;
	}

	/**
	 * 存放列表项控件句柄
	 */
	private class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

}
