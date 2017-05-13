package develop.cl.com.crsp.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import java.util.ArrayList;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.adapter.ImageListAdapter;
import develop.cl.com.crsp.util.PhotoDirInfo;

public class ImageList extends Activity {
    private ArrayList<String> selectedDataList = new ArrayList<String>();
    private ListView listView;
    private ImageListAdapter adapter;
    private Context mContext;
    private ArrayList<PhotoDirInfo> mDirInfos;
    private final String IMG_JPG = "image/jpg";
    private final String IMG_JPEG = "image/jpeg";
    private final String IMG_PNG = "image/png";
    private final String IMG_GIF = "image/gif";

    /**
     * @param savedInstanceState
     * @see Activity#onCreate(Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iamgelist_layout);
        mContext = this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedDataList = (ArrayList<String>) bundle
                .getSerializable("dataList");

        listView = (ListView) findViewById(R.id.imageListView);
        mDirInfos = getImageDir(mContext);
        adapter = new ImageListAdapter(ImageList.this, mDirInfos);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                PhotoDirInfo photoDirInfo = mDirInfos.get(position);
                Intent intent = new Intent(mContext, AlbumActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("dataList", selectedDataList);
                bundle.putString("bucketId", photoDirInfo.getDirId());
                Log.e("bucketId", "" + photoDirInfo.getDirId());
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
            }
        });
    }

    private synchronized ArrayList<PhotoDirInfo> getImageDir(Context context) {
        ArrayList<PhotoDirInfo> list = null;
        PhotoDirInfo dirInfo = null;
        ContentResolver mResolver = context.getContentResolver();
        String[] IMAGE_PROJECTION = new String[]{ImageColumns.BUCKET_ID,
                ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns.DATA,
                "COUNT(*) AS " + ImageColumns.DATA};

        String selection = " 1=1 AND " + ImageColumns.MIME_TYPE
                + " IN (?,?,?)) GROUP BY (" + ImageColumns.BUCKET_ID
                + ") ORDER BY (" + ImageColumns.BUCKET_DISPLAY_NAME;

        String[] selectionArgs = new String[]{IMG_JPG, IMG_JPEG, IMG_PNG};
        // String
        // selection=ImageColumns.MIME_TYPE+" IN ("+IMG_JPG+","+IMG_PNG+")) GROUP BY ("+ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;

        Cursor cursor = mResolver.query(Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION, selection, selectionArgs, null);
        if (null != cursor) {
            if (cursor.getCount() > 0) {
                list = new ArrayList<PhotoDirInfo>();
                while (cursor.moveToNext()) {
                    dirInfo = new PhotoDirInfo();
                    dirInfo.setDirId(cursor.getString(0));
                    dirInfo.setDirName(cursor.getString(1));
                    dirInfo.setFirstPicPath(cursor.getString(2));
                    // Log.e("PicPath==>", cursor.getString(2));
                    dirInfo.setPicCount(cursor.getInt(3));
                    dirInfo.setUserOtherPicSoft(false);
                    list.add(dirInfo);
                }
            }
            cursor.close();
        }
        Log.e("list.size()===>", "" + list.size());
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ArrayList<String> tDataList = (ArrayList<String>) bundle
                        .getSerializable("dataList");
                if (tDataList != null) {
                    selectedDataList.clear();
                    selectedDataList.addAll(tDataList);
                }
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("dataList", selectedDataList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        // super.onBackPressed();
    }
}
