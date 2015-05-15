package com.rockidog.demo.graphics;

import java.nio.ShortBuffer;
import java.util.ArrayList;

public class GShape {

  private GWorldModel mWolrdContext;
  private ArrayList<GFacet> mFacetList = new ArrayList<GFacet>();
  private ArrayList<GVertex> mVertexList = new ArrayList<GVertex>();

  public GShape(GWorldModel world, float left, float bottom, float back, float right, float top, float front) {
    mWolrdContext = world;
    
    GVertex leftBottomBack   = addVertex(left,  bottom, back);
    GVertex rightBottomBack  = addVertex(right, bottom, back);
    GVertex leftTopBack      = addVertex(left,  top,    back);
    GVertex rightTopBack     = addVertex(right, top,    back);
    GVertex leftBottomFront  = addVertex(left,  bottom, front);
    GVertex rightBottomFront = addVertex(right, bottom, front);
    GVertex leftTopFront     = addVertex(left,  top,    front);
    GVertex rightTopFront    = addVertex(right, top,    front);
    
    addFacet(new GFacet(leftBottomBack,  leftBottomFront,  rightBottomFront, rightBottomBack));
    addFacet(new GFacet(leftBottomFront, leftTopFront,     rightTopFront,    rightBottomFront));
    addFacet(new GFacet(leftBottomBack,  leftTopBack,      leftTopFront,     leftBottomFront));
    addFacet(new GFacet(rightBottomBack, rightBottomFront, rightTopFront,    rightTopBack));
    addFacet(new GFacet(leftBottomBack,  rightBottomBack,  rightTopBack,     leftTopBack));
    addFacet(new GFacet(leftTopBack,     rightTopBack,     rightTopFront,    leftTopFront));
  }

  public void setColor(GColor color) {
    for (GVertex vertex : mVertexList)
      vertex.setColor(color);
  }

  public void addFacet(GFacet facet) {
    mFacetList.add(facet);
  }

  private GVertex addVertex(float x, float y, float z) {
    for (GVertex v : mVertexList) {
      if (v.X == x && v.Y == y && v.Z == z)
        return v;
    }
    
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
}
