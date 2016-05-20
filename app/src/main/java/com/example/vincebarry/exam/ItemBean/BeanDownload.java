package com.example.vincebarry.exam.ItemBean;

/**
 * Created by VinceBarry on 2016/4/21.
 */
public class BeanDownload {

    public int size;
    public String filename;
    public String filetype;

    public BeanDownload(String filename,String filetype,int size){
        this.filename = filename;
        this.filetype = filetype;
        this.size = size;
    }
}
