package com.example.vincebarry.exam.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincebarry.exam.R;

/**
 * Created by VinceBarry on 2016/5/15.
 */
public class CloudReadFragment extends Fragment {
    private TextView mTvUser;
    private TextView mTvIntro;
    private TextView mTvWebPage;
    private TextView mTvComment;
    private ImageView mIvComment;
    private FrameLayout mFlComment;
    private String user;
    private String intro;
    private String webPage;
    private String comment;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_read,container,false);
        mTvComment = (TextView) view.findViewById(R.id.tv_cloud_read_comment);
        mTvWebPage = (TextView) view.findViewById(R.id.tv_cloud_read_context);
        mTvUser = (TextView) view.findViewById(R.id.tv_cloud_read_user);
        mFlComment = (FrameLayout) view.findViewById(R.id.fl_cloud_comment);
        mTvIntro = (TextView) view.findViewById(R.id.tv_cloud_read_intro);
        mIvComment = (ImageView) view.findViewById(R.id.iv_cloud_read_comment);
        user = getArguments().getString("user");
        intro = getArguments().getString("introduce");
        webPage = getArguments().getString("webPage");
        comment = getArguments().getString("comments");
        mTvComment.setText(comment);
        mTvIntro.setText(intro);
        mTvWebPage.setText(webPage);
        mTvUser.setText(user);
        mIvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlComment.setVisibility(View.VISIBLE);
                mIvComment.setClickable(false);
            }
        });
        return view;
    }

    public static CloudReadFragment newInstance() {
        CloudReadFragment cloudReadFragment = new CloudReadFragment();
        return cloudReadFragment;
    }
}
