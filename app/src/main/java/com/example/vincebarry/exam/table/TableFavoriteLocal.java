package com.example.vincebarry.exam.table;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by VinceBarry on 2016/4/18.
 * 此类是收藏夹的映射类，用于本地保存收藏夹书签信息，包括收藏时间，网址，注释，标题（默认为网页title），分类
 */
public class TableFavoriteLocal extends DataSupport{
    //int、short、long、float、double、boolean、String和Date
    private int id;
    private String date;
    private String URL;
    private String title;
    private String tags;
    private String note;
    private String pic;//网页图标

    public void setNote(String note) {
        this.note = note;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getDate() {
        return date;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getTags() {
        return tags;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getPic() {
        return pic;
    }

    public void setId(int id) {
        this.id = id;
    }

}
