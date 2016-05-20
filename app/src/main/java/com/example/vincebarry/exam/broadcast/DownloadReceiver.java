package com.example.vincebarry.exam.broadcast;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.ui.activity.MainActivity;
/**
 * Created by VinceBarry on 2016/4/23.
 */
public class DownloadReceiver extends BroadcastReceiver {
    public MainActivity mainActivity;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){
            mainActivity = (MainActivity)context;
            mainActivity.ChangeFragment(2);
        }else if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            //通知Activity调用让DownloadFragment中的ListView去更新数据
            mainActivity = (MainActivity)context;
            mainActivity.RefreshDownload();
        }else if(intent.getAction().equals(Constant.ACTION_CHANGE_WEBVIEW)){
            mainActivity = (MainActivity)context;
            mainActivity.OnChangeWebView(intent.getIntExtra("position",0));
        }else if(intent.getAction().equals(Constant.ACTION_NEWTAB_URL)){
            mainActivity = (MainActivity)context;
            mainActivity.passURLToMain(intent.getStringExtra("URL"));
        }else if(intent.getAction().equals(Constant.ACTION_CLOSE_PAGETAB)){
            mainActivity = (MainActivity)context;
            mainActivity.closePageTab();
        }
    }
}
