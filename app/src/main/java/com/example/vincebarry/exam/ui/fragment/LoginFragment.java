package com.example.vincebarry.exam.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vincebarry.exam.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by VinceBarry on 2016/5/13.
 */
public class LoginFragment extends Fragment {
    private EditText mEtUser;
    private EditText mEtPassword;
    private Button mBtEnter;
    private Button mBtReg;
    private OnLoginEvent mCallBack;
    public interface OnLoginEvent{
        public void OnReg();
        public void OnEnter(String name,String passWord);
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            //在此处将activity强转成接口
            mCallBack = (OnLoginEvent) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginEvent");
        }
        super.onAttach(activity);
    }

    public static LoginFragment newInstance(){
        LoginFragment loginFragment = new LoginFragment();
        return loginFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_login,container,false);
        mEtUser = (EditText) view.findViewById(R.id.et_cloud_login_user);
        mEtPassword = (EditText) view.findViewById(R.id.et_cloud_login_pwd);
        mBtEnter = (Button) view.findViewById(R.id.bt_cloud_login_enter);
        mBtReg = (Button) view.findViewById(R.id.bt_cloud_login_reg);
        mBtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.OnReg();
            }
        });
        mBtEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mEtUser.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                if(user.equals("")&&password.equals("")){
                    Toast.makeText(getContext(),"用户名或密码输入错误",Toast.LENGTH_SHORT).show();
                }else{
                    mCallBack.OnEnter(user,password);
                }
            }
        });
        return view;
    }
}
