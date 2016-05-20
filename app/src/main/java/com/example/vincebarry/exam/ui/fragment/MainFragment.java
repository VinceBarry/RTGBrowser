package com.example.vincebarry.exam.ui.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.vincebarry.exam.ItemBean.BeanWebPage;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.SPUtils;
import com.example.vincebarry.exam.function.WebViewSetter;
import com.example.vincebarry.exam.table.TableFavoriteLocal;
import org.litepal.tablemanager.Connector;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.example.vincebarry.exam.function.FileOperator;
import com.example.vincebarry.exam.function.ScreenShot;
import com.example.vincebarry.exam.table.TableHistoryLocal;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import okhttp3.Call;

public class MainFragment extends Fragment implements View.OnClickListener {

    //Layout
    private FrameLayout mProFrame;
    private LinearLayout mLLWebView;
    private LinearLayout mLLToolBar1;
    private LinearLayout mLLToolBar2;
    private FrameLayout mFlDialog;
    private LinearLayout mLLSaveBar;

    //View
    private WebView mWebView;
    private View view;
    private EditText mEtInput;//网址输入框
    private ImageButton mBtShare;
    private TextView mTvLinkTo;//跳转按钮
    private ImageButton mBtBack;//向后跳转
    private ImageButton mBtForward;//向前跳转
    private ProgressBar mProgressBar;
    private ImageView mSearchOrLinkTo;//用于切换搜索和链接
    private ImageView mIvDelete;
    private ImageButton mBtSwitch1;
    private ImageButton mBtSwitch2;
    private ImageButton mBtFavorite;
    private ImageButton mBtStop;
    private ImageButton mBtMainPage;
    private ImageButton mBtRefresh;
    private TextView mBtDigSave;
    private TextView mBtDigCancel;
    private EditText mEtDigTitle;
    private EditText mEtDigContext;
    private EditText mEtDigTags;
    private ImageView mBtTextSave;
    private ImageView mBtScreenShot;
    private ImageView mBtSave;
    private ImageView mBtPage;
    private ImageView mBtQR;

    //变量
    private boolean newTabFlag;
    private String URL;
    private int newProgress;
    public List<WebView> mWebViewList;
    private int SeOrLt;//此变量用于判断和切换搜索和链接
    private int SAVEBAR_STATE;
    public List<BeanWebPage> mWebPageList;
    private SQLiteDatabase db;
    private DownloadManager downloadManager;
    //功能类
    private OnMainFragmentListener mCallBack;

    public static MainFragment newInstance() {
        MainFragment mMainFragment = new MainFragment();
        return mMainFragment;
    }

    public void setURL(String URL) {
        this.URL =URL;
    }

    public interface OnMainFragmentListener {
        //回调接口

        public void onRefresh(int res);//刷新HistoryFragment和FavoriteFragment

        public void OnChangeToPage(List<BeanWebPage> mWebPageList, List<WebView> mWebViewList);

        public void onStartQRScanner();//QRScanner回调方法

        public void onShared(String str);//shareSDK回调方法

        public void OnChangeWebView(int position);

        public void OnPassList(List<BeanWebPage> mWebPageList, List<WebView> mWebViewList);
    }

    public String getURL() {
        return URL;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallBack = (OnMainFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        //分别用于储存新标签页的WebView和WebView信息
        mWebViewList = new ArrayList<>();
        mWebPageList = new ArrayList<>();
        SeOrLt = Constant.STATIC_LINKTO;//0为链接，1为搜索，初始化为链接
        SAVEBAR_STATE = 0;
        db = Connector.getDatabase();
        initView();
        initEvent();
        setURL((String) SPUtils.get(getActivity(),"CONTENT_MAINURL","http://www.baidu.com/"));
        WebViewSetter webViewSetter = new WebViewSetter(getContext(), MainFragment.this, mWebView, URL, downloadManager, new HelloWebViewClient(), new HelloWebChromeClient());
        webViewSetter.Setting();
        newTabFlag = true;
        mWebView.loadUrl(URL);
        mWebViewList.add(mWebView);
        mEtDigTitle.setText(mWebView.getTitle().toString());

        return view;
    }

    private void initEvent() {
        mBtMainPage.setOnClickListener(this);
        mBtShare.setOnClickListener(this);
        mBtQR.setOnClickListener(this);
        mBtPage.setOnClickListener(this);
        mBtStop.setOnClickListener(this);
        mBtRefresh.setOnClickListener(this);
        mBtForward.setOnClickListener(this);
        mBtDigSave.setOnClickListener(this);
        mBtDigCancel.setOnClickListener(this);
        mBtBack.setOnClickListener(this);
        mTvLinkTo.setOnClickListener(this);
        mSearchOrLinkTo.setOnClickListener(this);
        mBtSwitch1.setOnClickListener(this);
        mBtSwitch2.setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mBtFavorite.setOnClickListener(this);
        mBtScreenShot.setOnClickListener(this);
        mBtTextSave.setOnClickListener(this);
        mBtSave.setOnClickListener(this);

        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (s.toString().trim().equals("")) {
                    mIvDelete.setVisibility(View.GONE);
                } else {
                    mIvDelete.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //编辑文字时底部工具栏消失
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Do nothing.
                if (s.toString().trim().equals("")) {
                    mIvDelete.setVisibility(View.GONE);
                } else {
                    mIvDelete.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initView() {
        mLLWebView = (LinearLayout) view.findViewById(R.id.ll_webview);
        mBtMainPage = (ImageButton) view.findViewById(R.id.bt_mainpage);
        mBtShare = (ImageButton) view.findViewById(R.id.bt_share);
        mBtQR = (ImageView) view.findViewById(R.id.iv_qr);
        mBtPage = (ImageView) view.findViewById(R.id.bt_page);
        mBtStop = (ImageButton) view.findViewById(R.id.bt_stop);
        mBtRefresh = (ImageButton) view.findViewById(R.id.bt_refresh);
        mWebView = (WebView) view.findViewById(R.id.wv_main);
        mEtInput = (EditText) view.findViewById(R.id.et_input);
        mTvLinkTo = (TextView) view.findViewById(R.id.tv_linkto);
        mBtBack = (ImageButton) view.findViewById(R.id.bt_back);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pr_ld);
        mBtForward = (ImageButton) view.findViewById(R.id.bt_forward);
        mProFrame = (FrameLayout) view.findViewById(R.id.fl_bar);
        mSearchOrLinkTo = (ImageView) view.findViewById(R.id.iv_searchorlinkto);
        mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
        mLLToolBar1 = (LinearLayout) view.findViewById(R.id.ll_toolbar1);
        mLLToolBar2 = (LinearLayout) view.findViewById(R.id.ll_toolbar2);
        mBtSwitch2 = (ImageButton) view.findViewById(R.id.bt_switch2);
        mBtSwitch1 = (ImageButton) view.findViewById(R.id.bt_switch1);
        mBtFavorite = (ImageButton) view.findViewById(R.id.bt_favorite);
        mFlDialog = (FrameLayout) view.findViewById(R.id.fl_dialog);
        mBtDigSave = (TextView) view.findViewById(R.id.bt_dig_save);
        mBtDigCancel = (TextView) view.findViewById(R.id.bt_dig_cancel);
        mEtDigTitle = (EditText) view.findViewById(R.id.et_dig_title);
        mEtDigContext = (EditText) view.findViewById(R.id.et_dig_context);
        mEtDigTags = (EditText) view.findViewById(R.id.et_dig_tags);
        mBtTextSave = (ImageView) view.findViewById(R.id.bt_textsave);
        mBtScreenShot = (ImageView) view.findViewById(R.id.bt_screenshot);
        mLLSaveBar = (LinearLayout) view.findViewById(R.id.savebar);
        mBtSave = (ImageView) view.findViewById(R.id.bt_save);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_mainpage:
                mWebView.loadUrl((String) SPUtils.get(getActivity(),"CONTENT_MAINURL","http://www.baidu.com/"));
                break;
            case R.id.bt_share:
                mCallBack.onShared(URL);
                break;
            case R.id.iv_qr:
                //扫描二维码：调用接口通知Activity去启动扫码器，当扫完得到结果后将结果返回处理返回给WebView。
                mCallBack.onStartQRScanner();
                break;
            case R.id.bt_page:
                mCallBack.OnChangeToPage(mWebPageList, mWebViewList);
                break;
            case R.id.bt_stop:
                mWebView.stopLoading();
                break;
            case R.id.bt_refresh:
                mWebView.reload();
                break;
            case R.id.bt_save:
                switch (SAVEBAR_STATE) {
                    case 0:
                        mLLSaveBar.setVisibility(View.VISIBLE);
                        SAVEBAR_STATE = 1;
                        break;
                    case 1:
                        mLLSaveBar.setVisibility(View.GONE);
                        SAVEBAR_STATE = 0;
                        break;
                }

                break;
            case R.id.bt_textsave:
                //文字保存
                String fileName = new Date().toString();
                OkHttpUtils.get().url(getURL()).build().execute(new FileCallBack(FileOperator.BrowserDir().getAbsolutePath(),fileName+".html") {
                                                                    @Override
                                                                    public void inProgress(float progress, long total) {
                                                                        if(progress == 1){
                                                                            Toast.makeText(getActivity(),"网页保存成功",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onError(Call call, Exception e) {
                                                                        Toast.makeText(getActivity(),"网页保存失败",Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    @Override
                                                                    public void onResponse(File response) {

                                                                    }
                                                                });
                break;
            case R.id.bt_screenshot:
                mLLSaveBar.setVisibility(View.GONE);
                SAVEBAR_STATE = 0;
                ScreenShot mShot = new ScreenShot(MainFragment.this);
                Long currentTime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("", Locale.SIMPLIFIED_CHINESE);
                sdf.applyPattern("yyyy年MM月dd日HH时mm分ss秒");
                String shotName = sdf.format(currentTime);
                FileOperator mOperator = new FileOperator(shotName, mShot.shot());
                mOperator.writeBitmap();
                Toast.makeText(getActivity(), "网页图片已保存", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                getActivity().sendBroadcast(intent);
                //屏幕截图
                break;

            case R.id.bt_dig_cancel:
                mFlDialog.setVisibility(View.GONE);
                break;
            case R.id.bt_dig_save:
                //写入Favorite数据库

                Date dateTime = new Date();
                String URL = getURL();
                String date = dateTime.getTime() + "";
                String Title = mEtDigTitle.getText().toString();
                String Context = mEtDigContext.getText().toString();
                String tags = mEtDigTags.getText().toString().trim();

                if (tags.equals("")) {
                    tags = "默认";
                }
                if (Context.equals("")) {
                    Context = "这个家伙很懒，什么都没留下";
                }

                TableFavoriteLocal mTableFavoriteLocal = new TableFavoriteLocal();
                mTableFavoriteLocal.setTitle(Title);
                mTableFavoriteLocal.setNote(Context);
                mTableFavoriteLocal.setTags(tags);
                mTableFavoriteLocal.setDate(date);
                mTableFavoriteLocal.setURL(URL);
                mTableFavoriteLocal.save();
                //TODO:Write the data
                mCallBack.onRefresh(Constant.RES_FAVORITE);
                mFlDialog.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "书签保存成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.bt_favorite:
                //当按下收藏夹按钮后，弹出输入框
                mFlDialog.setVisibility(View.VISIBLE);
                mWebView.getUrl();
                break;
            case R.id.bt_switch1:
                mLLToolBar1.setVisibility(View.GONE);
                mLLToolBar2.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_switch2:
                mLLToolBar2.setVisibility(View.GONE);
                mLLToolBar1.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_delete:
                mEtInput.setText("");
                //存在情况，当文本框有字时,清除字体，然后消失
                mIvDelete.setVisibility(View.INVISIBLE);
                break;
            case R.id.bt_back:
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                }
                break;
            case R.id.bt_forward:
                if (mWebView.canGoForward()) {
                    mWebView.goForward();
                }
                break;
            case R.id.tv_linkto:
                //Two states to adapt the change.
                switch (SeOrLt) {
                    case 0:
                        URL = mEtInput.getText().toString().trim();
                        if (URL.startsWith("http://") || URL.startsWith("https://")) {
                            mWebView.loadUrl(URL);
                        } else {
                            URL = "http://" + URL;
                            mWebView.loadUrl(URL);
                        }
                        break;
                    case 1:
                        URL = mEtInput.getText().toString().trim();
                        try {
                            URL = URLEncoder.encode(URL, "gb2312");
                            URL = "http://www.baidu.com.cn/s?wd=" + URL + "&cl=3";
                            //通过百度接口来查询关键字
                            mWebView.loadUrl(URL);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        break;
                }


                break;
            case R.id.iv_searchorlinkto:
                //点击此处判断当前状态进行切换
                switch (SeOrLt) {
                    case 0://链接
                        mSearchOrLinkTo.setImageResource(R.drawable.ic_search);
                        mEtInput.setHint("请输入搜索关键字");
                        SeOrLt = 1;
                        break;
                    case 1://搜索
                        mSearchOrLinkTo.setImageResource(R.drawable.ic_web);
                        mEtInput.setHint("请输入网址链接");
                        SeOrLt = 0;
                        break;
                }

        }
    }

    public void WriteToHistorySQLite(WebView view, String url) {
        Date date = new Date();
        long dateTime = date.getTime();
        TableHistoryLocal mTableHistoryLocal = new TableHistoryLocal();
        mTableHistoryLocal.setTitle(view.getTitle());
        mTableHistoryLocal.setURL(url);
        mTableHistoryLocal.setDate(dateTime + "");
        mTableHistoryLocal.save();
        mCallBack.onRefresh(Constant.RES_HISTORY);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = new MenuInflater(getActivity());
        menu.clearHeader();
        inflater.inflate(R.menu.web_tab, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_this:
                //
                break;
            case R.id.menu_new:
                WebView mWebView = new WebView(getActivity());
                WebViewSetter webViewSetter = new WebViewSetter(getContext(), MainFragment.this, mWebView, URL, downloadManager,new HelloWebViewClient(), new HelloWebChromeClient());
                webViewSetter.Setting();
                newTabFlag = true;
                mWebView.loadUrl(URL);
                mWebViewList.add(mWebView);
                LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                mLLWebView.removeAllViews();
                mLLWebView.addView(mWebView, lps);
                Toast.makeText(getActivity(), "已在新标签页打开", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    public class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProFrame.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
            mBtStop.setVisibility(View.VISIBLE);
            mBtForward.setVisibility(View.GONE);
            mEtDigTitle.setText(view.getTitle().toString());
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            Log.i("progress", view.getProgress() + "");
            mProgressBar.setProgress(view.getProgress());
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadUrl("file:///android_asset/error.html");
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.i("onPageFinished","onPageFinished");
            super.onPageFinished(view, url);
            mProFrame.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.GONE);
            mBtStop.setVisibility(View.GONE);
            mBtForward.setVisibility(View.VISIBLE);
            mEtInput.setText(url);
            if (newProgress == 100) {
                setURL(view.getUrl());
                WriteToHistorySQLite(view, url);
                if(newTabFlag){
//                            final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),
//                                    Bitmap.Config.ARGB_8888);
//                            final Canvas c = new Canvas(bitmap);
//                            view.draw(c);
                            BeanWebPage mWebPage = new BeanWebPage(null,view.getTitle(),view.getUrl());
                            mWebPageList.add(mWebPage);
                            mCallBack.OnPassList(mWebPageList,mWebViewList);
                            newTabFlag = false;
                }
                newProgress = 0;
            }
            //如果成功载入则将网页写入数据库中
            //如何重复写同一个网页进入数据库？


        }
    }

    public class HelloWebChromeClient extends WebChromeClient {



        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            WebView mWebView = new WebView(getActivity());
            WebViewSetter webViewSetter = new WebViewSetter(getActivity(), MainFragment.this, mWebView, URL, downloadManager, new HelloWebViewClient(), new HelloWebChromeClient());
            webViewSetter.Setting();
            newTabFlag = true;
            mWebView.loadUrl(view.getUrl());
            mWebViewList.add(mWebView);
            LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            mLLWebView.removeAllViews();
            mLLWebView.addView(mWebView, lps);
            Toast.makeText(getActivity(), "已在新标签页打开", Toast.LENGTH_SHORT).show();
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebView);
            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if(newProgress == 100){
                MainFragment.this.newProgress = newProgress;
                Log.i("newProgress","100");
            }
        }
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
}



