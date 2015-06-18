package com.rockidog.demo.graphics;

import com.rockidog.demo.graphics.GVertex.Axis;

public class GVector {

  private GVertex mStart = null;
  private GVertex mEnd   = null;
  public float X, Y, Z;
  public float mOriginX, mOriginY, mOriginZ;

  public GVector(GVertex start, GVertex end) {
    mStart = start;
    mEnd = end;
    mOriginX = X = mEnd.X - mStart.X;
    mOriginY = Y = mEnd.Y - mStart.Y;
    mOriginZ = Z = mEnd.Z - mStart.Z;
  }

  public GVector(float x, float y, float z) {
    mOriginX = X = x;
    mOriginY = Y = y;
    mOriginZ = Z = z;
  }

  public GVector(GVector v) {
    mStart = v.mStart;
    mEnd = v.mEnd;
    mOriginX = v.mOriginX;
    mOriginY = v.mOriginY;
    mOriginZ = v.mOriginZ;
    X = v.X;
    Y = v.Y;
    Z = v.Z;
  }

  public void init() {
    X = mOriginX;
    Y = mOriginY;
    Z = mOriginZ;
  }

  public float getNorm() {
    return (float)Math.sqrt(X * X + Y * Y + Z * Z);
  }

  public GVector getUnit() {
    float norm = getNorm();
    float x = X / norm;
    float y = Y / norm;
    float z = Z / norm;
    return new GVector(x, y, z);
  }

  public GVector add(GVector v) {
    return new GVector(X + v.X, Y + v.Y, Z + v.Z);
  }

  public GVector multiply(float c) {
    return new GVector(X * c, Y * c, Z * c);
  }

  public float dotMultiply(GVector v) {
    return X * v.X + Y * v.Y + Z * v.Z;
  }

  public GVector crossMultiply(GVector v) {
    float x = Y * v.Z - v.Y * Z;
    float y = v.X * Z - X * v.Z;
    float z = X * v.Y - v.X * Y;
    return new GVector(x, y, z);
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
  }

  public static float getDotProduct(GVector v1, GVector v2) {
    return v1.X * v2.X + v1.Y * v2.Y + v1.Z * v2.Z;
  }

  public static GVector getSum(GVector v1, GVector v2) {
    return new GVector(v1.X + v2.X, v1.Y + v2.Y, v1.Z + v2.Z);
  }

  public static GVector getCrossProduct(GVector v1, GVector v2) {
    float x = v1.Y * v2.Z - v2.Y * v1.Z;
    float y = v2.X * v1.Z - v1.X * v2.Z;
    float z = v1.X * v2.Y - v2.X * v1.Y;
    return new GVector(x, y, z);
  }
}
