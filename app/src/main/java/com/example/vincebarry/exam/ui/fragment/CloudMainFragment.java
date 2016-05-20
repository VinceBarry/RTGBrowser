package com.example.vincebarry.exam.ui.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.vincebarry.exam.ItemBean.Community;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.adapter.AdapterCloud;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.MyDialog;
import com.example.vincebarry.exam.function.SPUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by VinceBarry on 2016/5/15.
 */
public class CloudMainFragment extends Fragment {
    private ProgressBar mPbCloud;
    private ListView mLvCloudMain;
    private BmobQuery<Community> query;
    private List<Community> contextList;
    private AdapterCloud mAdapterCloud;
    private ImageView mIvSync;
    private ImageView mIvUnlogin;
    public OnCloudMainEvent mCallBack;

    public interface OnCloudMainEvent{
        public void OnMainToRead(String user,String introduce,String webPage,String comments);
        public void OnUnLogin();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mCallBack = (OnCloudMainEvent) activity;
        }catch (Exception e){

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloudmain,container,false);
        mLvCloudMain = (ListView) view.findViewById(R.id.lv_cloud_main);
        mPbCloud = (ProgressBar) view.findViewById(R.id.pb_cloud);
        mIvSync= (ImageView) view.findViewById(R.id.bt_cloud_refresh);
        mIvUnlogin = (ImageView) view.findViewById(R.id.bt_cloud_unlogin);
        contextList = new ArrayList<>();
        query = new BmobQuery<>();
        query.findObjects(getContext(), new FindListener<Community>() {
            @Override
            public void onSuccess(List<Community> list) {
                    contextList.addAll(list);
                    mAdapterCloud = new AdapterCloud(getContext(),contextList);
                    mPbCloud.setVisibility(View.GONE);
                    mLvCloudMain.setAdapter(mAdapterCloud);
            }

            @Override
            public void onError(int i, String s) {

            }
        });
        mLvCloudMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user = contextList.get(position).getFromuser();
                String introduce = "这个人很懒，什么都没留下";
                String webPage = contextList.get(position).getWebpage();
                String comments = contextList.get(position).getComment();
                mCallBack.OnMainToRead(user,introduce,webPage,comments);
            }
        });
        mIvUnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog unloginDialog = new MyDialog(getContext(),"RTGBrowser","是否注销登录？", Constant.MYDIALOG_TWOBUTTON);
                unloginDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        mCallBack.OnUnLogin();
                    }

                    @Override
                    public void OnNegative() {

                    }
                });

            }
        });
        mIvSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCloud.notifyDataSetChanged();
            }
        });

        return view;
    }

    public static CloudMainFragment newInstance() {
        CloudMainFragment cloudMainFragment = new CloudMainFragment();
        return cloudMainFragment;
    }
}
