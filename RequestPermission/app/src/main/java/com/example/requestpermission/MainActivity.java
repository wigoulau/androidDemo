package com.example.requestpermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";

    private final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "申请权限", Toast.LENGTH_SHORT).show();
        checkPermission();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        ) {
            requestPermissions(new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            }, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.w(TAG, "onRequestPermissionsResult: " + requestCode + ", permissions:" + permissions + ", grantResults:"+grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                boolean result = true;
                for (String permission : permissions) {
                    Log.e(TAG, "permission:" + permission);
                }
                for (int each : grantResults) {
                    Log.e(TAG, "each is " + each);
                    if (each != PackageManager.PERMISSION_GRANTED) {
                        result = false;
                        break;
                    }
                }
                if (!result) {
                    Toast.makeText(this, "权限申请失败", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}