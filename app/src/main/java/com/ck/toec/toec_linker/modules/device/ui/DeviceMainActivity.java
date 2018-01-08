package com.ck.toec.toec_linker.modules.device.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.modules.device.adapter.DeviceFragAdapter;

import butterknife.BindView;

/**
 * Created by wm on 2017/11/17.
 */

public class DeviceMainActivity extends BaseActivity {
    @BindView(R.id.tablayout1)
    TabLayout mTablayout;
    @BindView(R.id.toolbar1)
    Toolbar mToolbar;
    @BindView(R.id.viewPager1)
    ViewPager mViewPager;

    DeviceWtdataFrag WtdataFrag;
    DeviceRtdataFrag RtdataFrag;
    @Override
    protected int layoutId() {
        return R.layout.device_main;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        String deviceID = intent.getStringExtra("deviceId");
        String deviceName = intent.getStringExtra("deviceName");
        mToolbar.setTitle(deviceName);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v->{finish();});
        DeviceFragAdapter deviceFragAdapter = new DeviceFragAdapter(getSupportFragmentManager());
        WtdataFrag = new DeviceWtdataFrag(deviceID);
        RtdataFrag = new DeviceRtdataFrag(deviceID);
        deviceFragAdapter.addTab(RtdataFrag,"实时数据");
        deviceFragAdapter.addTab(WtdataFrag,"开关列表");
        mViewPager.setAdapter(deviceFragAdapter);
        mTablayout.setupWithViewPager(mViewPager,false);
    }
}
