package com.rockidog.demo;

import com.rockidog.demo.network.*;

import android.app.Activity;
//import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startPainting(View view) {
        //Intent intent = new Intent(this, PaintingActivity.class);
        //startActivity(intent);
        new HttpClient("http://10.15.198.102/AndroidServer/").execute("picture");
    }
}
