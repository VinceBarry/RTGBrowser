package com.example.vincebarry.exam.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincebarry.exam.ItemBean.BeanWebPage;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.ui.activity.MainActivity;
import com.example.vincebarry.exam.ui.fragment.MainFragment;

import java.util.List;

/**
 * Created by VinceBarry on 2016/5/7.
 */
public class AdapterWebPager extends PagerAdapter {
    private Context context;
    private List<BeanWebPage> mBeanWebPageList;
    private List<WebView> mWebViewList;

    public AdapterWebPager(Context context, List<BeanWebPage> mBeanWebPageList, List<WebView> mWebViewList) {
        this.context = context;
        this.mBeanWebPageList = mBeanWebPageList;
        this.mWebViewList = mWebViewList;
    }

    //初始化Item
    public List<BeanWebPage> getData() {
        return mBeanWebPageList;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {
        View view = View.inflate(context, R.layout.item_webpage, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_item_page_title);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_item_page_shot);
        textView.setText(mBeanWebPageList.get(position).getTitle());
        final Bitmap bitmap = Bitmap.createBitmap(mWebViewList.get(position).getWidth(), mWebViewList.get(position).getHeight(),
                Bitmap.Config.ARGB_8888);
        final Canvas c = new Canvas(bitmap);
        mWebViewList.get(position).draw(c);
        imageView.setImageBitmap(bitmap);
        // imageView.setImageBitmap(mBeanWebPageList.get(position).getScreenShot());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Constant.ACTION_CHANGE_WEBVIEW);
                intent.putExtra("position", position);
                MainActivity mainActivity = (MainActivity) context;
                mainActivity.sendBroadcast(intent);
            }
        });
        collection.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return mBeanWebPageList.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
