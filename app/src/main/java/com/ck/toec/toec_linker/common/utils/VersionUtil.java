package com.ck.toec.toec_linker.common.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

import com.ck.toec.toec_linker.R;
import com.ck.toec.toec_linker.common.component.RetrofitUtil;
import com.ck.toec.toec_linker.modules.main.entity.Version;

/**
 * Created by wm on 2017/11/10.
 */

public class VersionUtil {
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            //return context.getString(R.string.can_not_find_version_name);
            return null;
        }
    }

    /**
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void checkVersion(Context context) {
/*        RetrofitSingleton.getInstance()
                .fetchVersion()
                .doOnNext(version -> {
                    String firVersionName = version.versionShort;
                    String currentVersionName = VersionUtil.getVersion(context);
                    if (currentVersionName.compareTo(firVersionName) < 0) {
                        if (!SharedPreferenceUtil.getInstance().getString("version", "").equals(version.versionShort)) {
                            showUpdateDialog(version, context);
                        }
                    }
                })
                .subscribe();*/
    }

    public static void checkVersion(Context context, boolean force) {
/*        RetrofitUtil.getInstance()
                .fetchVersion()
                .doOnNext(version -> {
                    String firVersionName = version.versionShort;
                    String currentVersionName = VersionUtil.getVersion(context);
                    if (currentVersionName.compareTo(firVersionName) < 0) {
                        showUpdateDialog(version, context);
                    } else {
                        Util.showShort("已经是最新版本!");
                    }
                })
                .subscribe();*/
    }

    private static void showUpdateDialog(Version versionAPI, final Context context) {
        String title = "发现新版" + versionAPI.name + "版本号：" + versionAPI.versionShort;

        new AlertDialog.Builder(context).setTitle(title)
                .setMessage(versionAPI.changelog)
                .setPositiveButton("下载", (dialog, which) -> {
                    Uri uri = Uri.parse(versionAPI.updateUrl);
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    context.startActivity(intent);
                })
                .setNegativeButton("跳过此版本",
                        (dialog, which) -> SharedPreferenceUtil.getInstance().putString("version", versionAPI.versionShort))
                .show();
    }
}
