package com.rockidog.demo.graphics;

import java.util.Scanner;

import android.util.Log;

public class Matrix4 {

  private static final String TAG = "Matrix4";

  private float m[][];

  public Matrix4() {
    m = new float[][] {
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0},
      {0, 0, 0, 0}
    };
  }

  public Matrix4(float[][] m4) {
    m = new float[4][4];
    for (int i = 0; i < 4; ++i)
      for (int j = 0; j < 4; ++j) {
        if (i < m4.length && j < m4[i].length) {
          m[i][j] = m4[i][j];
        } else {
          Log.e(TAG, "Bad parameter: incompatible matrix");
          m[i][j] = 0;
        }
      }
  }

  public Matrix4(Matrix4 other) {
    for (int i = 0; i < 4; ++i)
      for (int j = 0; j < 4; ++j)
        m[i][j] = other.m[i][j];
  }

  public void setIdentity() {
    for (int i = 0; i < 4; ++i)
      for (int j = 0; j < 4; ++j)
        m[i][j] = (i == j) ? 1 : 0;
  }

  public float[] multiply(float[] vectorT) {
    if (vectorT.length != 4) {
      Log.e(TAG, "Bad multiplication: incompatible vector");
      return null;
    }
    float[] result = new float[]{0, 0, 0, 0};
    for (int i = 0; i < 4; ++i) {
      for (int k = 0; k < 4; ++k)
        result[i] += m[i][k] * vectorT[k];
    }
    return result;
  }

  public Matrix4 multiply(Matrix4 other) {
    Matrix4 m4 = new Matrix4();
    for (int i = 0; i < 4; ++i)
      for (int j = 0; j < 4; ++j)
        for (int k = 0; k < 4; ++k)
          m4.m[i][j] += m[i][k] * other.m[k][j];
    return m4;
  }

  public Matrix4 add(Matrix4 other) {
    Matrix4 m4 = new Matrix4();
    for (int i = 0; i < 4; ++i)
      for (int j = 0; j < 4; ++j)
        m4.m[i][j] = m[i][j] + other.m[i][j];
    return m4;
  }

  @Override
  public boolean equals(Object o) {
    Matrix4 m4 = (Matrix4)o;
    for (int i = 0; i < 4; ++i)
      for (int j = 0; j < 4; ++j)
        if (m[i][j] != m4.m[i][j])
          return false;
    return true;
  }

  @Override
  public String toString() {
    String str = "Matrix 4 * 4:\n";
    for (int i = 0; i < m.length; ++i) {
      str += "\n";
      for (int j = 0; j < m[i].length; ++j) {
        if (j == 0)
          str += "|";
        str += " " + m[i][j];
        if (j == m[i].length - 1)
          str += " |\n";
      }
    }
    return str;
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);
    float m[][] = new float[4][4];
    for (int i = 0; i < 16; ++i)
      m[i / 4][i % 4] = in.nextFloat();
    in.close();
    Matrix4 m4 = new Matrix4(m);
    System.out.println(m4.multiply(m4));
  }
}
