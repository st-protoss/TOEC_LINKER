package com.ck.toec.toec_linker.modules.device.entity;

import java.io.Serializable;

/**
 * Created by toec on 2017/11/14.
 */

public class DeviceWtRule implements Serializable{
    private String wtName;
    private String content0;
    private String content1;
    private String wtImage;
    private String wtID;


    public String getWtName() {
        return wtName;
    }

    public void setWtName(String wtName) {
        this.wtName = wtName;
    }

    public String getContent0() {
        return content0;
    }

    public void setContent0(String content0) {
        this.content0 = content0;
    }

    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getWtImage() {
        return wtImage;
    }

    public void setWtImage(String wtImage) {
        this.wtImage = wtImage;
    }

    public String getWtID() {
        return wtID;
    }

    public void setWtID(String wtID) {
        this.wtID = wtID;
    }
}
