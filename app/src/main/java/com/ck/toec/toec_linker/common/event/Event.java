package com.ck.toec.toec_linker.common.event;

/**
 * Created by wm on 2017/12/18.
 * 事件类
 */

public class Event {
    private int type;
    public Event(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
