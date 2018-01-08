package com.ck.toec.toec_linker.common.utils;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by wm on 2017/12/18.
 * 事件总线封装类
 */

public class RxBus {
    private final Subject<Object> mBus;

    public RxBus() {
        mBus = PublishSubject.create();
    }

    private static class RxBusHolder {
        private  static final RxBus mInstance = new RxBus();
    }
    public static RxBus getDefault(){
        return RxBusHolder.mInstance;
    }
    public void post(Object o){
        mBus.onNext(o);
    }
    public <T> Observable<T> toObservable(Class<T> eventType){
        return mBus.ofType(eventType);
    }
}
