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

/**
 * Created by VinceBarry on 2016/5/15.
 */
public class RegisterFragment extends Fragment {
    private Button mBtReg;
    private EditText mEtUser;
    private EditText mEtPwd;
    private EditText mEtPwdAgain;
    private OnRegisterEvent mCallBack;
    public interface OnRegisterEvent{
        public void OnRegister(String user,String pwd);
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            //在此处将activity强转成接口
            mCallBack = (OnRegisterEvent) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginEvent");
        }
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cloud_reg,container,false);
        mBtReg   = (Button) view.findViewById(R.id.bt_cloud_reg);
        mEtUser   = (EditText) view.findViewById(R.id.et_cloud_reg_user);
        mEtPwd   = (EditText) view.findViewById(R.id.et_cloud_reg_pwd);
        mEtPwdAgain   = (EditText) view.findViewById(R.id.et_cloud_reg_pwdagain);
        mBtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mEtUser.getText().toString().trim();
                String pwd = mEtPwd.getText().toString().trim();
                String pwdAgain = mEtPwdAgain.getText().toString().trim();
                if(user.length()<6){
                    Toast.makeText(getContext(),"用户名必须大于6位",Toast.LENGTH_SHORT).show();
                }else{
                    if(!pwd.equals(pwdAgain)){
                        Toast.makeText(getContext(),"前后两次密码不匹配",Toast.LENGTH_SHORT).show();
                    }else{
                        mCallBack.OnRegister(user,pwd);
                    }
                }


            }
        });
        return view;
    }

    public static RegisterFragment newInstance() {
        RegisterFragment registerFragment = new RegisterFragment();
        return registerFragment;
    }
}
