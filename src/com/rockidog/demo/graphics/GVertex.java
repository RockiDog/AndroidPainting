package com.rockidog.demo.graphics;

import java.nio.IntBuffer;

public class GVertex {

  /* x, y , z */
  float X;
  float Y;
  float Z;

  /* RGBA */
  private GColor mColor = null;
  private final short mIndex;

  public GVertex(float x, float y, float z, int index) {
    X = x;
    Y = y;
    Z = z;
    mColor = GColor.RED.clone();
    mIndex = (short)index;
  }

  public GVertex(float x, float y, float z, GColor color, int index) {
    X = x;
    Y = y;
    Z = z;
    mColor = color.clone();
    mIndex = (short)index;
  }

  public short getIndex() {
    return mIndex;
  }

  public void setColor(GColor color) {
    mColor = color;
  }

  public void put(IntBuffer posBuffer, IntBuffer colorBuffer) {
    posBuffer.put(toFixed(X));
    posBuffer.put(toFixed(Y));
    posBuffer.put(toFixed(Z));
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
  }

  static private int toFixed(float num) {
    return (int)(num * 65536.0f);
  }
}
