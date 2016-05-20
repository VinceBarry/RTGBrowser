package com.example.vincebarry.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincebarry.exam.ItemBean.BeanFavorite;
import com.example.vincebarry.exam.ItemBean.BeanHistory;
import com.example.vincebarry.exam.R;

import java.util.List;

/**
 * Created by VinceBarry on 2016/4/29.
 */
public class AdapterHistory extends BaseAdapter {

    private LayoutInflater inflater;//用于获取布局
    private List<BeanHistory> mDataList;//用于获取数据库信息

    public AdapterHistory(Context context, List<BeanHistory> list){
        inflater = LayoutInflater.from(context);//从上下文（ListView父控件中获取上下文从而实现利用inflater在listView中将xml填充为view）
        mDataList = list;
    }

    public List<BeanHistory> getData(){
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
            convertView = inflater.inflate(R.layout.item_history,null);
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_his_icon);
            holder.title = (TextView) convertView.findViewById(R.id.tv_his_title);
            holder.url = (TextView) convertView.findViewById(R.id.tv_his_url);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        BeanHistory bean = mDataList.get(position);
        holder.icon.setImageResource(R.drawable.ic_about);
        holder.url.setText(bean.URL);
        holder.title.setText(bean.title);
        return convertView;
    }
    class ViewHolder{
        public ImageView icon;
        public TextView title;
        public TextView url;
    }//ViewHolder作用：临时的储存器，把你getView方法中每次返回的View存起来，可以下次再用。
}
