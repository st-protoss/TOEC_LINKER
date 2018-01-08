package com.ck.toec.toec_linker.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ck.toec.toec_linker.common.event.Event;
import com.ck.toec.toec_linker.common.utils.RxBus;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.reactivestreams.Subscription;

import butterknife.ButterKnife;

/**
 * Created by wm on 2017/11/10.
 */

public abstract class BaseActivity extends RxAppCompatActivity{
    private static String TAG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
        initRxBus();
    }
    private void initRxBus() {
        RxBus.getDefault().toObservable(Event.class)
                .subscribe(event->{
                    if (event.getType()==1){
                        finish();
                    }
                });
    }

    protected abstract int layoutId();

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
