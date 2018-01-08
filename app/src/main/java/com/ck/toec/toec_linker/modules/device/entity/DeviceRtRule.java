package com.ck.toec.toec_linker.modules.device.entity;

import java.io.Serializable;

/**
 * Created by toec on 2017/11/14.
 */

public class DeviceRtRule implements Serializable{
    private String rtName;
    private String rtData;
    private String rtImage;
    private String rtUnit;
    private String rtID;


    public String getRtName() {
        return rtName;
    }

    public void setRtName(String rtName) {
        this.rtName = rtName;
    }

    public String getRtData() {
        return rtData;
    }

    public void setRtData(String rtData) {
        this.rtData = rtData;
    }

    public String getRtImage() {
        return rtImage;
    }

    public void setRtImage(String rtImage) {
        this.rtImage = rtImage;
    }

    public String getRtUnit() {
        return rtUnit;
    }

    public void setRtUnit(String rtUnit) {
        this.rtUnit = rtUnit;
    }

    public String getRtID() {
        return rtID;
    }

    public void setRtID(String rtID) {
        this.rtID = rtID;
    }
}
