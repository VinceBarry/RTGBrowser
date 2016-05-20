package com.example.vincebarry.exam.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.example.vincebarry.exam.R;

/**
 * Created by VinceBarry on 2016/5/13.
 */
public class BrowserSettingFragment extends Fragment{
    private GridView mGvPic;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_browser,container,false);
        mGvPic = (GridView) view.findViewById(R.id.gv_wallpaper);

        return view;
    }

    public static BrowserSettingFragment newInstance() {
        BrowserSettingFragment mBrowserSettingFragment = new BrowserSettingFragment();
        return mBrowserSettingFragment;
    }
}
