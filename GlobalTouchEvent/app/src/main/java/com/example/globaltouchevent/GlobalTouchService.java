package com.example.globaltouchevent;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

public class GlobalTouchService extends Service implements View.OnTouchListener {
    private final String TAG = this.getClass().getSimpleName();
    private WindowManager mWindowManager;
    private LinearLayout mTouchLayout;

    public GlobalTouchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mTouchLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, LinearLayout.LayoutParams.MATCH_PARENT);
        mTouchLayout.setBackgroundColor(Color.CYAN);
        mTouchLayout.setOnTouchListener(this);

        // fetch window manager object
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
                100,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        Log.i(TAG, "add View");
        mWindowManager.addView(mTouchLayout, mParams);
    }

    @Override
    public void onDestroy() {
        if (mWindowManager != null) {
            if (mTouchLayout != null)
                mWindowManager.removeView(mTouchLayout);
        }
        super.onDestroy();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        Log.i(TAG, "onTouch:" + event.getAction());
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_UP) {
            Log.i(TAG, "Action:" + event.getAction() + "\t X: " + event.getRawX() + "\t Y: " + event.getRawY());
        }
        return true;
    }
}