package com.rockidog.demo.graphics;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class ShapePreviewerActivity extends Activity {

  private GLSurfaceView mGLView;
  private GLES20Render mRender;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mGLView = new GLSurfaceView(getApplication());
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
    world.addShape(new GShape(world, -1.0f, -1.0f, -2.0f, 1.0f, 1.0f, -1.0f));
    world.setup();
    return world;
  }
}
