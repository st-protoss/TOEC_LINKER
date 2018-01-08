package com.ck.toec.toec_linker.modules.main.ui;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.base.BaseApp;
import com.ck.toec.toec_linker.common.component.ImageLoader;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.ck.toec.toec_linker.common.event.Event;
import com.ck.toec.toec_linker.common.utils.FileUtil;
import com.ck.toec.toec_linker.common.utils.RxBus;
import com.ck.toec.toec_linker.common.utils.RxUtil;
import com.ck.toec.toec_linker.common.utils.SharedPreferenceUtil;
import com.ck.toec.toec_linker.common.utils.Util;
import com.ck.toec.toec_linker.modules.launch.VideoLoginActivity;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by wm on 2017/12/18.
 */

public class SettingActivity extends BaseActivity{
    @BindView(R.id.nav_toolbar)
    Toolbar mToolbar;
    @BindView(R.id.open_cache)
    AppCompatCheckBox openCache;
    @BindView(R.id.clear_cache)
    LinearLayout clearCache;
    @BindView(R.id.show_cache)
    TextView showCache;
    @BindView(R.id.open_alarm)
    AppCompatCheckBox openAlarm;
    @BindView(R.id.log_out)
    LinearLayout logOut;
    @Override
    protected int layoutId() {
        return R.layout.side_setting;
    }
    public static void launch(Context context){
        context.startActivity(new Intent(context,SettingActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setTitle("详细设置");
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(v->{finish();});

        //根据本地存储展示设置初始值
        showCache.setText("当前缓存："+FileUtil.getAutoFileOrFilesSize(Constant.NET_CACHE));
        openCache.setChecked(SharedPreferenceUtil.getInstance().getBoolean("openCache",true));
        openAlarm.setChecked(SharedPreferenceUtil.getInstance().getBoolean("openAlarm",false));
        openCache.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (openCache.isChecked()){
                    SharedPreferenceUtil.getInstance().putBoolean("openCache",true);
                    Snackbar.make(findViewById(R.id.open_cache), "自动缓存功能已经开启", Snackbar.LENGTH_SHORT).show();
                }else {
                    SharedPreferenceUtil.getInstance().putBoolean("openCache",false);
                }
            }
        });
        openAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(openAlarm.isChecked()){
                    SharedPreferenceUtil.getInstance().putBoolean("openAlarm",true);
                    Snackbar.make(findViewById(R.id.open_alarm), "报警提醒功能已经开启", Snackbar.LENGTH_SHORT).show();
                }else {
                    SharedPreferenceUtil.getInstance().putBoolean("openAlarm",false);
                }
            }
        });

    }
    @OnClick({R.id.log_out,R.id.clear_cache})
    public void onclick(View v){
        switch (v.getId()){
            case R.id.clear_cache:{
                clearCache();
                break;
            }
            case R.id.log_out:{
                //登出
                new AlertDialog.Builder(this).setTitle("提示")
                        .setMessage("您确认要退出此账户么，一旦退出将清除该账户登录信息")
                        .setPositiveButton("确认退出", (dialog, which) -> {
                            //还原常量
                            SharedPreferenceUtil.getInstance().putBoolean("openAlarm",false).putBoolean("openCache",true)
                                    .putString("userName",null).putString("userPass",null);
                            //关闭多余activity 返回登录界面 用RxBus传递事件
                            RxBus.getDefault().post(new Event(1));
                            this.startActivity(new Intent(this, VideoLoginActivity.class));
                        })
                        .setNegativeButton("取消注销",
                                (dialog, which) -> {dialog.dismiss();})
                        .show();
                break;
            }
        }

    }

    private void clearCache(){
        //清除图片缓存
        ImageLoader.clear(BaseApp.getAppContext());
        //清除网络缓存
        Observable.just(new File(Constant.NET_CACHE).delete())
                .subscribeOn(Schedulers.io())
                .subscribe(aBoolean -> {
                    showCache.setText("当前缓存："+FileUtil.getAutoFileOrFilesSize(Constant.NET_CACHE));
                    Snackbar.make(findViewById(R.id.clear_cache), "缓存已清除", Snackbar.LENGTH_SHORT).show();
                });
    }

}
