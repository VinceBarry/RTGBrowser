package com.example.vincebarry.exam.ItemBean;

import android.graphics.Bitmap;

/**
 * Created by VinceBarry on 2016/5/7.
 */
public class BeanWebPage {
    public String title;
    public Bitmap screenShot;
    public String url;

    public BeanWebPage(Bitmap screenShot, String title,String url) {
        this.screenShot = screenShot;
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getScreenShot() {
        return screenShot;
    }

    public void setScreenShot(Bitmap screenShot) {
        this.screenShot = screenShot;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
