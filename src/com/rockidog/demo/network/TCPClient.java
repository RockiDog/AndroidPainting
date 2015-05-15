package com.rockidog.demo.network;

import android.content.Context;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient implements Runnable {
  private static final String TAG = "AndroidPainting";
  private volatile boolean mThreadStopped = false;
  private int mTimeInterval = 100;

  private Context mContext;
  private byte[] mData = null;
  private Socket mSocket;
  private Thread mThread;
  private String mURL;
  private int mPort;

  public TCPClient(Context context, String url, int port) {
    mContext = context;
    mURL = url;
    mPort = port;
  }

  public void setTimeInterval(int interval) {
    mTimeInterval = interval;
  }

  public int getTimeInterval() {
    return mTimeInterval;
  }

  public void stop() {
    mThreadStopped = true;
  }

  public void connect() {
    mThread = new Thread(this);
    mThread.start();
  }

  public synchronized void setBytes(byte[] bytes) {
    mData = bytes.clone();
    notify();
  }

  private static String byteToHexString(byte b) {
    int v = (int)b;
    v <<= 24;
    v >>>= 24;
    return Integer.toHexString(v);
  }

  private static String byteArrayToString(byte[] b, int offset, int length) {
    String ret = "";
    for (int i = offset; i < length; ++i) {
      ret = ret + byteToHexString(b[i]) + " ";
      if ((i - offset) % 16 == 0 && i != offset)
        ret += "\n";
    }
    return ret;
  }

  private static byte[] integerToByteArray(int num) {
    byte[] b = new byte[4];
    b[0] = (byte)(num >>> 24);
    b[1] = (byte)(num >>> 16);
    b[2] = (byte)(num >>> 8);
    b[3] = (byte)num;
    return b;
  }

  @Override
  public void run() {
    try {
      mSocket = new Socket(mURL, mPort);
    } catch (UnknownHostException e) {
      Log.e(TAG, e.getMessage());
    } catch (IOException e) {
      Log.e(TAG, e.getMessage());
    }
    while (mThreadStopped == false) {
      if (mData != null && mSocket.isConnected() == true) {
        synchronized (this) {
          try {
            DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());
            
            Log.d(TAG, "****************************************");
            Log.d(TAG, "size:  " + byteArrayToString(integerToByteArray(mData.length), 0, 4));
            Log.d(TAG, "size:  " + mData.length);
            Log.d(TAG, "image head: " + byteArrayToString(mData, 0, 4));
            Log.d(TAG, "image tail: " + byteArrayToString(mData, mData.length - 4, mData.length));
            
            dos.writeInt(mData.length);
            dos.write(mData);
          } catch (IOException e) {
            Log.e(TAG, e.getMessage());
          }
          try {
            wait();
          } catch (InterruptedException e) {
            Log.e(TAG, e.getMessage());
          }
        }
      }
    }
  }
}
