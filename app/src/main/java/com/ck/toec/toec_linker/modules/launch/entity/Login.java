package com.ck.toec.toec_linker.modules.launch.entity;

/**
 * Created by toec on 2017/11/23.
 */

public class Login {
    private String id;
    private boolean allowed;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isAllowed() {
        return allowed;
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }
}
