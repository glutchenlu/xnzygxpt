/**
 * @Title: AlbumActivity.java
 * @Package com.example.adapter
 * @Description: TODO()
 * @author Derek
 * @email renchun525@gmail.com
 * @date 2013-11-12 下午3:39:27
 * @version V1.0
 */
package develop.cl.com.crsp.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import develop.cl.com.crsp.R;
import develop.cl.com.crsp.adapter.AlbumGridViewAdapter;
import develop.cl.com.crsp.util.ImageManager2;


public class AlbumActivity extends Activity {

    public static final int SELECT_OK = 0;
    private GridView gridView;
    private ArrayList<String> dataList = new ArrayList<String>();
    private HashMap<String, ImageView> hashMap = new HashMap<String, ImageView>();
    private ArrayList<String> selectedDataList = new ArrayList<String>();
    private String bucketId = "";
    private final String IMG_JPG = "image/jpg";
    private final String IMG_JPEG = "image/jpeg";
    private final String IMG_PNG = "image/png";
    private final String IMG_GIF = "image/gif";
    private ProgressBar progressBar;
    private AlbumGridViewAdapter gridImageAdapter;
    private LinearLayout selectedImageLayout;
    private Button okButton;
    private HorizontalScrollView scrollview;
    private Context mContext;
    public static Handler mHandler;

    /**
     * @param mHandler
     *            the mHandler to set
     */
    public static void setmHandler(Handler mHandler) {
        AlbumActivity.mHandler = mHandler;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mContext = this;

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        selectedDataList = (ArrayList<String>) bundle
                .getSerializable("dataList");
        bucketId = bundle.getString("bucketId");
        init();
        initListener();

    }

    private void init() {

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        gridView = (GridView) findViewById(R.id.myGrid);
        gridImageAdapter = new AlbumGridViewAdapter(this, dataList,
                selectedDataList);
        gridView.setAdapter(gridImageAdapter);
        refreshData();
        selectedImageLayout = (LinearLayout) findViewById(R.id.selected_image_layout);
        okButton = (Button) findViewById(R.id.ok_button);
        scrollview = (HorizontalScrollView) findViewById(R.id.scrollview);

        initSelectImage();

    }

    private void initSelectImage() {
        if (selectedDataList == null)
            return;
        for (final String path : selectedDataList) {
            ImageView imageView = (ImageView) LayoutInflater.from(
                    AlbumActivity.this).inflate(R.layout.choose_imageview,
                    selectedImageLayout, false);
            selectedImageLayout.addView(imageView);
            hashMap.put(path, imageView);
            ImageManager2.from(AlbumActivity.this).displayImage(imageView,
                    path, R.drawable.camera_default, 100, 100);
            imageView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    removePath(path);
                    gridImageAdapter.notifyDataSetChanged();
                }
            });
        }
        okButton.setText("完成(" + selectedDataList.size() + "/8)");
    }

    private void initListener() {

        gridImageAdapter
                .setOnItemClickListener(new AlbumGridViewAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(final ToggleButton toggleButton,
                                            int position, final String path, boolean isChecked) {

                        if (selectedDataList.size() >= 8) {
                            toggleButton.setChecked(false);
                            if (!removePath(path)) {
                                Toast.makeText(AlbumActivity.this, "只能选择8张图片",
                                        Toast.LENGTH_SHORT).show();
                            }
                            return;
                        }

                        if (isChecked) {
                            if (!hashMap.containsKey(path)) {
                                ImageView imageView = (ImageView) LayoutInflater
                                        .from(AlbumActivity.this).inflate(
                                                R.layout.choose_imageview,
                                                selectedImageLayout, false);
                                selectedImageLayout.addView(imageView);
                                imageView.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        int off = selectedImageLayout
                                                .getMeasuredWidth()
                                                - scrollview.getWidth();
                                        if (off > 0) {
                                            scrollview.smoothScrollTo(off, 0);
                                        }

                                    }
                                }, 100);

                                hashMap.put(path, imageView);
                                selectedDataList.add(path);
                                ImageManager2.from(AlbumActivity.this)
                                        .displayImage(imageView, path,
                                                R.drawable.camera_default, 100,
                                                100);
                                imageView
                                        .setOnClickListener(new View.OnClickListener() {

                                            @Override
                                            public void onClick(View v) {
                                                toggleButton.setChecked(false);
                                                removePath(path);

                                            }
                                        });
                                okButton.setText("完成("
                                        + selectedDataList.size() + "/8)");
                            }
                        } else {
                            removePath(path);
                        }

                    }
                });

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (mHandler != null) {
                    Message message = Message.obtain();
                    message.what = AlbumActivity.SELECT_OK;
                    message.obj = selectedDataList;
                    mHandler.sendMessage(message);

                    Intent intent = new Intent(mContext, FabuGoodsdetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private boolean removePath(String path) {
        if (hashMap.containsKey(path)) {
            selectedImageLayout.removeView(hashMap.get(path));
            hashMap.remove(path);
            removeOneData(selectedDataList, path);
            okButton.setText("完成(" + selectedDataList.size() + "/8)");
            return true;
        } else {
            return false;
        }
    }

    private void removeOneData(ArrayList<String> arrayList, String s) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).equals(s)) {
                arrayList.remove(i);
                return;
            }
        }
    }

    private void refreshData() {

        new AsyncTask<Void, Void, ArrayList<String>>() {

            @Override
            protected void onPreExecute() {
                progressBar.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected ArrayList<String> doInBackground(Void... params) {
                ArrayList<String> tmpList = new ArrayList<String>();
                /*
				 * ArrayList<String> listDirlocal = listAlldir( new
				 * File(cameraDir)); ArrayList<String> listDiranjuke = new
				 * ArrayList<String>(); listDiranjuke.addAll(listDirlocal);
				 * 
				 * for (int i = 0; i < listDiranjuke.size(); i++){ listAllfile(
				 * new File( listDiranjuke.get(i) ),tmpList); }
				 */

                ContentResolver mResolver = mContext.getContentResolver();
                String[] IMAGE_PROJECTION = new String[]{ImageColumns.DATA};

                String selection = ImageColumns.BUCKET_ID + "= ?  AND "
                        + ImageColumns.MIME_TYPE + " IN (?,?,?)";

                String[] selectionArgs = new String[]{bucketId, IMG_JPG,
                        IMG_JPEG, IMG_PNG};

                Cursor cursor = mResolver.query(
                        Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selection, selectionArgs, null);
                if (null != cursor) {
                    if (cursor.getCount() > 0) {
                        while (cursor.moveToNext()) {
                            tmpList.add(cursor.getString(0));
                        }
                    }
                    cursor.close();
                }
                return tmpList;
            }

            protected void onPostExecute(ArrayList<String> tmpList) {

                if (AlbumActivity.this == null
                        || AlbumActivity.this.isFinishing()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                dataList.clear();
                dataList.addAll(tmpList);
                gridImageAdapter.notifyDataSetChanged();
                return;

            }

            ;

        }.execute();

    }

    private ArrayList<String> listAlldir(File nowDir) {
        ArrayList<String> listDir = new ArrayList<String>();
        nowDir = new File(Environment.getExternalStorageDirectory()
                + nowDir.getPath());
        if (!nowDir.isDirectory()) {
            return listDir;
        }

        File[] files = nowDir.listFiles();

        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().substring(0, 1).equals(".")) {
                continue;
            }
            File file = new File(files[i].getPath());
            if (file.isDirectory()) {
                listDir.add(files[i].getPath());
            }
        }
        return listDir;
    }

    private ArrayList<String> listAllfile(File baseFile,
                                          ArrayList<String> tmpList) {
        if (baseFile != null && baseFile.exists()) {
            File[] file = baseFile.listFiles();
            for (File f : file) {
                if (f.getPath().endsWith(".jpg")
                        || f.getPath().endsWith(".png")) {
                    tmpList.add(f.getPath());
                }
            }
        }
        return tmpList;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        // intent.putArrayListExtra("dataList", dataList);
        bundle.putStringArrayList("dataList", selectedDataList);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
        // super.onBackPressed();
    }

	/*
	 * @Override public void finish() { // TODO Auto-generated method stub
	 * super.finish(); //
	 * ImageManager2.from(AlbumActivity.this).recycle(dataList); }
	 */

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
