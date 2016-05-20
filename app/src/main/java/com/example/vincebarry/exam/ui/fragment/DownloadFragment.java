package com.example.vincebarry.exam.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.vincebarry.exam.ItemBean.BeanDownload;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.adapter.AdapterDownload;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.vincebarry.exam.broadcast.DownloadReceiver;
import com.example.vincebarry.exam.function.FileOperator;
import com.example.vincebarry.exam.function.FolderOperator;

/**
 * Created by VinceBarry on 2016/4/16.
 */
public class DownloadFragment extends Fragment {
    private ListView mListView;
    private List<File> fileList;
    private AdapterDownload mAdapterDownload;
    private View view;
    public static DownloadFragment newInstance(){
        DownloadFragment downloadFragment = new DownloadFragment();
        return downloadFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_download,container,false);

        initView();
        initEvent();
        FolderOperator folderOperator = new FolderOperator();
        fileList = folderOperator.getFileList();
        mAdapterDownload = new AdapterDownload(getActivity(),fileList);
        mListView.setAdapter(mAdapterDownload);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileOperator fileOperator = new FileOperator(getActivity(),fileList.get(position));
                getActivity().startActivity(fileOperator.openFile());
            }
        });
        return view;
    }

    private void initEvent() {
        mListView = (ListView) view.findViewById(R.id.lv_download);
    }

    private void initView() {
    }
    public void roadData(){
        mAdapterDownload.getData().clear();
        FolderOperator folderOperator = new FolderOperator();
        fileList = folderOperator.getFileList();
        mAdapterDownload.getData().addAll(fileList);
        mAdapterDownload.notifyDataSetChanged();
    }
}
