package com.ck.toec.toec_linker.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ck.toec.toec_linker.base.BaseApp;

/**
 * Created by wm on 2017/11/10.
 */

public class BarSizeUtil {

    private static int sStatusBarHeight;

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return DensityUtil.dp2px(44);
    }

    public static int getStatusBarHeight() {
        if (sStatusBarHeight == 0) {
            int resourceId =
                    BaseApp.getAppContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = BaseApp.getAppContext().getResources().getDimensionPixelSize(resourceId);
            }
        }
        return sStatusBarHeight;
    }
    public static void setImmersiveStatusBar(@NonNull Activity activity) {
        if (Util.sdkVersionGe21()) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        if (Util.sdkVersionEq(19)) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        activity.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void setImmersiveStatusBarToolbar(@NonNull Toolbar toolbar, Context context) {
        ViewGroup.MarginLayoutParams toolLayoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
        toolLayoutParams.height = BarSizeUtil.getStatusBarHeight() + BarSizeUtil.getActionBarSize(context);
        toolbar.setLayoutParams(toolLayoutParams);
        toolbar.setPadding(0, BarSizeUtil.getStatusBarHeight(), 0, 0);
        toolbar.requestLayout();
    }

}
