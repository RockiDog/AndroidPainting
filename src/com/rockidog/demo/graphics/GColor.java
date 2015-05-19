package com.rockidog.demo.graphics;

public class GColor {

  public static final GColor WHITE       = new GColor(0x10000, 0x10000, 0x10000);
  public static final GColor BLACK       = new GColor(0, 0, 0);
  public static final GColor RED         = new GColor(0x10000, 0, 0);
  public static final GColor GREEN       = new GColor(0, 0x10000, 0);
  public static final GColor BLUE        = new GColor(0, 0, 0x10000);
  public static final GColor YELLOW      = new GColor(0x10000, 0x10000, 0);
  public static final GColor CYAN        = new GColor(0, 0x10000, 0x10000);
  public static final GColor MAGENTA     = new GColor(0x10000, 0, 0x10000);
  public static final GColor TRANSPARENT = new GColor(0, 0, 0, 0);

  public final int R;
  public final int G;
  public final int B;
  public final int A;

  public GColor(int r, int g, int b, int a) {
    R = r;
    G = g;
    B = b;
    A = a;
  }

  public GColor(int r, int g, int b) {
    R = r;
    G = g;
    B = b;
    A = 0x10000;
  }

  public GColor(GColor other) {
    R = other.R;
    G = other.G;
    B = other.B;
    A = other.A;
  }

  @Override
  public boolean equals(Object c) {
    GColor color = (GColor)c;
    if (this.R == color.R
        && this.G == color.G
        && this.B == color.B
        && this.A == color.A)
      return true;
    return false;
  }
}
