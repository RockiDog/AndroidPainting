package com.rockidog.demo.graphics;

import android.opengl.GLSurfaceView;
import android.util.Log;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.rockidog.demo.graphics.GVertex.Axis;

public class GLES20Render implements GLSurfaceView.Renderer {

  private static final String TAG = "GLES20Render";

  private FloatBuffer mAmbientColor  = FloatBuffer.wrap(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
  private FloatBuffer mDiffuseColor  = FloatBuffer.wrap(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
  private FloatBuffer mLightPosition = FloatBuffer.wrap(new float[]{0.0f, 5.0f, 5.0f, 1.0f});
  private float[] mBGColor = {0.0f, 0.0f, 0.0f, 1.0f};
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
    /* Set up viewport */
    float ratio = width * 1.0f / height;
    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(GL10.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glFrustumf(-ratio, ratio, -1, 1, 2, 50);
    gl.glDisable(GL10.GL_DITHER);
    
    /* Set up lights */
    gl.glEnable(GL10.GL_COLOR_MATERIAL);
    gl.glEnable(GL10.GL_LIGHTING);
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, mAmbientColor);
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, mDiffuseColor);
    gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, mLightPosition);
    gl.glEnable(GL10.GL_LIGHT0);
  }

  @Override
  public void onDrawFrame(GL10 gl) {
    gl.glClearColor(mBGColor[0], mBGColor[1], mBGColor[2], mBGColor[3]);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    
    gl.glMatrixMode(GL10.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0, 0, -3.0f);
    //gl.glRotatef(mVAngle, 0, 1, 0);
    //gl.glRotatef(mHAngle, 1, 0, 0);
    gl.glScalef(0.3f, 0.3f, 0.3f);
    
    gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
    gl.glEnable(GL10.GL_CULL_FACE);
    gl.glShadeModel(GL10.GL_SMOOTH);
    gl.glEnable(GL10.GL_DEPTH_TEST);
    
    mWorld.getShapeList().get(0).init();
    mWorld.getShapeList().get(0).rotatef(mHAngle, Axis.X);
    mWorld.getShapeList().get(0).rotatef(mVAngle, Axis.Y);
    mWorld.getShapeList().get(0).translatef(0, 0, -10);
    mWorld.draw(gl);
    
    if (mVAngle < 360)
      mVAngle += 1;
    else
      mVAngle = 0;
    if (mHAngle < 360)
      mHAngle += 0.5;
    else
      mHAngle = 0;
  }
  private float mVAngle = 0;
  private float mHAngle = 0;
}
