package com.rockidog.demo.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

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
            mConnection.setChunkedStreamingMode(0);
            mConnection.setRequestProperty("Content-Type", "image/jpeg");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        transferringEnable = true;
    }

    private void send(Bitmap bitmap) {
        try {
            OutputStream outputStream = mConnection.getOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, outputStream);
            outputStream.flush();
            outputStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            mConnection.disconnect();
            transferringEnable = false;
        }
    }

    @Override
    protected Void doInBackground(Bitmap... bitmap) {
        connect();
        if (true == transferringEnable) {
            send(bitmap[0]);
        }
        return null;
    }
}
