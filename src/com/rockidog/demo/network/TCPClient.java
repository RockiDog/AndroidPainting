package com.rockidog.demo.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;

public class TCPClient implements Runnable {

  private static final String TAG = "AndroidPainting";
  private volatile boolean mThreadStopped = false;
  private int mTimeInterval = 100;

  private Context mContext;
  private byte[] mData = null;
  private LinkedHashMap<Integer, byte[]> mReceivedImages = null;
  private Socket mSocket;
  private Thread mThread;
  private String mURL;
  private int mPort;
  private boolean mObjectFileTransmitFlag = false;

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

  public synchronized void transmitObjectFile(final int fileId) {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        try {
          DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());
          dos.writeInt(fileId);
          DataInputStream dis = new DataInputStream(mSocket.getInputStream());
          int objSize = dis.readInt();
          byte[] objBuffer = new byte[objSize];
          Log.i(TAG, "Tranmitting in background...");
          for (int offset = 0; offset < objSize;) {
            int count = dis.read(objBuffer, offset, objSize - offset);
            if (count == -1)
              break;
            offset += count;
          }
          FileOutputStream fis = mContext.openFileOutput(fileId + ".obj", Context.MODE_PRIVATE);
          fis.write(objBuffer);
          fis.close();
          Log.i(TAG, "Tranmitting in background exits");
          mObjectFileTransmitFlag = true;
        } catch (IOException e) {
          Log.e(TAG, e.getMessage());
        }
        return null;
      }
    }.execute();
  }

  public synchronized boolean isObjectFileTransmitOK() {
    if (mObjectFileTransmitFlag == true) {
      mObjectFileTransmitFlag = false;
      Log.i(TAG, "Object transmit OK");
      return true;
    }
    return false;
  }

  public synchronized LinkedHashMap<Integer, byte[]> getImages() {
    if (mReceivedImages != null) {
      LinkedHashMap<Integer, byte[]> result = mReceivedImages;
      mReceivedImages = null;
      return result;
    }
    return null;
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
      if (mData != null && mSocket != null && mSocket.isConnected() == true) {
        synchronized (this) {
          try {
            DataOutputStream dos = new DataOutputStream(mSocket.getOutputStream());
            
            Log.d(TAG, "****************************************");
            Log.d(TAG, "size: " + byteArrayToString(integerToByteArray(mData.length), 0, 4));
            Log.d(TAG, "size: " + mData.length);
            Log.d(TAG, "image head: " + byteArrayToString(mData, 0, 4));
            Log.d(TAG, "image tail: " + byteArrayToString(mData, mData.length - 4, mData.length));
            
            dos.writeInt(mData.length);
            dos.write(mData);
            
            /* 
             * Receive image stream
             * Protocol:
             *  | image_count | img0_id | img0_size | img0_body | img1_size | img1_body | ... |
             *  |    4-bit    |   4-bit |    4-bit  |   n0-bit  |    4-bit  |   n1-bit  | ... |
             */
            mReceivedImages = new LinkedHashMap<Integer, byte[]>();
            DataInputStream dis = new DataInputStream(mSocket.getInputStream());
            int imageCount = dis.readInt();
            for (int i = 0; i < imageCount; ++i) {
              while (dis.available() <= 0) {
                try {
                  Thread.sleep(mTimeInterval);
                } catch (InterruptedException e) {
                  Log.e(TAG, e.getMessage());
                }
              }
              int id = dis.readInt();
              int size = dis.readInt();
              Log.w(TAG, "Receiving Image " + id + ": " + size + " bytes");
              
              byte[] buffer = new byte[size];
              for (int offset = 0; offset < size;) {
                int count = dis.read(buffer, offset, size - offset);
                if (count == -1)
                  break;
                offset += count;
              }
              
              mReceivedImages.put(id, buffer);
            }
            Log.i(TAG, imageCount + " images received");
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
