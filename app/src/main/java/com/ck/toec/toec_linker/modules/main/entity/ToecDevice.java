package com.ck.toec.toec_linker.modules.main.entity;

import com.ck.toec.toec_linker.modules.device.entity.DeviceRtRule;
import com.ck.toec.toec_linker.modules.device.entity.DeviceWtRule;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wm on 2017/11/14.
 * 设备列表的实体类
 */

public class ToecDevice implements Serializable{
    private String deviceName;
    private String devicePos;
    private String deviceImage;
    private Double jd;
    private Double wd;
    private String deviceParent;
    private String deviceState;
    private String deviceID;


    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevicePos() {
        return devicePos;
    }

    public void setDevicePos(String devicePos) {
        this.devicePos = devicePos;
    }

    public String getDeviceImage() {
        return deviceImage;
    }

    public void setDeviceImage(String deviceImage) {
        this.deviceImage = deviceImage;
    }


    public String getDeviceParent() {
        return deviceParent;
    }

    public void setDeviceParent(String deviceParent) {
        this.deviceParent = deviceParent;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public Double getJd() {
        return jd;
    }

    public void setJd(Double jd) {
        this.jd = jd;
    }

    public Double getWd() {
        return wd;
    }

    public void setWd(Double wd) {
        this.wd = wd;
    }
}
