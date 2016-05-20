package com.example.vincebarry.exam.function;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.vincebarry.exam.ui.fragment.MainFragment;

import java.io.File;

/**
 * Created by VinceBarry on 2016/5/7.
 */
public class WebViewSetter {
    private WebView mWebView;
    private String URL;
    private Context context;
    private Fragment fragment;
    private DownloadManager downloadManager;
    private MainFragment.HelloWebViewClient helloWebViewClient;
    private MainFragment.HelloWebChromeClient helloWebChromeClient;

    public WebViewSetter(Context context, Fragment fragment, WebView webView, String url, DownloadManager downloadManager, MainFragment.HelloWebViewClient helloWebViewClient, MainFragment.HelloWebChromeClient helloWebChromeClient){
        this.fragment = fragment;
        this.context = context;
        this.mWebView = webView;
        this.downloadManager = downloadManager;
        this.helloWebChromeClient = helloWebChromeClient;
        this.helloWebViewClient = helloWebViewClient;
        this.URL = url;
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void Setting(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(helloWebViewClient);
        mWebView.setWebChromeClient(helloWebChromeClient);
        mWebView.setLongClickable(true);
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                fragment.unregisterForContextMenu(mWebView);
                WebView.HitTestResult result = mWebView.getHitTestResult();
                if(result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE){
                    //TODO:LinkedOnLongClicked.
                    URL = result.getExtra();
                    Intent intent = new Intent();
                    intent.setAction(Constant.ACTION_NEWTAB_URL);
                    intent.putExtra("URL",URL);
                    context.sendBroadcast(intent);
                    setURL(URL);
                    Log.i("URL",URL);
                }
                fragment.registerForContextMenu(mWebView);
                return false;
            }
        });
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                String contentTemp[] = url.split("/");
                String content = contentTemp[contentTemp.length - 1];
                Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
                downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                File downloadWay = new File(Environment.getExternalStorageDirectory(), "RTGBrowser");
                if (downloadWay != null) {
                    downloadWay.mkdirs();
                }
                request.setDestinationInExternalPublicDir("RTGBrowser", content);
                downloadManager.enqueue(request);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            }
        });
        //END
    }

}
