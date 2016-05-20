package com.example.vincebarry.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincebarry.exam.R;

import java.util.List;

import com.example.vincebarry.exam.ItemBean.BeanFavorite;

/**
 * Created by VinceBarry on 2016/4/18.
 * 此adapter用于将TableFavoriteLocal与FavoriteFragment中的listview进行关联
 */
public class AdapterFavorite extends BaseAdapter{
    private LayoutInflater inflater;//用于获取布局
    private List<BeanFavorite> mDataList;//用于获取数据库信息


    public AdapterFavorite(Context context, List<BeanFavorite> list){
        inflater = LayoutInflater.from(context);//从上下文（ListView父控件中获取上下文从而实现利用inflater在listView中将xml填充为view）
        mDataList = list;
    }

    public List<BeanFavorite> getData(){
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
            convertView = inflater.inflate(R.layout.item_favorite,null);
            holder.img = (ImageView) convertView.findViewById(R.id.iv_webpage);
            holder.title = (TextView) convertView.findViewById(R.id.tv_fav_title);
            holder.url = (TextView) convertView.findViewById(R.id.tv_fav_url);
            holder.tags = (TextView) convertView.findViewById(R.id.tv_fav_tags);
            holder.note = (TextView)convertView.findViewById(R.id.tv_fav_note);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        BeanFavorite bean = mDataList.get(position);
        holder.img.setImageResource(R.drawable.ic_about);
        holder.url.setText(bean.URL);
        holder.title.setText(bean.title);
        holder.tags.setText(bean.tags);
        holder.note.setText(bean.note);
        return convertView;
    }
    class ViewHolder{
        public ImageView img;
        public TextView title;
        public TextView url;
        public TextView tags;
        public TextView note;
    }//ViewHolder作用：临时的储存器，把你getView方法中每次返回的View存起来，可以下次再用。
}
