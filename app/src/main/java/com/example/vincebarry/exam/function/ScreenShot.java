package com.example.vincebarry.exam.function;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.support.v4.app.Fragment;
import android.webkit.WebView;
import com.example.vincebarry.exam.R;

/**
 * Created by VinceBarry on 2016/4/20.
 */
public class ScreenShot {
    private Fragment mFragment;
    private Bitmap bitmap;
    private Canvas canvas;
    private WebView mWebView;
    private Picture snap;
    public ScreenShot(Fragment fragment){
        mFragment = fragment;
    }
    public Bitmap shot(){
        mWebView = (WebView) mFragment.getView().findViewById(R.id.wv_main);
         snap = mWebView.capturePicture();
        bitmap = Bitmap.createBitmap(snap.getWidth(),snap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        snap.draw(canvas);
        return bitmap;
    }
}
