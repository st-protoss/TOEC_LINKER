package com.ck.toec.toec_linker.modules.launch;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * 引导页加载视频
 * Created by wm on 2017/11/21.
 */

public class LoginVideoView extends VideoView {

    public LoginVideoView(Context context){super(context);}
    public LoginVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LoginVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if(widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY){
            setMeasuredDimension(width,height);
        }else{
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }


    }
}
