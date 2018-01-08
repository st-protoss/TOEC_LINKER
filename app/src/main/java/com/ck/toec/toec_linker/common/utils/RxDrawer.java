package com.ck.toec.toec_linker.common.utils;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import io.reactivex.Observable;

/**
 * Created by wm on 2017/11/10.
 */

public class RxDrawer {
    private static final float OFFSET_THRESHOLD = 0.03f;

    public static Observable<Enum> close(final DrawerLayout drawer) {
        return Observable.create(emitter -> {
            drawer.closeDrawer(GravityCompat.START);
            DrawerLayout.DrawerListener listener = new DrawerLayout.SimpleDrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    if (slideOffset < OFFSET_THRESHOLD) {
                        emitter.onNext(Enum.INSTANCE);
                        emitter.onComplete();
                        drawer.removeDrawerListener(this);
                    }
                }
            };
            drawer.addDrawerListener(listener);
        });
    }
}
