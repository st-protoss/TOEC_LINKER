package com.ck.toec.toec_linker.modules.device.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseFragment;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.utils.RxUtil;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.device.adapter.DeviceWtdatalistAdapter;
import com.ck.toec.toec_linker.modules.device.entity.DeviceWtRule;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by wm on 2017/11/20.
 */

public class DeviceWtdataFrag extends BaseFragment {
    @BindView(R.id.recyclerview2)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiprefresh2)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.device_error)
    ImageView mDeviceListError;

    View view;
    String deviceID;
    private static List<DeviceWtRule> mDeviceWtList;
    private DeviceWtdatalistAdapter mAdapter;

    public DeviceWtdataFrag(String deviceID){
        this.deviceID = deviceID;
    }
    @Override
    protected void lazyLoad() {
        //zzz
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.device_wtdatalist_frag,container,false);
            ButterKnife.bind(this,view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadWtdataList();
        initView();
    }
    /**
     * 加载开关属性列表
     */
    private void loadWtdataList(){
        //String deviceID = "139f683d26e5454db3e5994d2a4b79d4";
        RetrofitUtil.getInstance()
                .getWtdataList(deviceID)
                .compose(RxUtil.fragmentLifecycle(this))
                .doOnSubscribe(a->mRefreshLayout.setRefreshing(true))
                .subscribe(new Observer<List<DeviceWtRule>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //mProgressBar.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onNext(@NonNull List<DeviceWtRule> deviceWtRules) {
                        //错误页面隐藏
                        mDeviceListError.setVisibility(View.GONE);
                        //正常列表显示
                        mRecyclerView.setVisibility(View.VISIBLE);
                        //将服务器返回的list赋值给内部变量
                        mDeviceWtList.clear();
                        mDeviceWtList.addAll(deviceWtRules);
                        //更新UI
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        mRefreshLayout.setRefreshing(false);
                        //错误页面显示
                        mDeviceListError.setVisibility(View.VISIBLE);
                        //正常列表隐藏
                        mRecyclerView.setVisibility(View.GONE);
                        Util.showShort("获取开关列表失败");
                    }

                    @Override
                    public void onComplete() {
                        mRefreshLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                        Util.showShort("加载完毕");
                    }
                });
    }
    /**
     *初始化
     */
    private void initView(){
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
            mRefreshLayout.setDistanceToTriggerSync(50);
            mRefreshLayout.setOnRefreshListener(
                    () -> mRefreshLayout.postDelayed(this::loadWtdataList, 1000));
        }
        mDeviceWtList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DeviceWtdatalistAdapter(mDeviceWtList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
