package com.ck.toec.toec_linker.common.component;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by wm on 2017/11/10.
 */

public class ImageLoader {
    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context).load(imageRes).crossFade().into(view);
    }
    public static void loadNetImage(Context context,String url,ImageView view){
        Glide.with(context).load(url).asBitmap().into(view);
        //.error(R.drawable.error_image)添加错误处理图片 。diskCacheStrategy(DiskCacheStrategy.NONE)禁用缓存
    }

    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }
}
