package com.rockidog.demo.graphics;

import android.util.Log;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public class GFacet {

  private static final String TAG = "GFacet";

  private ArrayList<GVertex> mVertices = new ArrayList<GVertex>();
  private GColor mColor = GColor.TRANSPARENT;

  public GFacet(GVertex v0, GVertex v1, GVertex v2, GVertex v3) {
    mVertices.add(v0);
    mVertices.add(v1);
    mVertices.add(v2);
    mVertices.add(v3);
    mColor = mVertices.get(mVertices.size() - 1).getColor();
  }

  public GFacet(Object[] vertices) {
    for (Object v : vertices)
      mVertices.add((GVertex)v);
    mColor = mVertices.get(mVertices.size() - 1).getColor();
  }

  public ArrayList<GVertex> getVertices() {
    return mVertices;
  }

  public int getIndexCount() {
    return (mVertices.size() - 2) * 3;
  }

  public void put(ShortBuffer buffer) {
    GVertex v0 = mVertices.get(0);
    GVertex vn = mVertices.get(mVertices.size() - 1);
    
    for (int i = 1; i < mVertices.size() - 1; ++i) {
      GVertex v1 = mVertices.get(i);
      buffer.put(v0.getIndex());
      buffer.put(v1.getIndex());
      buffer.put(vn.getIndex());
      v0 = v1;
    }
  }

  public void setColor(GColor color) {
    int last = mVertices.size() - 1;
    if (last < 2) {
      Log.e(TAG, "not enough vertices in setColor()");
    } else {
      mVertices.get(last).setColor(color);
    }
    mColor = mVertices.get(last).getColor();
  }

  public GColor getColor() {
    return mColor;
  }
}
