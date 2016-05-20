package com.example.vincebarry.exam.adapter;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincebarry.exam.ItemBean.BeanDownload;
import com.example.vincebarry.exam.ItemBean.BeanFavorite;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.RTGApplication;
import com.example.vincebarry.exam.function.LengthConvert;
import com.example.vincebarry.exam.ui.activity.MainActivity;

import java.io.File;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by VinceBarry on 2016/4/21.
 */
public class AdapterDownload extends BaseAdapter {

    public LayoutInflater inflater;
    //public RTGApplication mApplication;
    public Context context;
    //List<BeanDownload> mDataList;
    List<File> mDataList;
//    public AdapterDownload(Context context, List<BeanDownload> list){
//        inflater = LayoutInflater.from(context);//从上下文（ListView父控件中获取上下文从而实现利用inflater在listView中将xml填充为view）
//        mDataList = list;
//    }

    public AdapterDownload(Context context, List<File> fileList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.mDataList = fileList;
    }

    public List<File> getData() {
        return mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_download, null);
            holder.filetype = (ImageView) convertView.findViewById(R.id.iv_filepic);
            holder.filename = (TextView) convertView.findViewById(R.id.tv_down_filename);
            holder.size = (TextView) convertView.findViewById(R.id.tv_down_size);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //BeanDownload bean = mDataList.get(position);
        File file = mDataList.get(position);
        if (file.getName().contains(".")) {
            String fileType = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            Log.i("fileName", fileType);
            holder.filetype.setImageBitmap(getFileTypePic(fileType));
        } else {
            holder.filetype.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_blank));
        }
        Log.i("fileName", file.getName());
        holder.filename.setText(file.getName());
        //对文件长度进行换算后再将其传递给holder.size。
        String size = new LengthConvert().FormetFileSize(file.length());
        holder.size.setText(size);
        return convertView;
    }

    public Bitmap getFileTypePic(String fileType) {
        if (fileType.equals("m4a") || fileType.equals("mp3") || fileType.equals("mid") ||
                fileType.equals("xmf") || fileType.equals("ogg") || fileType.equals("wav")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_music);
        } else if (fileType.equals("3gp") || fileType.equals("mp4")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_video);
        } else if (fileType.equals("jpg") || fileType.equals("gif") || fileType.equals("png") ||
                fileType.equals("jpeg") || fileType.equals("bmp")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_image);

        } else if (fileType.equals("apk")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_apk);

        } else if (fileType.equals("ppt")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_ppt);

        } else if (fileType.equals("xls")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_xls);

        } else if (fileType.equals("doc")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_word);

        } else if (fileType.equals("pdf")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_word);

        } else if (fileType.equals("chm")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_word);

        } else if (fileType.equals("txt")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_text);

        } else if (fileType.equals("html")) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_web);

        }else{
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_blank);
        }
    }


    class ViewHolder{
        public TextView filename;
        public ImageView filetype ;
        public TextView size;
    }//ViewHolder作用：临时的储存器，把你getView方法中每次返回的View存起来，可以下次再用。

    }

