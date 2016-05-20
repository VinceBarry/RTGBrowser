package com.example.vincebarry.exam.function;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by VinceBarry on 2016/5/2.
 * 那现在一起来总结一下基本的回调是如何实现的，首先创建一个接口，这个接口用于你在某个情景下执行相应的操作。
 * 接着创建一个功能类，比如这个类可以显示一个对话框、可以滑动菜单、可以下载数据等等。
 * 然后，在这个类里面声明回调接口的对象，之后在这个类里面创建在某个情景下需要执行的方法，而且在这个方法里面为声明的接口对象赋值。
 * 最后在其他的类中使用这个功能类就可以了。所以说，最少也是需要三个类共同来完成这个回调机制。
 */
public class MyDialog {
    private onButtonClick mButtonClickCallBack;
    private Context context;
    private String title;
    private String message;
    private int type;
    AlertDialog.Builder builderSec;
    public interface onButtonClick{
        public void OnPositive();
        public void OnNegative();
    }
    public MyDialog(Context context,String title,String message,int type){
        this.context = context;
        this.title = title;
        this.message = message;
        this.type = type;
        switch(type){
            case Constant.MYDIALOG_ONEBUTTON:
                builderSec = new AlertDialog.Builder(context);
                builderSec.setTitle(title);
                builderSec.setMessage(message);
                builderSec.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mButtonClickCallBack.OnNegative();
                    }
                });
                builderSec.show();
                break;
            case Constant.MYDIALOG_TWOBUTTON:
                builderSec = new AlertDialog.Builder(context);
                builderSec.setTitle(title);
                builderSec.setMessage(message);
                builderSec.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mButtonClickCallBack.OnPositive();
                    }
                });
                builderSec.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mButtonClickCallBack.OnNegative();
                    }
                });
                builderSec.show();
                break;
        }

    }

    public void setOnButtonClick(onButtonClick mNegativeCallBack) {
        this.mButtonClickCallBack = mNegativeCallBack;
    }

}
