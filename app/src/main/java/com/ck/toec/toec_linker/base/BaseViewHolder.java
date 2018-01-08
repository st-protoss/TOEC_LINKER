package com.ck.toec.toec_linker.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by wm on 2017/11/10.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder{
    public BaseViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected abstract void bind(T t);
}

