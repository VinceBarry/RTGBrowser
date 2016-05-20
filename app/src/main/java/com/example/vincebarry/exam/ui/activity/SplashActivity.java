package com.example.vincebarry.exam.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.ui.activity.MainActivity;

/**
 * Created by VinceBarry on 2016/4/16.
 */
public class SplashActivity extends Activity {
    private TextView mLineFirst;
    private TextView mLineSecond;
   private Handler mMainHandler = new Handler() {

    public void handleMessage(Message msg) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(getApplication(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
};

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        mLineFirst = (TextView) findViewById(R.id.tv_linefirst);
        mLineSecond = (TextView) findViewById(R.id.tv_linesecond);
        Typeface face = Typeface.createFromAsset(getAssets(),"fonts/NEOTERIC.ttf");
        mLineFirst.setTypeface(face);
        mLineSecond.setTypeface(face);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mMainHandler.sendEmptyMessageDelayed(0, 2000);
    }

    @Override
    public void onBackPressed() {
    }
}
