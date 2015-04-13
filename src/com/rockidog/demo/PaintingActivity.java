package com.rockidog.demo;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class PaintingActivity extends Activity {
  private PaintingView paintingView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    paintingView = new PaintingView(this);
    setContentView(paintingView);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.pen:
        paintingView.mPaint.setXfermode(null);
        paintingView.mPaint.setStrokeWidth(6);
        return true;
      case R.id.eraser:
        paintingView.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paintingView.mPaint.setStrokeWidth(80);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
