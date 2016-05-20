package com.example.vincebarry.exam.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.vincebarry.exam.ItemBean.BeanFavorite;
import com.example.vincebarry.exam.ItemBean.BeanHistory;
import com.example.vincebarry.exam.R;
import com.example.vincebarry.exam.adapter.AdapterFavorite;
import com.example.vincebarry.exam.adapter.AdapterHistory;
import com.example.vincebarry.exam.function.Constant;
import com.example.vincebarry.exam.function.MyDialog;
import com.example.vincebarry.exam.table.TableFavoriteLocal;
import com.example.vincebarry.exam.table.TableHistoryLocal;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by VinceBarry on 2016/4/16.
 */
public class HistoryFragment extends Fragment {
    private ListView mLvHistory;
    private List<BeanHistory> UseList;
    private ImageButton mBtClear;
    private int clickPosition;
    private AdapterHistory mAdapterHistory;
    private String deleteItem;
    private OnHistoryToMainListener mCallBack;
    List<TableHistoryLocal> SQLiteList;
    TableHistoryLocal mHistoryLocal;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            //在此处将activity强转成接口
            mCallBack = (OnHistoryToMainListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    public interface OnHistoryToMainListener{
        public void onHistoryToMain(String URL);
    }
    public static HistoryFragment newInstance(){
        HistoryFragment historyFragment = new HistoryFragment();
        return historyFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        mBtClear = (ImageButton) view.findViewById(R.id.bt_clear_his);
        queryAndTrans();
        mHistoryLocal = new TableHistoryLocal();
        mLvHistory = (ListView) view.findViewById(R.id.lv_history);
        mAdapterHistory = new AdapterHistory(getActivity(), UseList);
        mLvHistory.setAdapter(mAdapterHistory);//为Lv配置好Adapter
        mLvHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //普通点击：直接切换至MainFragment页然后显示对应的URL页面,使用intent来切换
                //TODO:maybe wrong.
                String URL;
                URL = UseList.get(position).URL;
                mCallBack.onHistoryToMain(URL);

            }
        });
        mLvHistory.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                clickPosition = position;//点击的Item的位置
                deleteItem = mAdapterHistory.getData().get(clickPosition).getDate();

                MyDialog deleteDialog = new MyDialog(getActivity(),"RTGBrowser","是否删除当前历史记录？", Constant.MYDIALOG_TWOBUTTON);
                deleteDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        DataSupport.deleteAll(TableHistoryLocal.class,"date = ?",deleteItem);
                        queryAndTrans();
                        roadData();
                    }
                    @Override
                    public void OnNegative() {
                        //donothing.
                    }
                });
                return true;
            }
        });
        mBtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("click","clear");
                MyDialog myDialog = new MyDialog(getActivity(),"RTGBrowser","是否删除所有历史记录？",Constant.MYDIALOG_TWOBUTTON);
                myDialog.setOnButtonClick(new MyDialog.onButtonClick() {
                    @Override
                    public void OnPositive() {
                        DataSupport.deleteAll(TableHistoryLocal.class);
                        queryAndTrans();
                        roadData();
                    }

                    @Override
                    public void OnNegative() {
                        //donothing.
                    }
                });

            }
        });
        return view;
    }

    public void queryAndTrans() {
        SQLiteList = DataSupport.findAll(TableHistoryLocal.class);
        //  Log.i("list","SQLiteList"+SQLiteList.size());
        UseList = new ArrayList<>();
        for(int i = SQLiteList.size()-1;i>=0;i--){
            BeanHistory mBean = new BeanHistory(SQLiteList.get(i).getTitle(),SQLiteList.get(i).getURL(), "");
            mBean.setDate(SQLiteList.get(i).getDate());
            UseList.add(mBean);
        }

    }
    public void roadData(){
        mAdapterHistory.getData().clear();
        mAdapterHistory.getData().addAll(UseList);
        mAdapterHistory.notifyDataSetChanged();
    }

}
