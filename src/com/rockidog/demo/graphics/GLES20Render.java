package com.rockidog.demo.graphics;

import android.opengl.GLSurfaceView;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLES20Render implements GLSurfaceView.Renderer {

  private static final String TAG = "GLES20Render";

  private float[] mBGColor = {0.5f, 0.5f, 0.5f, 0.5f};
  private GWorldModel mWorld;

  public GLES20Render(GWorldModel world) {
    mWorld = world;
  }

  @Override
  public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    Log.i(TAG, "Surface created");
  }

  @Override
  public void onSurfaceChanged(GL10 gl, int width, int height) {
    float ratio = width * 1.0f / height;
    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(GL10.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glFrustumf(-ratio, ratio, -1, 1, 2, 12);
    gl.glDisable(GL10.GL_DITHER);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    gl.glClearColor(mBGColor[0], mBGColor[1], mBGColor[2], mBGColor[3]);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    
    gl.glMatrixMode(GL10.GL_MODELVIEW);
    gl.glLoadIdentity();
    
    gl.glTranslatef(0, 0, -3.0f);
    //gl.glScalef(0.5f, 0.5f, 0.5f);
    //gl.glColor4f(0.5f, 0.5f, 0.5f, 1.0f);
    
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    
    gl.glEnable(GL10.GL_CULL_FACE);
    gl.glShadeModel(GL10.GL_SMOOTH);
    gl.glEnable(GL10.GL_DEPTH_TEST);
    
    mWorld.draw(gl);
  }
}
