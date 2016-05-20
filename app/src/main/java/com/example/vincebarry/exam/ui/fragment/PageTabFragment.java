package com.example.vincebarry.exam.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.example.vincebarry.exam.ItemBean.BeanWebPage;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.adapter.AdapterWebPager;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.ui.activity.MainActivity;
import com.melnykov.fab.FloatingActionButton;
import java.util.List;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by VinceBarry on 2016/4/26.
 * CurrentPageList
 */
public class PageTabFragment extends Fragment {
    private ViewPager viewPager;
    private List<BeanWebPage> beanWebPages;
    private List<WebView> mWebViewList;
    private FloatingActionButton mFab;
    private AdapterWebPager mAdapterWebPager;
    private CircleIndicator indicator;
    public static PageTabFragment newInstance() {
        PageTabFragment pageTabFragment = new PageTabFragment();
        return pageTabFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_pagetab, container, false);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        indicator  = (CircleIndicator) v.findViewById(R.id.indicator);
        mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Constant.ACTION_CLOSE_PAGETAB);
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.sendBroadcast(intent);
            }
        });
        beanWebPages = (List<BeanWebPage>) this.getArguments().getSerializable("List");
        mWebViewList = (List<WebView>) this.getArguments().getSerializable("webview");
        mAdapterWebPager = new AdapterWebPager(getContext(),beanWebPages,mWebViewList);
        roadData();
        viewPager.setAdapter(mAdapterWebPager);
        indicator.setViewPager(viewPager);
        return v;
    }


    public void roadData(){
        mAdapterWebPager.notifyDataSetChanged();
    }
}
