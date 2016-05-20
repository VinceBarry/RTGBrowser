package com.example.vincebarry.exam.function;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * 此类用于实现数据保存操作
 * Created by VinceBarry on 2016/4/20.
 */
public class FileOperator {
    private String text;
    private Bitmap bitmap;
    private String mimetype;
    private static Context context;
    private File father;
    private String bitmapName;
    private File file;
    private File fileBitmap;
    private int progress;
    private String filename;
    private InputStream inputStream;
    public Runnable runnable;
    public final static int TEXT_WRITE = 0;
    public final static int BYTE_WRITE = 1;

    public FileOperator(Context context,InputStream inputStream,String filename){
        //构造方法用于写入二进制流
        this.inputStream = inputStream;
        this.context = context;
        this.filename = filename;
    }
    public FileOperator(Context context,InputStream inputStream,String filename,String mimetype){
        //构造方法用于写入二进制流
        this.inputStream = inputStream;
        this.context = context;
        this.filename = filename;
        this.mimetype = mimetype;
    }
    public FileOperator(Context context,String text,String filename){
        //构造函数传入待写入数据,此方法写入文本
        this.text = text;
        this.context = context;
        this.filename = filename;
    }
    public FileOperator(Context context,File file){
        this.context = context;
        this.file = file;
    }
    public FileOperator(String bitmapName,Bitmap bitmap){
        //此方法写入图像
        this.bitmapName = bitmapName;
        this.bitmap = bitmap;
    }
    public Intent openFile(){
        if(!file.exists()) return null;
        /* 取得扩展名 */
        String end=file.getName().substring(file.getName().lastIndexOf(".") + 1,file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(file);
        }else if(end.equals("3gp")||end.equals("mp4")){
            return getAudioFileIntent(file);
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getImageFileIntent(file);
        }else if(end.equals("apk")){
            return getApkFileIntent(file);
        }else if(end.equals("ppt")){
            return getPptFileIntent(file);
        }else if(end.equals("xls")){
            return getExcelFileIntent(file);
        }else if(end.equals("doc")){
            return getWordFileIntent(file);
        }else if(end.equals("pdf")){
            return getPdfFileIntent(file);
        }else if(end.equals("chm")){
            return getChmFileIntent(file);
        }else if(end.equals("txt")){
            return getTextFileIntent(file,false);
        }else{
            return getAllIntent(file);
        }
    }

    //Android获取一个用于打开APK文件的intent  
    public static Intent getAllIntent( File file ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri,"*/*");
        return intent;
    }
    //Android获取一个用于打开APK文件的intent  
    public static Intent getApkFileIntent( File file ) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    //Android获取一个用于打开VIDEO文件的intent  
    public static Intent getVideoFileIntent( File file ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    //Android获取一个用于打开AUDIO文件的intent  
    public static Intent getAudioFileIntent( File file ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    //Android获取一个用于打开Html文件的intent     
    public static Intent getHtmlFileIntent( File file ){

        Uri uri = Uri.parse(file.toString() ).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(file.toString() ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    //Android获取一个用于打开图片文件的intent  
    public static Intent getImageFileIntent( File file ) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    //Android获取一个用于打开PPT文件的intent     
    public static Intent getPptFileIntent( File file ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    //Android获取一个用于打开Excel文件的intent     
    public static Intent getExcelFileIntent( File file ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //Android获取一个用于打开Word文件的intent     
    public static Intent getWordFileIntent( File file ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    //Android获取一个用于打开CHM文件的intent     
    public static Intent getChmFileIntent( File file ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    //Android获取一个用于打开文本文件的intent     
    public static Intent getTextFileIntent( File file, boolean paramBoolean){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (paramBoolean){
            Uri uri1 = Uri.parse(file.toString() );
            intent.setDataAndType(uri1, "text/plain");
        }else{
            Uri uri2 = Uri.fromFile(file);
            intent.setDataAndType(uri2, "text/plain");
        }
        return intent;
    }
    //Android获取一个用于打开PDF文件的intent     
    public static Intent getPdfFileIntent( File file ){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }

    public int writeBitmap(){
        int rescode = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isSdCardExist()) {
//            byte[] buf = new byte[2048];
//            int len = 0;
                    if (fileBitmap == null) {
                        fileBitmap = new File(BrowserDir(),bitmapName+".jpg");
                    }
                    try {
                        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileBitmap));
                        bitmap.compress(Bitmap.CompressFormat.JPEG,90,bos);
                        bos.flush();
                        bos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return rescode;
    }
    public int write(int request) {
        //request为TEXT_WRITE 时写入文本，为BYTE_WRITE写入二进制
        int result = 0;//返回读写是否正确，0为错误1为正确
        //写入流程：1.检测sd卡是否存在
        if(isSdCardExist()){
            //存在,可以读写之
            // path = Environment.getExternalStorageDirectory().getAbsolutePath();
            father = BrowserDir();
            Log.i("path",father.toString());
            if(request == TEXT_WRITE){
                filename = filename + ".txt";
                file = new File(father,filename);
            }
            if(request == BYTE_WRITE){
                //filename = filename +"." +mimetype;
                file = new File(father,filename);
            }

            Log.i("path",file.toString());
            Log.i("path","file exists?"+file.exists()+"");
            if(file.exists()){
                Toast.makeText(context,"文件已存在", Toast.LENGTH_SHORT).show();
            }else{
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("path","file exists?"+file.exists()+"");
                if(request == TEXT_WRITE){
                    BufferedWriter bw = null;
                    try {

                        bw = new BufferedWriter(new FileWriter(file));
                        bw.write(text);
                        bw.flush();
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    result = 1;
                }
                //使用IO流写入文件操作
                if(request == BYTE_WRITE){
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            byte[] buf = new byte[2048];
                            int len = 0;
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                while((len = inputStream.read(buf)) != -1){
                                    fos.write(buf,0,len);
                                }

                                fos.flush();
                                fos.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    new Thread(runnable).start();

                }
            }
        }else{
            //不存在
            Toast.makeText(context,"无法检测到SD卡", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
    public int read(){
        int result = 0;//返回读写是否正确
        return result;
    }
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    public static File BrowserDir(){
        File pub = Environment.getExternalStorageDirectory();
        Log.i("dir","pub:"+pub.exists());
        File dir = new File(pub,"RTGBrowser");
        Log.i("dir",dir.toString());
        if(!dir.exists()){
            dir.mkdirs();
        }
        Log.i("dir",dir.mkdirs()+"");
        return dir;
    }
}
