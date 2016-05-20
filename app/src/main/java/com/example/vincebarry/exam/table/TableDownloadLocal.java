package com.example.vincebarry.exam.table;

import org.litepal.crud.DataSupport;

/**
 * Created by VinceBarry on 2016/4/21.
 */
public class TableDownloadLocal extends DataSupport {

    private long size;
    private String filename;
    private String filetype;

    public long getSize() {
        return size;
    }

    public String getFilename() {
        return filename;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
