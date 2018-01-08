package com.ck.toec.toec_linker.modules.device.entity;

import java.io.Serializable;

/**
 * Created by wm on 2017/11/28.
 */

public class AlarmData implements Serializable{
    private String alData;
    private String alDate;

    public String getAlData() {
        return alData;
    }

    public void setAlData(String alData) {
        this.alData = alData;
    }

    public String getAlDate() {
        return alDate;
    }

    public void setAlDate(String alDate) {
        this.alDate = alDate;
    }
}
