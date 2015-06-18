package com.rockidog.demo.graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.ScaleGestureDetector;

import java.io.InputStream;
import java.io.IOException;

public class ShapePreviewerActivity extends Activity {

  private static final String TAG = "ShapePreviewerActivity";
  private static final String EXTRA_OBJECT_ID = "com.rockidog.demo.EXTRA_OBJECT_ID";
  private GLSurfaceView mGLView;
  private GLES20Render mRender;
  private String mObjectFilename;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int fileId = getIntent().getIntExtra(EXTRA_OBJECT_ID, 0);
    if (fileId > 0)
      mObjectFilename = fileId + ".obj";
    else
      mObjectFilename = "";
    
    mGLView = new GLSurfaceView(getApplication());
    mRender = new GLES20Render(newWorld());
    mGLView.setRenderer(mRender);
    setContentView(mGLView);
    
    new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
          
          @Override
          public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            mRender.scale(scale);
            return false;
          }
          
          @Override
          public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
          }
          
          @Override
          public void onScaleEnd(ScaleGestureDetector detector) {}
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    mGLView.onResume();
  }

  private GWorldModel newWorld() {
    if (mObjectFilename.length() > 0) {
      GWorldModel world = new GWorldModel();
      //GCube shape = new GCube(world, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f);
      //shape.setColor(GColor.GREEN);
      //world.addShape(shape);
      world.addShape(loadObject(world, mObjectFilename));
      world.setup();
      return world;
    }
    return null;
  }

  private GObject loadObject(GWorldModel world, String filename) {
    try {
      InputStream is = openFileInput(filename);
      return new GObject(world, is);
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }
    return null;
  }
}
