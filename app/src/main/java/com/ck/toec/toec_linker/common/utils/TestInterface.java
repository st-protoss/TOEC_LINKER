package com.ck.toec.toec_linker.common.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.base.BaseActivity;
import com.ck.toec.toec_linker.base.BaseApp;
import com.ck.toec.toec_linker.common.component.Log;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.modules.device.entity.AlarmData;
import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.device.entity.DeviceWtRule;
import com.ck.toec.toec_linker.modules.device.entity.RtHistory;
import com.ck.toec.toec_linker.modules.launch.entity.Login;
import com.ck.toec.toec_linker.modules.main.entity.ToecDevice;
import com.ck.toec.toec_linker.modules.main.ui.ProjectMainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by wm on 2017/11/23.
 * 接口测试类
 */

public class TestInterface extends BaseActivity {
    @BindView(R.id.test_glide)
    ImageView test_glide;
    @BindView(R.id.web_test)
    WebView webView;
@OnClick({R.id.test_login,R.id.test_devicelist,R.id.test_rtlist,R.id.test_wtlist
         ,R.id.test_wtdata,R.id.test_rtdata,R.id.test_historydata,R.id.test_alarmlist,R.id.test_rtalarm})
public void OnClick(View v){
switch (v.getId()){
    //启动
    case R.id.test_login:{
        RetrofitUtil.getInstance().loginCheck("admin","1")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Login>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Login login) {
                    String a = login.getId();
                        Util.showShort("用户ID为"+a);
                        Log.e("用户ID为"+a);
                         if(login.isAllowed()){
                             Util.showShort("登录成功");
                         }else{
                             Util.showShort("登录失败");
                         }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.showShort("请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }break;
    //设备列表
    case R.id.test_devicelist:{
        RetrofitUtil.getInstance().getDeviceList("1")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }break;
    //实时列表
    case R.id.test_rtlist:{
        RetrofitUtil.getInstance().getRtdataList("139f683d26e5454db3e5994d2a4b79d4")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DeviceRtRule>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<DeviceRtRule> deviceRtRules) {
                        AlertDialog a = new AlertDialog.Builder(BaseApp.getAppContext())
                                .setMessage(deviceRtRules.toString()).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.showShort("请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }break;
    //开关列表
    case R.id.test_wtlist:{
        RetrofitUtil.getInstance().getWtdataList("139f683d26e5454db3e5994d2a4b79d4")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<DeviceWtRule>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<DeviceWtRule> deviceWtRules) {
                        AlertDialog a = new AlertDialog.Builder(BaseApp.getAppContext())
                                .setMessage(deviceWtRules.toString()).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.showShort("请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }break;
    //写入数据
    case R.id.test_wtdata:{
        RetrofitUtil.getInstance().writeCommand("69447fce4e294f8986ed11cf41979c1a",1)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        if (s.equalsIgnoreCase("1")){
                            Util.showShort("指令下发成功");
                        }else if(s.equalsIgnoreCase("0")){
                            Util.showShort("指令下发失败");
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Util.showShort("请求失败");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }break;
    //实时数据
    case R.id.test_rtdata:{
        RetrofitUtil.getInstance().GetRtData("169798c3e3b542a1b3d9f357f2ed8c7e","")
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeviceRtRule>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull DeviceRtRule deviceRtRule) {
                        AlertDialog a = new AlertDialog.Builder(BaseApp.getAppContext())
                                .setMessage(deviceRtRule.toString()).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }break;
    //历史列表
    case R.id.test_historydata:{
        RetrofitUtil.getInstance().GetHistoryList("169798c3e3b542a1b3d9f357f2ed8c7e",
                "139f683d26e5454db3e5994d2a4b79d4",1)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }break;
    //警报列表
    case R.id.test_alarmlist:{
        RetrofitUtil.getInstance().GetAlarmList("169798c3e3b542a1b3d9f357f2ed8c7e",1)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }break;
    //实时警报
    case R.id.test_rtalarm:{
//        Glide.with(this).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1511951214113&di=fffaa325539737ca2b48a4fcd426fba8&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F17%2F07%2F15%2F23H58PICJcE_1024.jpg")
//                .into(test_glide);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl("file:///android_asset/echarts.html");
    }break;
}
}
    @Override
    protected int layoutId() {
        return R.layout.test_interface;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public static void launch(Context context){
        context.startActivity(new Intent(context,TestInterface.class));
    }
}
