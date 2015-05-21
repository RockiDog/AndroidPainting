package com.rockidog.demo;

import com.rockidog.demo.graphics.ShapePreviewerActivity;
import com.rockidog.demo.network.TCPClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.BaseAdapter;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.InputStream;
import java.io.IOException;

public class ImagePreviewerActivity extends Activity {

  private class ImageAdapter extends BaseAdapter {
    private ImageView[] mImageArray;
    
    public ImageAdapter(Context context, ImageView[] imageArray) {
      mImageArray = imageArray;
    }
    
    @Override
    public Object getItem(int position) {
      return mImageArray[position];
    }
    
    @Override
    public long getItemId(int position) {
      return position;
    }
    
    @Override
    public int getCount() {
      return mImageArray.length;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      ImageView view = mImageArray[position];
      return view;
    }
  }

  private class FlushedInputStream extends FilterInputStream {
    
    public FlushedInputStream(InputStream in) {
      super(in);
    }
    
    @Override
    public int read(byte[] buffer, int offset, int count) throws IOException {
      int ret = super.read(buffer, offset, count);
      for (int i = 6; i < buffer.length - 4; i++) {
        if (buffer[i] == 0x2c) {
          if (buffer[i + 2] == 0 && buffer[i + 1] > 0 && buffer[i + 1] <= 48) buffer[i + 1] = 0;
          if (buffer[i + 4] == 0 && buffer[i + 3] > 0 && buffer[i + 3] <= 48) buffer[i + 3] = 0;
        }
      }
      return ret;
    }
  }

  private static final String TAG = "ImagePreviewActivity";
  private static final String EXTRA_IMAGES = "com.rockidog.demo.EXTRA_IMAGES";
  private static final String EXTRA_OBJECT_ID = "com.rockidog.demo.EXTRA_OBJECT_ID";
  private static final int MAX_TRY_TIMES = 100;
  private static final long SLEEP_TIME = 50;

  private TCPClient mTCPClient;
  private ListView mImageList;
  private ImageAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_image_preview);
    mTCPClient = PaintingView.getTCPClient();
  }

  @Override
  protected void onResume() {
    super.onResume();
    final String[] filenames = getIntent().getStringArrayExtra(EXTRA_IMAGES);
    final Context context = this;
    if (filenames.length != 0)
      inflateImages(filenames);
    else
      Log.w(TAG, "Empty filename array");
    mImageList.setBackgroundColor(Color.BLACK);
    mImageList.setOnItemClickListener(new OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.w(TAG, "Selected item: " + filenames[position]);
        Log.w(TAG, "ID: " + filenames[position].split("\\.")[0]);
        int fileId = Integer.parseInt(filenames[position].split("\\.")[0]);
        mTCPClient.transmitObjectFile(fileId);
        
        int triedTimes = 0;
        while (mTCPClient.isObjectFileTransmitOK() == false && triedTimes < MAX_TRY_TIMES) {
          ++triedTimes;
          try {
            Thread.sleep(SLEEP_TIME);
          } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
          }
        }
        if (triedTimes < MAX_TRY_TIMES) {
          Intent intent = new Intent(context, ShapePreviewerActivity.class);
          intent.putExtra(EXTRA_OBJECT_ID, fileId);
          startActivity(intent);
        }
      }
    });
  }

  private void inflateImages(String[] filenames) {
    Log.i(TAG, "file count " + filenames.length);
    ImageView[] imagesViews = new ImageView[filenames.length];
    for (int i = 0; i < filenames.length; ++i) {
      try {
        FlushedInputStream fis = new FlushedInputStream(openFileInput(filenames[i]));
        //Log.w(TAG, Boolean.toString(fis.markSupported()));
        //FlushedInputStream fis = new FlushedInputStream(new FileInputStream(new File("/sdcard/" + filenames[i])));
        Bitmap bitmap = BitmapFactory.decodeStream(fis);
        ImageView image = new ImageView(this);
        image.setImageBitmap(bitmap);
        imagesViews[i] = image;
      } catch (FileNotFoundException e) {
        Log.e(TAG, e.getMessage());
      }
      /*
      String filename = "/data/data/com.rockidog.demo/files/" + filenames[i];
      Bitmap bitmap = BitmapFactory.decodeFile(filename);
      ImageView image = new ImageView(this);
      image.setImageBitmap(bitmap);
      imagesViews[i] = image;
      */
    }
    mImageList = (ListView)findViewById(R.id.image_list);
    mAdapter = new ImageAdapter(this, imagesViews);
    mImageList.setAdapter(mAdapter);
  }
}
