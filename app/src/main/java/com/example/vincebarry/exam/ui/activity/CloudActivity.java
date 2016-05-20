package com.example.vincebarry.exam.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.function.SPUtils;
import com.example.vincebarry.exam.ui.fragment.CloudMainFragment;
import com.example.vincebarry.exam.ui.fragment.CloudReadFragment;
import com.example.vincebarry.exam.ui.fragment.LoginFragment;
import com.example.vincebarry.exam.ui.fragment.RegisterFragment;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by VinceBarry on 2016/5/13.
 */
public class CloudActivity extends FragmentActivity implements LoginFragment.OnLoginEvent,RegisterFragment.OnRegisterEvent,CloudMainFragment.OnCloudMainEvent{
    private FragmentManager cloudFragmentManager;
    private RegisterFragment registerFragment;
    private CloudMainFragment cloudMainFragment;
    private LoginFragment loginFragment;
    private CloudReadFragment cloudReadFragment;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud);
        Bmob.initialize(this,"4e7e5e4f40b69af8f1f052b9f933a3b5");
        loginFragment = LoginFragment.newInstance();
        cloudReadFragment = CloudReadFragment.newInstance();
        cloudMainFragment = CloudMainFragment.newInstance();
        registerFragment = RegisterFragment.newInstance();
        cloudFragmentManager = getSupportFragmentManager();

        if((Boolean)SPUtils.get(getApplicationContext(),"isLogin",false)){
            cloudFragmentManager.beginTransaction()
                    .remove(loginFragment)
                    .add(R.id.fl_cloud,cloudMainFragment,cloudMainFragment.getClass().getName())
                    .commit();
        }else{
            cloudFragmentManager.beginTransaction().add(R.id.fl_cloud,loginFragment,loginFragment.getClass().getName()).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void OnReg() {
        cloudFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(loginFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.fl_cloud,registerFragment,registerFragment.getClass().getName()).commit();
    }

    @Override
    public void OnEnter(final String name, final String passWord) {
        BmobUser user = new BmobUser();
        user.setUsername(name);
        user.setPassword(passWord);
        user.login(CloudActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                SPUtils.put(getApplicationContext(),"isLogin",true);
                SPUtils.put(getApplicationContext(),"LoginUser",name);
                SPUtils.put(getApplicationContext(),"LoginPwd",passWord);
                cloudFragmentManager.beginTransaction()
                        .remove(loginFragment)
                        .add(R.id.fl_cloud,cloudMainFragment,cloudMainFragment.getClass().getName())
                        .commit();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(),"登录失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnRegister(String user,String pwd) {
        BmobUser bu = new BmobUser();
        bu.setUsername(user);
        bu.setPassword(pwd);
        bu.signUp(CloudActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                cloudFragmentManager.beginTransaction()
                        .remove(registerFragment)
                        .add(R.id.fl_cloud,loginFragment,loginFragment.getClass().getName())
                        .commit();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getApplicationContext(),"注册失败",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void OnMainToRead(String user, String introduce, String webPage, String comments) {
        Bundle bundle = new Bundle();
        bundle.putString("user",user);
        bundle.putString("introduce",introduce);
        bundle.putString("webPage",webPage);
        bundle.putString("comments",comments);
        cloudReadFragment.setArguments(bundle);
        cloudFragmentManager.beginTransaction()
                .remove(cloudMainFragment)
                .add(R.id.fl_cloud, cloudReadFragment,cloudReadFragment.getClass().getName())
                .commit();
    }

    @Override
    public void OnUnLogin() {
        SPUtils.put(getApplicationContext(),"isLogin",false);
        SPUtils.remove(getApplicationContext(),"LoginUser");
        SPUtils.remove(getApplicationContext(),"LoginPwd");
        finish();
    }
}
