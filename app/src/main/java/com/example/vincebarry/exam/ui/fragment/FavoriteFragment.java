package com.example.vincebarry.exam.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.vincebarry.exam.ItemBean.BeanHistory;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.MyDialog;
import com.example.vincebarry.exam.table.TableFavoriteLocal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import com.example.vincebarry.exam.ItemBean.BeanFavorite;
import com.example.vincebarry.exam.adapter.AdapterFavorite;
import com.example.vincebarry.exam.table.TableHistoryLocal;


/**
 * Created by VinceBarry on 2016/4/16.
 */
public class FavoriteFragment extends Fragment{
    private ListView mLvFavorite;
    private List<BeanFavorite> UseList;
    private List<TableFavoriteLocal> SQLiteList;
    private ImageButton mBtEdit;
    private ImageButton mBtDelete;
    private AdapterFavorite mAdapterFavorite;
    private AppCompatCheckBox mCbItem;
    private OnFavoriteToMainListener mCallBack;


    public interface OnFavoriteToMainListener{
        public void OnFavoriteToMain(String URL);
        public void OnFavToEdit(String url,String title,String content,String date,String tags);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //在此处将activity强转成接口
            mCallBack = (OnFavoriteToMainListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    public static FavoriteFragment newInstance(){
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        return favoriteFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        mBtDelete = (ImageButton) view.findViewById(R.id.bt_fav_delete);
        mLvFavorite = (ListView) view.findViewById(R.id.lv_favorite);
        mBtEdit = (ImageButton) view.findViewById(R.id.bt_fav_edit);
        queryAndTrans();
        mAdapterFavorite = new AdapterFavorite(getActivity(), UseList);
        mCbItem = (AppCompatCheckBox) mLvFavorite.findViewById(R.id.cb_fav);
        mLvFavorite.setAdapter(mAdapterFavorite);//为Lv配置好Adapter
        mLvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //普通点击：直接切换至MainFragment页然后显示对应的URL页面,使用intent来切换
                //TODO:maybe wrong.
                String URL;
                URL = UseList.get(position).URL;
                mCallBack.OnFavoriteToMain(URL);
            }
        });
        mLvFavorite.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO:There should be edited.
                //跳转到FavoriteEditFragment
                String Url = UseList.get(position).getURL();
                String title =UseList.get(position).getTitle();
                String content = UseList.get(position).getNote();
                String date = UseList.get(position).getDate();
                String tags = UseList.get(position).getTags();
                mCallBack.OnFavToEdit(Url,title,content,date,tags);
                return true;
            }
        });
        mBtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog deleteDialog = new MyDialog(getActivity(),"收藏夹","是否删除当前所有书签？", Constant.MYDIALOG_TWOBUTTON);
                deleteDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        DataSupport.deleteAll(TableFavoriteLocal.class);
                        queryAndTrans();
                        roadData();
                    }

                    @Override
                    public void OnNegative() {

                    }
                });

            }
        });
        mBtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCbItem != null){
                    mCbItem.setVisibility(View.VISIBLE);
                }else{
                    Log.i("mcb","null");
                }
            }
        });
        return view;
    }
    public void queryAndTrans() {
        SQLiteList = DataSupport.findAll(TableFavoriteLocal.class);
        UseList = new ArrayList<>();
        for(int i = SQLiteList.size()-1;i>=0;i--){
            BeanFavorite mBean = new BeanFavorite(SQLiteList.get(i).getURL(), SQLiteList.get(i).getTitle(), SQLiteList.get(i).getTags(), SQLiteList.get(i).getNote(), "");
            mBean.setDate(SQLiteList.get(i).getDate());
            UseList.add(mBean);
        }

    }
    public void roadData(){
        mAdapterFavorite.getData().clear();
        mAdapterFavorite.getData().addAll(UseList);
        mAdapterFavorite.notifyDataSetChanged();
    }
}
