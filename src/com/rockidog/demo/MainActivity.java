package com.rockidog.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

  private static final String EXTRA_SERVER_NAME = "com.rockidog.demo.EXTRA_SERVER_NAME";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void startPainting(View view) {
    EditText edit = (EditText)findViewById(R.id.edit_server);
    String serverName = edit.getText().toString();
    if (serverName.length() == 0)
      serverName = getResources().getString(R.string.server_name);
    Intent intent = new Intent(this, PaintingActivity.class);
    intent.putExtra(EXTRA_SERVER_NAME, serverName);
    startActivity(intent);
  }
}
