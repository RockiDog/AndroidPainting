package com.rockidog.demo.graphics;

import com.rockidog.demo.graphics.GVertex.Axis;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public abstract class GShape {

  protected GWorldModel mWolrdContext;
  protected ArrayList<GFacet> mFacetList = new ArrayList<GFacet>();
  protected ArrayList<GVertex> mVertexList = new ArrayList<GVertex>();

  public GShape(GWorldModel world) {
    mWolrdContext = world;
  }

  public void setColor(GColor color) {
    for (GVertex vertex : mVertexList)
      vertex.setColor(color);
  }

  public void addFacet(GFacet facet) {
    mFacetList.add(facet);
  }

  protected GVertex addVertex(float x, float y, float z) {
    /*
    for (GVertex v : mVertexList) {
      if (v.X == x && v.Y == y && v.Z == z)
        return v;
    }
    */
    GVertex v = mWolrdContext.addVertex(x, y, z);
    mVertexList.add(v);
    return v;
  }

  public int getIndexCount() {
    int count = 0;
    for (GFacet face : mFacetList)
      count += face.getIndexCount();
    return count;
  }

  public void put(ShortBuffer buffer) {
    for (GFacet facet : mFacetList)
      facet.put(buffer);
  }

  public void translatef(float x, float y, float z) {
    for (GVertex v : mVertexList)
      v.translatef(x, y, z);
  }

  public void rotatef(float angle, Axis axis) {
    for (GVertex v : mVertexList)
      v.rotatef(angle, axis);
  }

  public void init() {
    for (GVertex v : mVertexList)
      v.init();
  }
}
