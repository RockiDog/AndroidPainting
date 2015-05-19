package com.rockidog.demo;

import java.io.ByteArrayOutputStream;

import com.rockidog.demo.network.TCPClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PaintingView extends View {

  private static final String TAG = "PaintingView";
  private static final float TOUCH_TOLERANCE = 4;

  /* For button UI */
  private float mButtonR;
  private float mButtonOuterR;
  private float mButtonX;
  private float mButtonY;
  private boolean mButtonPressed = false;
  private boolean mButtonClicked = false;
  private MaskFilter mBlur = new BlurMaskFilter(16, BlurMaskFilter.Blur.SOLID);
  private Paint mButtonPaint = new Paint();

  private float mX;
  private float mY;

  private Bitmap mBitmap;
  private Canvas mCanvas;
  private Path mPath;
  public Paint mPaint;

  private TCPClient mTCPClient;

  public PaintingView(Context context) {
    super(context);
    
    mPath = new Path();
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setDither(true);
    mPaint.setColor(Color.WHITE);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeJoin(Paint.Join.ROUND);
    mPaint.setStrokeCap(Paint.Cap.ROUND);
    mPaint.setStrokeWidth(6);
    
    mButtonPaint.setAntiAlias(true);
    
    String server = context.getString(R.string.server_name);
    int port = Integer.parseInt(context.getString(R.string.port_number));
    mTCPClient = new TCPClient(context, server, port);
    mTCPClient.connect();
  }

  @Override
  protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
    super.onSizeChanged(width, height, oldWidth, oldHeight);
    mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    mCanvas = new Canvas(mBitmap);
    
    mButtonR = width / 10.0f;
    mButtonOuterR = mButtonR * 1.1f;
    mButtonX = width / 2.0f;
    mButtonY = height - mButtonR * 2;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    canvas.drawColor(Color.DKGRAY);
    canvas.drawBitmap(mBitmap, 0, 0, null);
    canvas.drawPath(mPath, mPaint);
    
    /* Draw button */
    if (mButtonPressed == false) {
      mButtonPaint.setMaskFilter(null);
      mButtonPaint.setColor(Color.LTGRAY);
      canvas.drawCircle(mButtonX, mButtonY, mButtonOuterR, mButtonPaint);
      mButtonPaint.setColor(Color.WHITE);
      canvas.drawCircle(mButtonX, mButtonY, mButtonR, mButtonPaint);
    } else {
      mButtonPaint.setMaskFilter(mBlur);
      mButtonPaint.setColor(Color.WHITE);
      canvas.drawCircle(mButtonX, mButtonY, mButtonOuterR, mButtonPaint);
      mButtonPaint.setColor(Color.LTGRAY);
      canvas.drawCircle(mButtonX, mButtonY, mButtonR, mButtonPaint);
    }
    
    /* Blinking */
    if (mButtonClicked == true) {
      try {
        Thread.sleep(200);
        canvas.drawColor(Color.LTGRAY, PorterDuff.Mode.DARKEN);
      } catch (InterruptedException e) {
        Log.e(TAG, e.getMessage());
      }
      mButtonClicked = false;
      invalidate();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();
    float y = event.getY();
    float dist2 = (float)(Math.pow(x - mButtonX, 2) + Math.pow(y - mButtonY, 2));
    switch(event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (dist2 < (float)Math.pow(mButtonR, 2)) {
          mButtonPressed = true;
        } else {
          mButtonPressed = false;
          touchDown(x, y);
        }
        invalidate();
        break;
      case MotionEvent.ACTION_MOVE:
        if (mButtonPressed == false) {
          touchMove(x, y);
          invalidate();
        } break;
      case MotionEvent.ACTION_UP:
        if (dist2 < (float)Math.pow(mButtonR, 2)) {
          if (mButtonPressed == false) {
            mButtonPressed = false;
          } else {
            mButtonPressed = false;
            mButtonClicked = true;
          }
        } else {
          mButtonPressed = false;
        }
        touchUp();
        invalidate();
        sendImage();
        break;
      default:
        break;
    }
    return true;
  }

  private void touchDown(float x, float y) {
    mPath.reset();
    mPath.moveTo(x, y);
    mX = x;
    mY = y;
  }

  private void touchMove(float x, float y) {
    final float dx = Math.abs(x - mX);
    final float dy = Math.abs(y - mY);
    if (dx > TOUCH_TOLERANCE || dy > TOUCH_TOLERANCE) {
      mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
      mX = x;
      mY = y;
    }
  }

  private void touchUp() {
    mPath.lineTo(mX, mY);
    mCanvas.drawPath(mPath, mPaint);
    mPath.reset();
  }

  private void sendImage() {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    mBitmap.compress(CompressFormat.JPEG, 10, os);
    mTCPClient.setBytes(os.toByteArray());
  }
}
