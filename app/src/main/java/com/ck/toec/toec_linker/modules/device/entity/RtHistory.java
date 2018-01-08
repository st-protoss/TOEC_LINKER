package com.ck.toec.toec_linker.modules.device.entity;

import java.io.Serializable;

/**
 * Created by wm on 2017/11/28.
 */

public class RtHistory implements Serializable{
    private String rtData;
    private String rtDate;

    public String getRtData() {
        return rtData;
    }

    public void setRtData(String rtData) {
        this.rtData = rtData;
    }

    public String getRtDate() {
        return rtDate;
    }

    public void setRtDate(String rtDate) {
        this.rtDate = rtDate;
    }
}
