package com.example.vincebarry.exam.function;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by VinceBarry on 2016/5/1.
 */
public class FolderOperator {
    public static List<File> getFileList(){
        File rootFile = new File(Environment.getExternalStorageDirectory(),"RTGBrowser");
        if(rootFile==null) {
            rootFile = FileOperator.BrowserDir();
        }
        List<File> fileList = new LinkedList<>();
        File[] fileArr = rootFile.listFiles();

            for(int i = 0;i<fileArr.length;i++){
                fileList.add(fileArr[i]);
            }

        return  fileList;
    }
}
