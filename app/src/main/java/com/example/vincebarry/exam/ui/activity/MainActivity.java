package com.example.vincebarry.exam.ui.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.vincebarry.exam.ItemBean.BeanWebPage;
import com.example.vincebarry.exam.R;
import br.liveo.interfaces.OnItemClickListener;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import demo.CaptureActivity;
import com.example.vincebarry.exam.broadcast.DownloadReceiver;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.MyDialog;
import com.example.vincebarry.exam.function.SPUtils;
import com.example.vincebarry.exam.function.Sharer;
import com.example.vincebarry.exam.function.URLRegex;
import com.example.vincebarry.exam.table.TableFavoriteLocal;
import com.example.vincebarry.exam.ui.fragment.BrowserSettingFragment;
import com.example.vincebarry.exam.ui.fragment.DownloadFragment;
import com.example.vincebarry.exam.ui.fragment.FavoriteEditFragment;
import com.example.vincebarry.exam.ui.fragment.FavoriteFragment;
import com.example.vincebarry.exam.ui.fragment.HistoryFragment;
import com.example.vincebarry.exam.ui.fragment.MainFragment;
import com.example.vincebarry.exam.ui.fragment.PageTabFragment;
import com.example.vincebarry.exam.ui.fragment.SettingFragment;
import org.litepal.crud.DataSupport;
import java.io.Serializable;
import java.util.List;

/**
 * 这是Web浏览器的MainActivity
 * 主要UI框架的搭建和Fragment通信中心
 */
public class MainActivity extends NavigationLiveo implements OnItemClickListener,SettingFragment.OnSettingToSubSetting,MainFragment.OnMainFragmentListener, HistoryFragment.OnHistoryToMainListener, FavoriteFragment.OnFavoriteToMainListener, FavoriteEditFragment.OnToFavorite {

    private HelpLiveo mHelpLiveo;//Drawer的List

    //Fragment的实例
    private MainFragment mMainFragment;
    private FavoriteFragment mFavoriteFragment;
    private DownloadFragment mDownloadFragment;
    private HistoryFragment mHistoryFragment;
    private SettingFragment mSettingFragment;
    private PageTabFragment mPageTabFragment;
    private FavoriteEditFragment mFavoriteEditFragment;
    private BrowserSettingFragment mBrowserSettingFragment;

    //使用的工具类
    private FragmentTransaction mFragmentTransaction;
    private BroadcastReceiver receiver;
    private DownloadManager downloadManager;
    private FragmentManager fragmentManager;
    private WebView mWebView;

    //变量
    public int fragmentPosition;//用于存储当前fragment的位置，默认值为0（即主fragment），此变量用于实现WebView回退
    private String deleteItem;
    private String updateTitle;
    private String updateTags;
    private List<WebView> mWebViewList;
    private List<BeanWebPage> mWebPageList;
    private String updateContent;
    private String needUpdated;


    @Override
    public void onInt(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            //内存重启调用
            mMainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(mMainFragment.getClass().getName());
            mDownloadFragment = (DownloadFragment) getSupportFragmentManager().findFragmentByTag(mDownloadFragment.getClass().getName());
            mFavoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentByTag(mFavoriteFragment.getClass().getName());
            mHistoryFragment = (HistoryFragment) getSupportFragmentManager().findFragmentByTag(mHistoryFragment.getClass().getName());
            mSettingFragment = (SettingFragment) getSupportFragmentManager().findFragmentByTag(mSettingFragment.getClass().getName());
            getSupportFragmentManager().beginTransaction()
                    .show(mMainFragment)
                    .hide(mDownloadFragment)
                    .hide(mFavoriteFragment)
                    .hide(mHistoryFragment)
                    .hide(mSettingFragment)
                    .commit();
        } else {
            mMainFragment = MainFragment.newInstance();
            mSettingFragment = SettingFragment.newInstance();
            mDownloadFragment = DownloadFragment.newInstance();
            mHistoryFragment = HistoryFragment.newInstance();
            mFavoriteFragment = FavoriteFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, mMainFragment, mMainFragment.getClass().getName())
                    .add(R.id.container, mDownloadFragment, mDownloadFragment.getClass().getName())
                    .add(R.id.container, mFavoriteFragment, mFavoriteFragment.getClass().getName())
                    .add(R.id.container, mSettingFragment, mSettingFragment.getClass().getName())
                    .add(R.id.container, mHistoryFragment, mHistoryFragment.getClass().getName())
                    .show(mMainFragment)
                    .hide(mDownloadFragment)
                    .hide(mFavoriteFragment)
                    .hide(mHistoryFragment)
                    .hide(mSettingFragment)
                    .commit();
        }

        mPageTabFragment = PageTabFragment.newInstance();
        fragmentManager = getSupportFragmentManager();
        mBrowserSettingFragment = BrowserSettingFragment.newInstance();
        receiver = new DownloadReceiver();
        IntentFilter filter = new IntentFilter();
        if (mMainFragment != null) {
            downloadManager = mMainFragment.getDownloadManager();
            filter.addAction(downloadManager.ACTION_NOTIFICATION_CLICKED);
            filter.addAction(downloadManager.ACTION_DOWNLOAD_COMPLETE);
            filter.addAction(Constant.ACTION_CHANGE_WEBVIEW);
            filter.addAction(Constant.ACTION_DOWNLOAD_UPDATE);
            filter.addAction(Constant.ACTION_NEWTAB_URL);
            filter.addAction(Constant.ACTION_CLOSE_PAGETAB);
        }
        registerReceiver(receiver, filter);

        //初始化Drawer
        initDrawer();
    }

    private void initDrawer() {
        // User Information
        this.userName.setText((String)SPUtils.get(getApplicationContext(),"LoginUser","尚未登录"));
        this.userEmail.setText("感谢您使用RTGBrowser");
        this.userPhoto.setImageResource(R.drawable.touxiang);
        this.userBackground.setImageResource(R.drawable.material_red);
        fragmentPosition = 0;
        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.drawer_main), R.drawable.ic_main);
        mHelpLiveo.add(getString(R.string.drawer_favorite), R.drawable.ic_favorite);
        mHelpLiveo.add(getString(R.string.drawer_download), R.drawable.ic_download);
        mHelpLiveo.add(getString(R.string.drawer_history), R.drawable.ic_history);
        mHelpLiveo.addSeparator();
        mHelpLiveo.add(getString(R.string.drawer_setting), R.drawable.ic_setting);
        mHelpLiveo.add(getString(R.string.drawer_about), R.drawable.ic_about);
        mHelpLiveo.addSeparator();
        mHelpLiveo.add(getString(R.string.drawer_quit), R.drawable.ic_quit);
        with(this) // default theme is dark
                .selectorCheck(R.drawable.selector_item_me)
                .colorItemSelected(R.color.icons)
                .colorItemDefault(R.color.colorPrimaryDark)
                .colorItemCounter(R.color.colorPrimaryDark)
                .startingPosition(0) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())
                .setOnClickUser(onClickPhoto)
                .build();
    }


    @Override
    public void onItemClick(int position) {

        fragmentPosition = position;//获取fragment位置
        switch (position) {
            case 0:
                ChangeFragment(0);
                break;
            case 1:
                ChangeFragment(1);
                break;
            case 2:
                ChangeFragment(2);
                break;
            case 3:
                ChangeFragment(3);
                break;
            case 5:
                ChangeFragment(5);
                break;
            case 6:
                MyDialog aboutDialog = new MyDialog(MainActivity.this, "关于", "RoadToGeek浏览器（v1.0）版\n" +
                        "开发者：RoadToGeek\n" +
                        "QQ:1004743668\n" +
                        "Blog:roadtogeek.cn", Constant.MYDIALOG_ONEBUTTON);
                aboutDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        //DoNothing.
                    }

                    @Override
                    public void OnNegative() {
                        //DoNothing.
                    }
                });
                break;
            case 8:
                MyDialog quitDialog = new MyDialog(MainActivity.this, "RTGBrowser", "是否退出浏览器？", Constant.MYDIALOG_TWOBUTTON);
                quitDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        finish();
                    }

                    @Override
                    public void OnNegative() {
                        //DoNothing.
                    }
                });
                break;
        }

    }

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this,CloudActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //drawer
        if (keyCode == KeyEvent.KEYCODE_BACK && MainActivity.this.isDrawerOpen()) {
            MainActivity.this.closeDrawer();
            return false;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && fragmentPosition != 0) {
            ChangeFragment(0);
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && !mWebView.canGoBack() && fragmentPosition == 0) {
            MyDialog myDialog = new MyDialog(MainActivity.this, "RTGBrowser", "是否退出浏览器？", Constant.MYDIALOG_TWOBUTTON);
            myDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                @Override
                public void OnPositive() {
                    finish();
                }

                @Override
                public void OnNegative() {
                    //donothing.
                }
            });
            return false;
        }
        return false;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void ChangeFragment(int position) {
        switch (position) {
            case Constant.CHANGEFRAGMENT_MAIN:
                fragmentManager.beginTransaction()
                        .show(mMainFragment)
                        .hide(mHistoryFragment)
                        .hide(mFavoriteFragment)
                        .hide(mSettingFragment)
                        .hide(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_MAIN;
                this.setCheckedItemNavigation(fragmentPosition, true);
                break;

            case Constant.CHANGEFRAGMENT_FAVORITE:
                fragmentManager.beginTransaction()
                        .hide(mMainFragment)
                        .hide(mHistoryFragment)
                        .show(mFavoriteFragment)
                        .hide(mSettingFragment)
                        .hide(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_FAVORITE;
                this.setCheckedItemNavigation(fragmentPosition, true);
                break;
            case Constant.CHANGEFRAGMENT_DOWNLOAD:
                fragmentManager.beginTransaction()
                        .hide(mMainFragment)
                        .hide(mHistoryFragment)
                        .hide(mFavoriteFragment)
                        .hide(mSettingFragment)
                        .show(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_DOWNLOAD;
                this.setCheckedItemNavigation(fragmentPosition, true);
                break;
            case Constant.CHANGEFRAGMENT_HISTORY:
                fragmentManager.beginTransaction()
                        .hide(mMainFragment)
                        .show(mHistoryFragment)
                        .hide(mFavoriteFragment)
                        .hide(mSettingFragment)
                        .hide(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_HISTORY;
                this.setCheckedItemNavigation(fragmentPosition, true);
                break;
            case Constant.CHANGEFRAGMENT_SETTING:
                fragmentManager.beginTransaction()
                        .hide(mMainFragment)
                        .hide(mHistoryFragment)
                        .hide(mFavoriteFragment)
                        .show(mSettingFragment)//
                        .hide(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_SETTING;
                this.setCheckedItemNavigation(fragmentPosition, true);
                break;
            case Constant.CHANGEFRAGMENT_PAGETAB:
                fragmentManager.beginTransaction()
                        .add(R.id.container, mPageTabFragment, mPageTabFragment.getClass().getName())
                        .show(mPageTabFragment)
                        .hide(mMainFragment)
                        .hide(mHistoryFragment)
                        .hide(mFavoriteFragment)
                        .hide(mSettingFragment)
                        .hide(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_PAGETAB;
                this.setCheckedItemNavigation(fragmentPosition, true);
                break;
            case Constant.CHANGEFRAGMENT_ALLHIDE:
                fragmentManager.beginTransaction()
                        .hide(mMainFragment)
                        .hide(mHistoryFragment)
                        .hide(mFavoriteFragment)
                        .hide(mSettingFragment)
                        .hide(mDownloadFragment)
                        .commit();
                fragmentPosition = Constant.CHANGEFRAGMENT_ALLHIDE;
                break;
        }
    }

    @Override
    public void onStartQRScanner() {
        //启动扫码器（Activity）
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, 0);//获取结果
    }

    @Override
    public void onShared(String str) {
        //TODO:完成分享功能逻辑的实现
        Sharer sharer = new Sharer(MainActivity.this, str);
        sharer.showShare();
    }


    //下面是Fragment之间回调实现
    @Override
    public void onRefresh(int res) {

        switch (res) {
            case Constant.RES_HISTORY:
                if (mHistoryFragment != null) {
                    mHistoryFragment.queryAndTrans();
                    mHistoryFragment.roadData();
                }

                break;
            case Constant.RES_FAVORITE:
                if (mFavoriteFragment != null) {
                    mFavoriteFragment.queryAndTrans();
                    mFavoriteFragment.roadData();
                }
                break;

        }
    }

    public void RefreshDownload() {
        if (mDownloadFragment != null) {
            mDownloadFragment.roadData();
        }
    }

    @Override
    public void OnChangeToPage(List<BeanWebPage> mWebPageList, List<WebView> mWebViewList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("List", (Serializable) mWebPageList);
        bundle.putSerializable("webview", (Serializable) mWebViewList);
        mPageTabFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        mFragmentTransaction.add(R.id.container, mPageTabFragment, mPageTabFragment.getClass().getName()).commit();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //获取扫码器的结果做出回应（将值传给MainFragment）
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //在此处处理对扫码器回应的结果
            String content = data.getStringExtra(CaptureActivity.EXTRA_RESULT);
            URLRegex urlRegex = new URLRegex(content);
            if (urlRegex.isURL()) {
                mWebView.loadUrl(content);
            } else {
                mWebView.loadData(content, "text/html", "UTF-8");
            }
        } else {
            //失败时回应的结果
        }
    }

    @Override
    public void onHistoryToMain(String URL) {
        ChangeFragment(0);
        if(mWebView == null){
            mWebView = (WebView) mMainFragment.getView().findViewById(R.id.wv_main);
        }
        mWebView.loadUrl(URL);
        Log.i("position", "checked:" + this.getCurrentCheckPosition() + "");
        Log.i("position", "NoChecked:" + this.getCurrentPosition() + "");

    }

    @Override
    public void OnFavoriteToMain(String URL) {
        if(mWebView == null){
            mWebView = (WebView) mMainFragment.getView().findViewById(R.id.wv_main);
        }
        ChangeFragment(0);
        mWebView.loadUrl(URL);
    }

    //TODO:OnFavToEdit
    @Override
    public void OnFavToEdit(String url, String title, String content, String date, String tags) {
        Bundle bundle = new Bundle();
        bundle.putString("Url", url);
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("tags", tags);
        bundle.putString("date", date);
        mFavoriteEditFragment = FavoriteEditFragment.newInstance();
        mFavoriteEditFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.container, mFavoriteEditFragment, mFavoriteEditFragment.getClass().getName()).commit();
        ChangeFragment(7);
    }


    @Override
    public void OnEditToFavorite(int res, String args, String... args2) {
        deleteItem = args;
        needUpdated = args2[0];
        updateTitle = args2[1];
        updateContent = args2[2];
        updateTags = args2[3];
        switch (res) {
            case Constant.EDITTOFAV_OK:
                //TODO:OK?
                if (needUpdated == "true") {
                    MyDialog editDialog = new MyDialog(MainActivity.this, "收藏夹", "是否保存当前更改？", Constant.MYDIALOG_TWOBUTTON);
                    editDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                        @Override
                        public void OnPositive() {
                            //更改
                            ContentValues values = new ContentValues();
                            values.put("title", updateTitle);
                            values.put("note", updateContent);
                            values.put("tags", updateTags);
                            DataSupport.updateAll(TableFavoriteLocal.class, values, "date = ?", deleteItem);
                            if (mFavoriteFragment != null) {
                                mFavoriteFragment.queryAndTrans();
                                mFavoriteFragment.roadData();
                            }
                        }

                        @Override
                        public void OnNegative() {

                        }
                    });
                }
                break;
            case Constant.EDITTOFAV_BACK:

                break;
            case Constant.EDITTOFAV_DELETE:
                MyDialog deleteDialog = new MyDialog(MainActivity.this, "收藏夹", "是否删除当前书签？", Constant.MYDIALOG_TWOBUTTON);
                deleteDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        DataSupport.deleteAll(TableFavoriteLocal.class, "date = ?", deleteItem);
                        if (mFavoriteFragment != null) {
                            mFavoriteFragment.queryAndTrans();
                            mFavoriteFragment.roadData();
                        }
                    }

                    @Override
                    public void OnNegative() {

                    }
                });
                break;
        }
        fragmentManager.beginTransaction().addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .remove(mFavoriteEditFragment).commit();

        ChangeFragment(1);
    }

    public void OnChangeWebView(int position) {
        if (mMainFragment != null) {
            fragmentManager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).remove(mPageTabFragment).commit();
            mWebView = mWebViewList.get(position);
            String url = mWebView.getUrl();
            ChangeFragment(0);
            LinearLayout mLLWebView = (LinearLayout) mMainFragment.getView().findViewById(R.id.ll_webview);
            mWebView.loadUrl(mWebView.getUrl());
            LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            mLLWebView.removeAllViews();
            mLLWebView.addView(mWebView, lps);
        }
    }

    @Override
    public void OnPassList(List<BeanWebPage> mWebPageList, List<WebView> mWebViewList) {
        this.mWebViewList = mWebViewList;
        this.mWebPageList = mWebPageList;
        if (mWebView == null) {
            mWebView = mWebViewList.get(mWebViewList.size()-1);
        }
    }

    public void passURLToMain(String URL) {
        mMainFragment.setURL(URL);
    }

    @Override
    public void OnSettingToBrowser() {
        fragmentManager = getSupportFragmentManager();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.container, mBrowserSettingFragment, mBrowserSettingFragment.getClass().getName()).commit();
    }

    public void closePageTab() {
        fragmentManager = getSupportFragmentManager();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        mFragmentTransaction.remove(mPageTabFragment).commit();
    }
}

