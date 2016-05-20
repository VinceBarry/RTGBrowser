package com.example.vincebarry.exam.ItemBean;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by VinceBarry on 2016/4/20.
 */
public class BeanHistory {
    public String title;
    public String URL;
    public String icon;
    public String date;
    public BeanHistory(String title,String URL,String icon){
        this.title = title;
        this.URL = URL;
        this.icon = icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
