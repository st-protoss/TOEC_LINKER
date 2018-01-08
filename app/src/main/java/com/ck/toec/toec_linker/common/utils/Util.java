package com.ck.toec.toec_linker.common.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import com.ck.toec.toec_linker.base.BaseApp;
import com.ck.toec.toec_linker.common.component.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by wm on 2017/11/10.
 */

public class Util {
    /**
     * 只关注是否联网
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    /**
     * 获取顶部status bar 高度
     */
    public static int getStatusBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.i("Status height:" + height);
        return height;
    }

    /**
     * 获取底部 navigation bar 高度
     */
    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.i("Nav height:" + height);
        return height;
    }

    /**
     *
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
    /**
     * Java 中有一个 Closeable 接口,标识了一个可关闭的对象,它只有一个 close 方法.
     */
    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * sdk 版本判断
     * @param version
     * @return
     */
    public static boolean sdkVersionGe(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    public static boolean sdkVersionEq(int version) {
        return Build.VERSION.SDK_INT == version;
    }

    public static boolean sdkVersionLt(int version) {
        return Build.VERSION.SDK_INT < version;
    }

    public static boolean sdkVersionGe19() {
        return sdkVersionGe(19);
    }

    public static boolean sdkVersionGe21() {
        return sdkVersionGe(21);
    }

    /**
     * Toast封装
     * @param msg
     */
    public static void showShort(String msg) {
        Toast.makeText(BaseApp.getAppContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String msg) {
        Toast.makeText(BaseApp.getAppContext(), msg, Toast.LENGTH_LONG).show();
    }
    public static void copyToClipboard(String info, Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        Util.showShort(String.format("[%s] 已经复制到剪贴板！", info));
    }
}
