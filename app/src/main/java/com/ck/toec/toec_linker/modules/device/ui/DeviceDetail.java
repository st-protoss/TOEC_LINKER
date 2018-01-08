package com.ck.toec.toec_linker.modules.device.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.common.component.ImageLoader;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.device.adapter.DetailRefresh;
import com.ck.toec.toec_linker.modules.device.adapter.DeviceDetailAdapter;
import com.ck.toec.toec_linker.modules.device.entity.AlarmData;
import com.ck.toec.toec_linker.modules.device.entity.DetailPage;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.device.entity.HisRealdata;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function4;

/**
 * Created by wm on 2017/11/30.
 */

public class DeviceDetail extends Activity {
    @BindView(R.id.device_rt_image)
    ImageView device_rt_image;
    @BindView(R.id.rt_name)
    TextView rt_name;
    @BindView(R.id.trans_head)
    RelativeLayout trans_head;
    @BindView(R.id.recyclerview)
    RecyclerView rv;
    @BindView(R.id.swiprefresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.detail_exit)
    ImageView detail_exit;
    @BindView(R.id.device_error)
    ImageView mDeviceListError;

    DeviceDetailAdapter dAdapter;
    String transName;

    String deviceID;
    String RtID;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Slide slide = new Slide(Gravity.BOTTOM);
        slide.addTarget(R.id.rt_name);
        slide.addTarget(R.id.device_rt_image);
        getWindow().setEnterTransition(slide);
        setContentView(R.layout.device_detail);
        ButterKnife.bind(this);
        initData();
        ViewCompat.setTransitionName(trans_head, transName);
        initView();
    }

    private void initView() {
        if (mRefreshLayout != null) {
            mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
            mRefreshLayout.setDistanceToTriggerSync(200);
            mRefreshLayout.setOnRefreshListener(
                    () -> mRefreshLayout.postDelayed(this::initAllDatas, 1000));
        }
        detail_exit.setOnClickListener(v->{finish();});

        dAdapter = new DeviceDetailAdapter();
        initAllDatas();
        rv.setLayoutManager(new LinearLayoutManager(this));
        dAdapter.setDetailRefresh(new DetailRefresh() {
            @Override
            public void rtRefresh() {
                RetrofitUtil.getInstance()
                        .GetRtData(deviceID,RtID)
                        .subscribe(new Consumer<DeviceRtRule>() {
                            @Override
                            public void accept(@NonNull DeviceRtRule rtRule) throws Exception {
                                dAdapter.setRtData(rtRule);
                                dAdapter.notifyItemChanged(0);
                            }
                        });
            }

            @Override
            public void lineRefresh(String refreshType) {
                /*                switch (refreshType){
                    case 1:
                        //按日查看数据 选项文字颜色变色
                        break;
                    case 2:
                        //按周查看数据 选项文字颜色变色
                        break;
                    case 3:
                        //按月查看数据 选项文字颜色变色
                        break;
                }*/
                RetrofitUtil.getInstance( )
                        .GetTrendList(RtID,refreshType)
                        .subscribe(new Consumer<List<HisRealdata>>() {
                            @Override
                            public void accept(@NonNull List<HisRealdata> histories) throws Exception {
                                dAdapter.setRtHistories(histories);
                                dAdapter.notifyItemChanged(1);
                            }
                        });


            }

            @Override
            public void chartRefresh(int expectPage) {
                RetrofitUtil.getInstance()
                        .GetHistoryList(RtID,deviceID,expectPage)
                        .subscribe(new Consumer<DetailPage<RtHistory>>() {
                            @Override
                            public void accept(@NonNull DetailPage<RtHistory> rtHistoryDetailPage) throws Exception {
                                if(rtHistoryDetailPage!=null){
                                    dAdapter.setHisPage(rtHistoryDetailPage);
                                    dAdapter.notifyItemChanged(2);
                                }

                            }
                        });
            }

            @Override
            public void alarmRefresh(int expectPage) {
                RetrofitUtil.getInstance()
                        .GetAlarmList(RtID,expectPage)
                        .subscribe(new Consumer<DetailPage<AlarmData>>() {
                            @Override
                            public void accept(@NonNull DetailPage<AlarmData> alarmDataDetailPage) throws Exception {
                                if (alarmDataDetailPage!=null){
                                    dAdapter.setAlarmPage(alarmDataDetailPage);
                                    dAdapter.notifyItemChanged(3);
                                }

                            }
                        });

            }
        });
        rv.setAdapter(dAdapter);
    }

    //初始化标题栏数据
    private void initData() {
        Intent intent = getIntent();
        deviceID = intent.getStringExtra("deviceID");
        RtID= intent.getStringExtra("RtID");
        transName = intent.getStringExtra("trans_name");

        RetrofitUtil.getInstance()
                .GetRtData(deviceID,RtID)
                .subscribe(new Observer<DeviceRtRule>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DeviceRtRule deviceRtRule) {
                        rt_name.setText(deviceRtRule.getRtName());
                        ImageLoader.loadNetImage(DeviceDetail.this, Constant.ServerAddr+deviceRtRule.getRtImage(),device_rt_image);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.showShort("请求失败");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    private void initAllDatas(){
        Observable a = RetrofitUtil.getInstance().GetAlarmList(RtID,1);
        Observable b = RetrofitUtil.getInstance().GetHistoryList(RtID,deviceID,1);
        Observable c = RetrofitUtil.getInstance( ).GetTrendList(RtID,"1");
        Observable d = RetrofitUtil.getInstance().GetRtData(deviceID,RtID);
        Observable.zip(a,b,c,d,new Function4<DetailPage<AlarmData>,DetailPage<RtHistory>
                ,List<HisRealdata>,DeviceRtRule,String>(){
            @Override
            public String apply(@NonNull DetailPage<AlarmData> alarmDataDetailPage,
                                @NonNull DetailPage<RtHistory> rtHistoryDetailPage,
                                @NonNull List<HisRealdata> hisRealdatas,
                                @NonNull DeviceRtRule rtRule) throws Exception {
                dAdapter.setRtData(rtRule);
                dAdapter.setRtHistories(hisRealdatas);
                dAdapter.setHisPage(rtHistoryDetailPage);
                dAdapter.setAlarmPage(alarmDataDetailPage);
                return "success";
            }
        }).subscribe(new Observer() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onNext(@NonNull Object o) {
                mRefreshLayout.setRefreshing(false);
                rv.setVisibility(View.VISIBLE);
                mDeviceListError.setVisibility(View.GONE);
                for (int i=0;i<4;i++){
                    dAdapter.notifyItemChanged(i);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mRefreshLayout.setRefreshing(false);
                mDeviceListError.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
            }
            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
