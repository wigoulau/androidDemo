package com.example.globaltouchevent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    Intent mGlobalService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatTool.RequestOverlayPermission(this);
        mGlobalService = new Intent(this, GlobalTouchService.class);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        FloatTool.onActivityResult(requestCode, resultCode, data, this);
    }

    public void buttonClicked(View view) {
        if (view.getTag() == null) {
            startService(mGlobalService);
            view.setTag("on");
            ((Button)view).setText("Stop Service");
            Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();
        } else {
            stopService(mGlobalService);
            view.setTag(null);
            ((Button)view).setText("Start Service");
            Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
        }

    }
}