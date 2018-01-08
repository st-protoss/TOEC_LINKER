package com.ck.toec.toec_linker.base;

import android.app.Application;
import android.content.Context;

import com.baidu.mapapi.SDKInitializer;
import com.ck.toec.toec_linker.common.component.CrashHandler;
import com.ck.toec.toec_linker.common.component.Log;
import com.ck.toec.toec_linker.common.constant.Constant;
import com.facebook.stetho.Stetho;
import com.github.moduth.blockcanary.BlockCanary;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by wm on 2017/11/10.
 */

public class BaseApp extends Application{
    private static String sCacheDir;
    private static Context sAppContext;
    public static String username = null;
    public static String usertoken = null;
    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = getApplicationContext();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        CrashHandler.init(new CrashHandler(getApplicationContext()));
        if (!Constant.DEBUG) {
            //bughd sdk
            // FIR.init(this);
        } else {
            //watcher
            // Watcher.getInstance().start(this);
            Stetho.initializeWithDefaults(this);
        }
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        LeakCanary.install(this);
        RxJavaPlugins.setErrorHandler(throwable -> {
            if (throwable != null) {
                Log.e(throwable.toString());
            } else {
                Log.e("call onError but exception is null");
            }
        });
        /*
         * 如果存在SD卡则将缓存写入SD卡,否则写入手机内存
         */
        if (getApplicationContext().getExternalCacheDir() != null && ExistSDCard()) {
            sCacheDir = getApplicationContext().getExternalCacheDir().toString();
        } else {
            sCacheDir = getApplicationContext().getCacheDir().toString();
        }
    }

    private boolean ExistSDCard() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public static Context getAppContext() {
        return sAppContext;
    }

    public static String getAppCacheDir() {
        return sCacheDir;
    }
}
