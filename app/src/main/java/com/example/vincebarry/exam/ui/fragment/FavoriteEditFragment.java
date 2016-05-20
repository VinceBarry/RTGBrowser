package com.example.vincebarry.exam.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.MyDialog;
import com.example.vincebarry.exam.table.TableHistoryLocal;

import org.litepal.crud.DataSupport;

/**
 * Created by VinceBarry on 2016/5/4.
 */
public class FavoriteEditFragment extends Fragment implements TextWatcher{
    private EditText mEtTitle;
    private EditText mEtContent;
    private TextView mTvUrl;
    private TextView mTvOk;
    private ImageView mIvBack;
    private ImageView mIvDelete;
    private EditText mEtTags;
    private String TempTags;
    private String TempTitle;
    private String TempContent;
    private OnToFavorite mCallBack;
    private String IsEdited = "false";//是否被更改
    private String date;

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if((mEtTitle.getText().toString().equals(TempTitle))&&(mEtContent.getText().toString().equals(TempContent))&&(mEtTags.getText().toString().equals(TempTags))){
            IsEdited = "false";
        }else{
            IsEdited ="true";
        }
    }

    public interface OnToFavorite{
        public void OnEditToFavorite(int res,String args,String... args2);

    }
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //在此处将activity强转成接口
            mCallBack = (OnToFavorite) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    public static FavoriteEditFragment newInstance() {
        Bundle args = new Bundle();
        //通信机制
        FavoriteEditFragment fragment = new FavoriteEditFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite_edit,container,false);
        mEtContent = (EditText) v.findViewById(R.id.et_fav_edit_content);
        mEtTags = (EditText) v.findViewById(R.id.et_fav_edit_tags);
        mEtTitle = (EditText) v.findViewById(R.id.et_fav_edit_title);
        mTvUrl = (TextView) v.findViewById(R.id.tv_fav_edit_url);
        mTvOk = (TextView) v.findViewById(R.id.bt_fav_edit_ok);
        mIvBack = (ImageView) v.findViewById(R.id.iv_fav_edit_back);
        mIvDelete = (ImageView) v.findViewById(R.id.et_fav_edit_delete);
        mEtTitle.setText(getArguments().getString("title"));
        mEtContent.setText(getArguments().getString("content"));
        mEtTags.setText(getArguments().getString("tags"));
        TempTitle = mEtTitle.getText().toString();
        TempContent = mEtContent.getText().toString();
        TempTags = mEtTags.getText().toString();
        mEtContent.addTextChangedListener(this);
        mEtTitle.addTextChangedListener(this);
        mEtTags.addTextChangedListener(this);
        date = getArguments().getString("date");
        mTvUrl.setText(getArguments().getString("Url"));
        mTvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String updateTitle = mEtTitle.getText().toString();
                    String updateContent = mEtContent.getText().toString();
                    String updateTags = mEtTags.getText().toString();
                    mCallBack.OnEditToFavorite(Constant.EDITTOFAV_OK,date,IsEdited,updateTitle,updateContent,updateTags);
            }
        });
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.OnEditToFavorite(Constant.EDITTOFAV_BACK,"","","","","");
            }
        });
        mIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updateTitle = mEtTitle.getText().toString();
                String updateContent = mEtContent.getText().toString();
                String updateTags = mEtTags.getText().toString();
                mCallBack.OnEditToFavorite(Constant.EDITTOFAV_DELETE,date,IsEdited,updateTitle,updateContent,updateTags);
            }
        });

        return v;
    }
}
