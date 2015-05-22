package com.rockidog.demo.graphics;

public class GVector {

  private GVertex mStart = null;
  private GVertex mEnd   = null;
  private float X, Y, Z;

  public GVector(GVertex start, GVertex end) {
    mStart = start;
    mEnd = end;
    X = mEnd.X - mStart.X;
    Y = mEnd.Y - mStart.Y;
    Z = mEnd.Z - mStart.Z;
  }

  public GVector(float x, float y, float z) {
    X = x;
    Y = y;
    Z = z;
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
