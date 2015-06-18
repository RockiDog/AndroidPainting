package com.rockidog.demo.graphics;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class GWorldModel {

  private static final String TAG = "GWorldModel";
  private int mDebugFlag          = 0;
  private int mIndexCount         = 0;

  private IntBuffer mVertexBuffer;         // All shared veritces position buffer
  private IntBuffer mColorBuffer;          // ALl shared vertices color buffer
  private IntBuffer mNormalBuffer;         // ALl shared normal buffer
  private ShortBuffer mIndexBuffer;        // All vertices
  private ArrayList<GVertex> mVertexList;  // Store all the shared vertices
  private ArrayList<GShape> mShapeList;    // Store all the shapes

  public GWorldModel() {
    mVertexList = new ArrayList<GVertex>();
    mShapeList = new ArrayList<GShape>();
  }

  public void setup() {
    ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mVertexList.size() * 4 * 3);
    byteBuffer.order(ByteOrder.nativeOrder());
    mVertexBuffer = byteBuffer.asIntBuffer();
    
    byteBuffer = ByteBuffer.allocateDirect(mVertexList.size() * 4 * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    mColorBuffer = byteBuffer.asIntBuffer();
    
    byteBuffer = ByteBuffer.allocateDirect(mVertexList.size() * 3 * 4);
    byteBuffer.order(ByteOrder.nativeOrder());
    mNormalBuffer = byteBuffer.asIntBuffer();
    
    Log.i(TAG, mShapeList.size() + " shapes");
    byteBuffer = ByteBuffer.allocateDirect(mIndexCount * 2);
    byteBuffer.order(ByteOrder.nativeOrder());
    mIndexBuffer = byteBuffer.asShortBuffer();
    
    for (GVertex vertex : mVertexList)
      vertex.put(mVertexBuffer, mColorBuffer, mNormalBuffer);
    for (GShape shape : mShapeList)
      shape.put(mIndexBuffer);
  }

  public void draw(GL10 gl) {
    mVertexBuffer.position(0);
    mColorBuffer.position(0);
    mIndexBuffer.position(0);
    
    /* Output debug message */
    if (mDebugFlag == 1) {
      for (int i = 0; i < mIndexCount; ++i) {
        String info = new String();
        int index = mIndexBuffer.get(i);
        info += "X " + Integer.toString(mVertexBuffer.get(index * 3)) + " : ";
        info += "Y " + Integer.toString(mVertexBuffer.get(index * 3 + 1)) + " : ";
        info += "Z " + Integer.toString(mVertexBuffer.get(index * 3 + 2)) + " : ";
        Log.i(TAG, info);
      }
      for (int i = 0; i < mIndexCount; ++i) {
        String info = new String();
        int index = mIndexBuffer.get(i);
        info += "R " + Integer.toString(mColorBuffer.get(index * 4)) + " : ";
        info += "G " + Integer.toString(mColorBuffer.get(index * 4 + 1)) + " : ";
        info += "B " + Integer.toString(mColorBuffer.get(index * 4 + 2)) + " : ";
        info += "A " + Integer.toString(mColorBuffer.get(index * 4 + 3)) + " : ";
        Log.i(TAG, info);
      }
      mDebugFlag = 0;
    }
    
    gl.glFrontFace(GL10.GL_CW);
    gl.glShadeModel(GL10.GL_FLAT);
    gl.glVertexPointer(3, GL10.GL_FIXED, 0, mVertexBuffer);
    gl.glColorPointer(4, GL10.GL_FIXED, 0, mColorBuffer);
    gl.glNormalPointer(GL10.GL_FIXED, 0, mNormalBuffer);
    gl.glDrawElements(GL10.GL_TRIANGLES, mIndexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
  }

  public void addShape(GShape shape) {
    if (shape != null) {
      mShapeList.add(shape);
      mIndexCount += shape.getIndexCount();
    }
  }

  public GVertex addVertex(float x, float y, float z) {
    GVertex vertex = new GVertex(x, y, z, mVertexList.size());
    mVertexList.add(vertex);
    return vertex;
  }

  public ArrayList<GShape> getShapeList() {
    return mShapeList;
  }
}
