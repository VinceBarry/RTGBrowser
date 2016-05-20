package com.example.vincebarry.exam.table;

import android.graphics.Bitmap;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by VinceBarry on 2016/4/29.
 */
public class TableHistoryLocal extends DataSupport {
    private int id;
    private String URL;
    private String title;
    private Bitmap icon;
    private String date;

    public String getURL() {
        return URL;
    }

    public String getTitle() {
        return title;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
