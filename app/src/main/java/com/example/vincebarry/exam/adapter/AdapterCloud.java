package com.example.vincebarry.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincebarry.exam.ItemBean.BeanFavorite;
import com.example.vincebarry.exam.ItemBean.Community;
import com.example.vincebarry.exam.R;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by VinceBarry on 2016/5/15.
 */
public class AdapterCloud extends BaseAdapter {
    private LayoutInflater inflater;//用于获取布局
    private List<Community> mDataList;//用于获取数据库信息
    Context context;


    public AdapterCloud(Context context, List<Community> list) {
        inflater = LayoutInflater.from(context);//从上下文（ListView父控件中获取上下文从而实现利用inflater在listView中将xml填充为view）
        mDataList = list;
    }

    public List<Community> getData() {
        return mDataList;
    }//将Adapter中的list返回

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
        if(convertView == null){
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_cloud,null);
            holder.fromUser = (ImageView) convertView.findViewById(R.id.iv_cloud_item_head);
            holder.title = (TextView) convertView.findViewById(R.id.tv_cloud_item_title);
            holder.url = (TextView) convertView.findViewById(R.id.tv_cloud_item_url);
            holder.like = (TextView) convertView.findViewById(R.id.tv_cloud_item_like);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Community bean = mDataList.get(position);
        holder.fromUser.setImageResource(R.drawable.ic_about);
        holder.title.setText(bean.getTitle());
        holder.url.setText(bean.getUrl());
        if(bean.getLike()!=null){
            holder.like.setText(bean.getLike()+"");
        }else{
            holder.like.setText("0");
        }
        return convertView;
    }
    class ViewHolder{
        public ImageView fromUser;
        public TextView like;
        public TextView title;
        public TextView url;
        }//ViewHolder作用：临时的储存器，把你getView方法中每次返回的View存起来，可以下次再用。
    }
