package com.ck.toec.toec_linker.modules.device.entity;

import java.util.List;

/**
 * Created by wm on 2017/12/7.
 */

public class DetailPage<T> {
    private List<T> list;
    private int totalPage;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
