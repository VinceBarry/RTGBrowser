package com.example.vincebarry.exam.ItemBean;

import java.util.Date;

/**
 * Created by VinceBarry on 2016/4/18.
 */
public class BeanFavorite {
    public String date;
    public String URL;
    public String title;
    public String tags;
    public String note;
    public String pic;//网页图标

    public BeanFavorite(String URL,String title,String tags,String note,String pic){
        this.note = note;
        this.pic = pic;
        this.tags = tags;
        this.title = title;
        this.URL = URL;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
