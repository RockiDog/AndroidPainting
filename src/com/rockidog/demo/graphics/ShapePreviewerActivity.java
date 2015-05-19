package com.rockidog.demo.graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.io.IOException;

public class ShapePreviewerActivity extends Activity {

  private static final String TAG = "ShapePreviewerActivity";
  private GLSurfaceView mGLView;
  private GLES20Render mRender;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mGLView = new GLSurfaceView(getApplication());
    Log.e(TAG, "Hardware Acceleration: " + mGLView.isHardwareAccelerated());
    mRender = new GLES20Render(newWorld());
    mGLView.setRenderer(mRender);
    setContentView(mGLView);
  }

  @Override
  protected void onResume() {
    super.onResume();
    mGLView.onResume();
  }

  private GWorldModel newWorld() {
    GWorldModel world = new GWorldModel();
    //GCube shape = new GCube(world, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f);
    //shape.setColor(GColor.GREEN);
    //world.addShape(shape);
    world.addShape(loadObject(world, "chess.obj"));
    world.setup();
    return world;
  }

  private GObject loadObject(GWorldModel world, String filename) {
    try {
      InputStream is = getResources().getAssets().open(filename);
      return new GObject(world, is);
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }
    return null;
  }
}
