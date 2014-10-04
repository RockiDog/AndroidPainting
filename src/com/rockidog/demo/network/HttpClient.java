package com.rockidog.demo.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClient extends AsyncTask<Bitmap, Void, Void> {
    private URL mUrl;
    private HttpURLConnection mConnection;
    private boolean transferringEnable = false;

    public HttpClient(String url) {
        super();
        try {
            mUrl = new URL(url);
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void connect() {
        try {
            mConnection = (HttpURLConnection) mUrl.openConnection();
            mConnection.setDoOutput(true);
            transferringEnable = true;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void send(Bitmap bitmap) {
        try {
            OutputStream outputStream = new BufferedOutputStream(mConnection.getOutputStream());
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            mConnection.disconnect();
        }
    }

    @Override
    protected Void doInBackground(Bitmap... bitmap) {
        if (true == transferringEnable) {
            connect();
            if (0 < bitmap.length)
                send(bitmap[0]);
        }
        return null;
    }
}
