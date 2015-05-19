package com.rockidog.demo.graphics;

public class GCube extends GShape {

  public enum FaceIndex {
    BOTTOM(0), FRONT(1), LEFT(2), RIGHT(3), BACK(4), TOP(5);
    
    private final int index;
    
    private FaceIndex(int index) {
      this.index = index;
    }
    
    public int getIndex() {
      return index;
    }
  }

  public GCube(GWorldModel world, float left, float bottom, float back, float right, float top, float front) {
    super(world);
    
    GVertex leftBottomBack   = addVertex(left,  bottom, back);
    GVertex rightBottomBack  = addVertex(right, bottom, back);
    GVertex leftTopBack      = addVertex(left,  top,    back);
    GVertex rightTopBack     = addVertex(right, top,    back);
    GVertex leftBottomFront  = addVertex(left,  bottom, front);
    GVertex rightBottomFront = addVertex(right, bottom, front);
    GVertex leftTopFront     = addVertex(left,  top,    front);
    GVertex rightTopFront    = addVertex(right, top,    front);
    
    addFacet(new GFacet(leftBottomBack,  leftBottomFront,  rightBottomFront, rightBottomBack));   // bottom
    addFacet(new GFacet(leftBottomFront, leftTopFront,     rightTopFront,    rightBottomFront));  // front
    addFacet(new GFacet(leftBottomBack,  leftTopBack,      leftTopFront,     leftBottomFront));   // left
    addFacet(new GFacet(rightBottomBack, rightBottomFront, rightTopFront,    rightTopBack));      // right
    addFacet(new GFacet(leftBottomBack,  rightBottomBack,  rightTopBack,     leftTopBack));       // back
    addFacet(new GFacet(leftTopBack,     rightTopBack,     rightTopFront,    leftTopFront));      // top
  }

  public void setFacetColor(FaceIndex index, GColor color) {
    mFacetList.get(index.getIndex()).setColor(color);
  }

  public GColor getFacetColor(FaceIndex index) {
    return mFacetList.get(index.getIndex()).getColor();
  }

  public void addFacet(GFacet facet) {
    mFacetList.add(facet);
  }
}
