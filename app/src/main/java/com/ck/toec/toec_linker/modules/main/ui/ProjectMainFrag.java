package com.ck.toec.toec_linker.modules.main.ui;


import android.Manifest;
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
import com.ck.toec.toec_linker.base.BaseApp;
import com.ck.toec.toec_linker.base.BaseFragment;
import com.ck.toec.toec_linker.base.onItemClickListener;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.utils.RxUtil;
import com.ck.toec.toec_linker.common.utils.SharedPreferenceUtil;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.common.utils.VersionUtil;
import com.ck.toec.toec_linker.modules.device.entity.DetailPage;
import com.ck.toec.toec_linker.modules.device.ui.DeviceMainActivity;
import com.ck.toec.toec_linker.modules.main.adapter.DeviceListAdapter;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by wm on 2017/11/13.
 */

public class ProjectMainFrag extends BaseFragment{
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.device_error)
    ImageView mDeviceListError;

    private String userId;

    private View view;
    //声明实体类和其对应的适配器
    private static List<ToecDevice> mDeviceList;
    private DeviceListAdapter mAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       if(view==null){
           view = inflater.inflate(R.layout.project_main_frag,container,false);
           ButterKnife.bind(this,view);
       }
        mIsCreateView = true;
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadDeviceList();
        initView();
/*        new RxPermissions(getActivity())
                .request(Manifest.permission.ACCESS_COARSE_LOCATION)
                .doOnNext(o -> mRefreshLayout.setRefreshing(true))
                .doOnNext(granted -> {
                    //loadDeviceList();
                    //VersionUtil.checkVersion(getActivity());
                })
                .subscribe();*/

    }
    private void initView() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light);
            mRefreshLayout.setDistanceToTriggerSync(50);
            mRefreshLayout.setOnRefreshListener(
                    () -> mRefreshLayout.postDelayed(this::loadDeviceList, 1000));
        }
        mDeviceList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DeviceListAdapter(mDeviceList);
        //给点击recyclerview的每一项添加点击跳转事件
        mAdapter.setOnItemClickListener(new onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mDeviceList.get(position)!=null){
                    Intent intent = new Intent();
                    intent.putExtra("deviceName",mDeviceList.get(position).getDeviceName());
                    intent.putExtra("deviceId",mDeviceList.get(position).getDeviceID());
                    intent.setClass(getContext(), DeviceMainActivity.class);
                    startActivity(intent);
                }

            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 加载设备列表
     */
    private void loadDeviceList(){
        //使用retrofit向服务器进行网络请求
        RetrofitUtil.getInstance()
                //username和usertoken应该从sharedpreference中取得
                .getDeviceList(userId)
                .compose(RxUtil.fragmentLifecycle(this))
                .doOnSubscribe(a->mRefreshLayout.setRefreshing(true))
                .subscribe(new Observer<DetailPage<ToecDevice>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //mProgressBar.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onNext(@NonNull DetailPage<ToecDevice> toecDevices) {
                        //错误页面隐藏
                        mDeviceListError.setVisibility(View.GONE);
                        //正常列表显示
                        mRecyclerView.setVisibility(View.VISIBLE);
                        //将服务器返回的list赋值给内部变量
                        mDeviceList.clear();
                        if(toecDevices.getList()!=null)
                        mDeviceList.addAll(toecDevices.getList());
                        //测试地图用
                     /*   Gson a = new Gson();
                        String b = a.toJson(toecDevices.getList());*/
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
                        Util.showShort("获取设备列表失败");
                    }

                    @Override
                    public void onComplete() {
                        mRefreshLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                        Util.showShort("加载完毕");
                    }
                });
    }
    @Override
    protected void lazyLoad() {
        //zzz
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
}

