package com.rockidog.demo.graphics;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

import android.util.Log;

public class GObject extends GShape {

  private static final String TAG = "GObject";
  private Scanner mIn;
  
  public GObject(GWorldModel world, InputStream is) {
    super(world);
    mIn = new Scanner(new BufferedReader(new InputStreamReader(is)));
    loadObject();
  }

  private void loadObject() {
    float x = 0, y = 0, z = 0;
    Scanner line = null;
    while (mIn.hasNextLine()) {
      line = new Scanner(mIn.nextLine());
      if (line.hasNext()) {
        String head = line.next();
        if (head.equals("v")) {
          x = line.nextFloat();
          y = line.nextFloat();
          z = line.nextFloat();
          addVertex(x, y, z);
        } else if (head.equals("f")) {
          ArrayList<GVertex> vertices = new ArrayList<GVertex>();
          while (line.hasNext()) {
            int index = Integer.parseInt(line.next().split("/")[0]) - 1;
            GVertex v = mVertexList.get(index);
            vertices.add(v);
          }
          addFacet(new GFacet((vertices.toArray())));
        }
        line.close();
        continue;
      } else {
        line.close();
      }
    }
    if (line != null)
      line.close();
    Log.i(TAG, mVertexList.size() + " vertices");
    Log.i(TAG, mFacetList.size() + " facets");
  }
}
