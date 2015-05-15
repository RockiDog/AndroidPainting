package com.rockidog.demo.graphics;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public class GFacet {

  private ArrayList<GVertex> mVertices = new ArrayList<GVertex>();

  public GFacet(GVertex v0, GVertex v1, GVertex v2, GVertex v3) {
    mVertices.add(v0);
    mVertices.add(v1);
    mVertices.add(v2);
    mVertices.add(v3);
  }

  ArrayList<GVertex> getVertices() {
    return mVertices;
  }

  int getIndexCount() {
    return (mVertices.size() - 2) * 3;
  }

  void put(ShortBuffer buffer) {
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
}
