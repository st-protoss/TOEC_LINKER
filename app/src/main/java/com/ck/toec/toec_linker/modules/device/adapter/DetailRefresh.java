package com.ck.toec.toec_linker.modules.device.adapter;

/**
 * Created by wm on 2017/12/5.
 * 详情信息局部刷新的回调接口
 */

public interface DetailRefresh {
    public void rtRefresh();
    public void lineRefresh(String refreshType);
    public void chartRefresh(int pageNum);
    public void alarmRefresh(int apgeNum);
}
