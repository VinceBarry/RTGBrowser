package com.example.vincebarry.exam.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.MyDialog;
import com.example.vincebarry.exam.function.SPUtils;
import com.example.vincebarry.exam.ui.activity.MainActivity;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * Created by VinceBarry on 2016/4/16.
 */
public class SettingFragment extends Fragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{
    private RelativeLayout mRlBrowse;
    private RelativeLayout mRlCall;
    private RelativeLayout mRlOpen;
    private LinearLayout mLLSubMenu;
    private SwitchButton mSbPic;
    private SwitchButton mSbPort;
    private RelativeLayout mRlMainURL;
    private OnSettingToSubSetting mCallBack;
    private int SOH;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
            case R.id.sb_setting_item_pic:
                if(isChecked){
                    Log.i("settingFragment","pic"+"isChecked");
                    SPUtils.put(getActivity(),"isPicShowed",new Boolean(true));

                }else{
                    Log.i("settingFragment","pic"+"isNoneChecked");
                    SPUtils.put(getActivity(),"isPicShowed",new Boolean(false));
                }
                break;
            case R.id.sb_setting_item_port:
                if(isChecked){
                    Log.i("settingFragment","port"+"isChecked");
                    SPUtils.put(getActivity(),"isPort",new Boolean(true));
                }else{
                    Log.i("settingFragment","port"+"isNoneChecked");
                    SPUtils.put(getActivity(),"isPort",new Boolean(false));
                }
                break;
        }
    }

    public interface OnSettingToSubSetting{
        public void OnSettingToBrowser();
    }
    public static SettingFragment newInstance(){
        SettingFragment settingFragment = new SettingFragment();
        return settingFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //在此处将activity强转成接口
            mCallBack = (OnSettingToSubSetting) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting,container,false);
        mRlBrowse = (RelativeLayout) view.findViewById(R.id.rl_item_browse);
        mRlCall = (RelativeLayout) view.findViewById(R.id.rl_item_call);
        mRlOpen = (RelativeLayout) view.findViewById(R.id.rl_item_open);
        mRlMainURL = (RelativeLayout) view.findViewById(R.id.rl_item_mainurl);
        mLLSubMenu = (LinearLayout) view.findViewById(R.id.item_setting_sub);
        mSbPic = (SwitchButton) view.findViewById(R.id.sb_setting_item_pic);
        mSbPort = (SwitchButton) view.findViewById(R.id.sb_setting_item_port);
        //mRlSkin = (RelativeLayout) view.findViewById(R.id.rl_item_skin);

        //mRlSkin.setOnClickListener(this);
        SOH = 0;
        mRlCall.setOnClickListener(this);
        mRlOpen.setOnClickListener(this);
        mRlBrowse.setOnClickListener(this);
        mSbPic.setChecked((Boolean) SPUtils.get(getActivity(),"isPicShowed",new Boolean(true)));
        mSbPort.setChecked((Boolean) SPUtils.get(getActivity(),"isPort",new Boolean(true)));
        mSbPic.setOnCheckedChangeListener(this);
        mRlMainURL.setOnClickListener(this);
        mSbPort.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.rl_item_mainurl:
                final EditText mEtMainURL = new EditText(getContext());
                mEtMainURL.setText((String) SPUtils.get(getActivity(),"CONTENT_MAINURL","http://www.baidu.com/"));
                new AlertDialog.Builder(getContext())
                        .setTitle("修改主页")
                        .setView(mEtMainURL)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SPUtils.put(getActivity(),"CONTENT_MAINURL",mEtMainURL.getText().toString());
                            }
                        })
                        .create()
                        .show();
                break;
           // case R.id.rl_item_skin:

             //   break;
            case R.id.rl_item_browse:
                //mCallBack.OnSettingToBrowser();
                switch(SOH){
                    case 0:
                        mLLSubMenu.setVisibility(View.VISIBLE);
                        SOH = 1;
                        break;
                    case 1:
                        mLLSubMenu.setVisibility(View.GONE);
                        SOH = 0;
                        break;
                }

                break;
            case R.id.rl_item_open:
                MyDialog openDialog = new MyDialog(getContext(),"开源许可","RTGBrowser使用以下开源库：\n"+"com.android.support:appcompat-v7:23.2.1\n" +
                        "br.com.liveo:navigationdrawer-material:2.5.1\n" +
                        "org.litepal.android:core:1.3.1\n" +
                        "com.melnykov:floatingactionbutton:1.3.0\n" +
                        "me.relex:circleindicator:1.2.1@aar\n" +
                        "zxing.jar", Constant.MYDIALOG_ONEBUTTON);
                openDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {

                    }

                    @Override
                    public void OnNegative() {

                    }
                });
                break;
        }
    }
}
