package com.ck.toec.toec_linker.modules.device.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewCompat;
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
import com.ck.toec.toec_linker.base.onItemClickListener;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.utils.RxUtil;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.device.adapter.DeviceRtdatalistAdapter;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.main.adapter.DeviceListAdapter;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by wm on 2017/11/17.
 */

public class DeviceRtdataFrag extends BaseFragment {
    @BindView(R.id.recyclerview1)
    RecyclerView mRecyclerView;
    @BindView(R.id.swiprefresh1)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.device_error)
    ImageView mDeviceListError;

    View view;
    String deviceID;

    //声明实体类和其对应的适配器
    private static List<DeviceRtRule> mDeviceRtList;
    private DeviceRtdatalistAdapter mAdapter;

    public DeviceRtdataFrag (String deviceID){
        this.deviceID = deviceID;
    }
    @Override
    protected void lazyLoad() {
        //zzzz
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.device_rtdatalist_frag,container,false);
            ButterKnife.bind(this,view);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadRtdataList();
        initView();
    }

    /**
     * 加载该设备实时数据列表
     */
    private void loadRtdataList() {
        //String deviceID = "139f683d26e5454db3e5994d2a4b79d4";
        RetrofitUtil.getInstance()
                .getRtdataList(deviceID)
                .compose(RxUtil.fragmentLifecycle(this))
                .doOnSubscribe(a->mRefreshLayout.setRefreshing(true))
                .subscribe(new Observer<List<DeviceRtRule>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        //mProgressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(@NonNull List<DeviceRtRule> deviceRtRules) {
                        //错误页面隐藏
                        mDeviceListError.setVisibility(View.GONE);
                        //正常列表显示
                        mRecyclerView.setVisibility(View.VISIBLE);
                        //将服务器返回的list赋值给内部变量
                        mDeviceRtList.clear();
                        mDeviceRtList.addAll(deviceRtRules);
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
                        Util.showShort("获取实时数据列表失败");
                    }

                    @Override
                    public void onComplete() {
                        mRefreshLayout.setRefreshing(false);
                        mProgressBar.setVisibility(View.GONE);
                        Util.showShort("加载完毕");
                    }
                });
    }

    private void initView() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
            mRefreshLayout.setDistanceToTriggerSync(50);
            mRefreshLayout.setOnRefreshListener(
                    () -> mRefreshLayout.postDelayed(this::loadRtdataList, 1000));
        }
        mDeviceRtList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new DeviceRtdatalistAdapter(mDeviceRtList);
        mAdapter.setOnViewClickedListener(new onItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(),DeviceDetail.class);
                intent.putExtra("RtID",mDeviceRtList.get(position).getRtID());
                intent.putExtra("deviceID",deviceID);
                ViewCompat.setTransitionName(view,String.valueOf(position)+"_image");
                intent.putExtra("trans_name",String.valueOf(position)+"_image");
                Pair a = new Pair<>(view, ViewCompat.getTransitionName(view));
                ActivityOptionsCompat transitionActivityOptions =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                                getActivity(), a);
                ActivityCompat.startActivity(
                        getContext(), intent, transitionActivityOptions.toBundle());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }
}
