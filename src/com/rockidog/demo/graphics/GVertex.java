package com.rockidog.demo.graphics;

import android.util.Log;

import java.nio.IntBuffer;

public class GVertex {

  public enum Axis { X, Y, Z }

  private static final String TAG = "GVertex";
  private final short mIndex;

  public static float mMaxX, mMaxY, mMaxZ;
  public float mOriginX, mOriginY, mOriginZ;
  public float X, Y, Z;
  private GVector mNormal = null;
  private GColor mColor = null;
  private IntBuffer mPosBuffer = null;
  private IntBuffer mColorBuffer = null;
  private IntBuffer mNormalBuffer = null;

  public GVertex(float x, float y, float z, int index) {
    mOriginX = X = x;
    mOriginY = Y = y;
    mOriginZ = Z = z;
    mColor = new GColor(GColor.WHITE);
    mIndex = (short)index;
  }

  public GVertex(float x, float y, float z, GColor color, int index) {
    mOriginX = X = x;
    mOriginY = Y = y;
    mOriginZ = Z = z;
    mColor = new GColor(color);
    mIndex = (short)index;
  }

  public short getIndex() {
    return mIndex;
  }

  public void init() {
    X = mOriginX;
    Y = mOriginY;
    Z = mOriginZ;
    if (mPosBuffer != null) {
      mPosBuffer.put(mIndex * 3, toFixed(X));
      mPosBuffer.put(mIndex * 3 + 1, toFixed(Y));
      mPosBuffer.put(mIndex * 3 + 2, toFixed(Z));
    }
    if (mNormalBuffer != null && mNormal != null) {
      mNormal.init();
      mNormalBuffer.put(mIndex * 3, toFixed(mNormal.X));
      mNormalBuffer.put(mIndex * 3 + 1, toFixed(mNormal.Y));
      mNormalBuffer.put(mIndex * 3 + 2, toFixed(mNormal.Z));
    }
  }

  public void setNormal(GVector normal) {
    if (mNormalBuffer != null) {
      if (mNormal == null) {
        mNormalBuffer.put(mIndex * 3, 0);
        mNormalBuffer.put(mIndex * 3 + 1, 0);
        mNormalBuffer.put(mIndex * 3 + 2, 0);
      } else {
        mNormalBuffer.put(mIndex * 3, toFixed(normal.X));
        mNormalBuffer.put(mIndex * 3 + 1, toFixed(normal.Y));
        mNormalBuffer.put(mIndex * 3 + 2, toFixed(normal.Z));
      }
    }
    mNormal = new GVector(normal);
  }

  public GVector getNormal() {
    return mNormal;
  }

  public void setColor(GColor color) {
    if (mColorBuffer != null) {
      if (mColor == null) {
        mColorBuffer.put(mIndex * 4, 0);
        mColorBuffer.put(mIndex * 4 + 1, 0);
        mColorBuffer.put(mIndex * 4 + 2, 0);
        mColorBuffer.put(mIndex * 4 + 3, 0);
      } else {
        mColorBuffer.put(mIndex * 4, color.R);
        mColorBuffer.put(mIndex * 4 + 1, color.G);
        mColorBuffer.put(mIndex * 4 + 2, color.B);
        mColorBuffer.put(mIndex * 4 + 3, color.A);
      }
    }
    mColor = new GColor(color);
  }

  public GColor getColor() {
    return mColor;
  }

  public void put(IntBuffer posBuffer, IntBuffer colorBuffer, IntBuffer normalBuffer) {
    mPosBuffer = posBuffer;
    mColorBuffer = colorBuffer;
    mNormalBuffer = normalBuffer;
    
    if (Math.abs(mOriginX) > mMaxX) mMaxX = Math.abs(mOriginX);
    if (Math.abs(mOriginY) > mMaxY) mMaxY = Math.abs(mOriginY);
    if (Math.abs(mOriginZ) > mMaxZ) mMaxZ = Math.abs(mOriginZ);
    posBuffer.put(toFixed(mOriginX));
    posBuffer.put(toFixed(mOriginY));
    posBuffer.put(toFixed(mOriginZ));
    if (mColor == null) {
      colorBuffer.put(0);
      colorBuffer.put(0);
      colorBuffer.put(0);
      colorBuffer.put(0);
    } else {
      colorBuffer.put(mColor.R);
      colorBuffer.put(mColor.G);
      colorBuffer.put(mColor.B);
      colorBuffer.put(mColor.A);
    }
    if (mNormal == null) {
      normalBuffer.put(0);
      normalBuffer.put(0);
      normalBuffer.put(0);
    } else {
      normalBuffer.put(toFixed(mNormal.mOriginX));
      normalBuffer.put(toFixed(mNormal.mOriginY));
      normalBuffer.put(toFixed(mNormal.mOriginZ));
    }
  }

  static private int toFixed(float num) {
    return (int)(num * 65536.0f);
  }

  @Override
  public boolean equals(Object o) {
    GVertex v = (GVertex)o;
    if (mOriginX == v.mOriginX
        && mOriginY == v.mOriginY
        && mOriginZ == v.mOriginZ)
      return true;
    return false;
  }

  public void translatef(float x, float y, float z) {
    Matrix4 m4 = new Matrix4(new float[][]{
      {1, 0, 0, x},
      {0, 1, 0, y},
      {0, 0, 1, z},
      {0, 0, 0, 1}
    });
    float[] result = m4.multiply(new float[]{X, Y, Z, 1});
    X = result[0];
    Y = result[1];
    Z = result[2];
    
    if (mPosBuffer != null) {
      //Log.i(TAG, "update postition");
      mPosBuffer.put(mIndex * 3, toFixed(X));
      mPosBuffer.put(mIndex * 3 + 1, toFixed(Y));
      mPosBuffer.put(mIndex * 3 + 2, toFixed(Z));
    }
  }

  public void rotatef(float angle, Axis axis) {
    float rad = (float)Math.toRadians(angle);
    float cos = (float)Math.cos(rad);
    float sin = (float)Math.sin(rad);
    float[][] core;
    switch (axis) {
      case X:
        core = new float[][] {
          {1,   0,    0, 0},
          {0, cos, -sin, 0},
          {0, sin,  cos, 0},
          {0,   0,    0, 1},
        }; break;
      case Y:
        core = new float[][] {
          {cos, 0, -sin, 0},
          {  0, 1,    0, 0},
          {sin, 0,  cos, 0},
          {  0, 0,    0, 1},
        }; break;
      case Z:
        core = new float[][] {
          {cos, -sin, 0, 0},
          {sin,  cos, 0, 0},
          {  0,    0, 1, 0},
          {  0,    0, 0, 1},
        }; break;
      default:
        core = new float[][] {
          {1, 0, 0, 0},
          {0, 1, 0, 0},
          {0, 0, 1, 0},
          {0, 0, 0, 1},
        }; break;
    }
    Matrix4 m4 = new Matrix4(core);
    float[] result = m4.multiply(new float[]{X, Y, Z, 1});
    X = result[0] / result[3];
    Y = result[1] / result[3];
    Z = result[2] / result[3];
    
    if (mPosBuffer != null) {
      mPosBuffer.put(mIndex * 3, toFixed(X));
      mPosBuffer.put(mIndex * 3 + 1, toFixed(Y));
      mPosBuffer.put(mIndex * 3 + 2, toFixed(Z));
    }
    if (mNormalBuffer != null && mNormal != null) {
      mNormal.rotatef(angle, axis);
      mNormalBuffer.put(mIndex * 3, toFixed(mNormal.X));
      mNormalBuffer.put(mIndex * 3 + 1, toFixed(mNormal.Y));
      mNormalBuffer.put(mIndex * 3 + 2, toFixed(mNormal.Z));
    }
  }
}
