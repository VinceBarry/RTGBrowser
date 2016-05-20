package com.example.vincebarry.exam.function;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vincebarry.exam.ItemBean.Community;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.ui.activity.CloudActivity;
import com.example.vincebarry.exam.ui.activity.MainActivity;
import com.example.vincebarry.exam.ui.fragment.MainFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Map;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by VinceBarry on 2016/5/3.
 */
public class Sharer {
    private Context context;
    private String content;
    public Sharer(Context context,String content){
        this.context =context;
        this.content = content;

    }


    public void showShare() {
        ShareSDK.initSDK(context);
        OnekeyShare oks = new OnekeyShare();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.share_qr);
        String label = "二维码";
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view = inflater.inflate(R.layout.dialog_qr,null);
                ImageView mIvQR = (ImageView) view.findViewById(R.id.iv_qr);
                Bitmap bitmap = EncodeQR(content);
                mIvQR.setImageBitmap(bitmap);
                new AlertDialog.Builder(context)
                        .setView(view)
                        .create()
                        .show();

            }
        };
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater2 = LayoutInflater.from(context);
                View view = inflater2.inflate(R.layout.dialog_cloud,null);
                final EditText mTvTitle = (EditText) view.findViewById(R.id.tv_dialog_cloud_title);
                final EditText mTvContext = (EditText) view.findViewById(R.id.tv_dialog_cloud_comment);
                new AlertDialog.Builder(context)
                        .setView(view)
                        .setPositiveButton("分享", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO:ERROR
                                //判断是否已经登录
                                if(!(Boolean) SPUtils.get(context.getApplicationContext(),"isLogin",false)){
                                    Toast.makeText(context,"你尚未登录",Toast.LENGTH_SHORT).show();
                                }else{
                                    if(!mTvTitle.equals("")&&!mTvContext.equals("")){
                                        //TODO:
                                        Community com = new Community();
                                        com.setComment(mTvContext.getText().toString());
                                        com.setFromuser((String) SPUtils.get(context.getApplicationContext(),"LoginUser","匿名用户"));
                                        com.setTitle(mTvTitle.getText().toString());
                                        com.setUrl(content);
                                        com.setWebpage("Test");
                                        com.save(context, new SaveListener() {
                                            @Override
                                            public void onSuccess() {
                                                Toast.makeText(context,"分享到社区成功",Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(int i, String s) {
                                                Toast.makeText(context,"无法分享到社区",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }else{
                                        Toast.makeText(context,"输入内容不规范",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            }
        };
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_cloud);
        String label2 = "社区分享";
        oks.setCustomerLogo(bitmap,label,listener);
        oks.setCustomerLogo(bitmap2,label2,listener2);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("RTGBrowser");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(content);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("说些什么");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("RTGBrowser");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(content);

// 启动分享GUI
        oks.show(context);
    }


    private Bitmap EncodeQR(String content) {
        Bitmap bitmap = null;
        int width = 400;
        int height = 400;
        Map hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
